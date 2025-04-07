package APProject.project;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
public class Level3 implements Screen, Serializable{
    private transient BitmapFont healthFont;
    private transient ShapeRenderer shapeRenderer;
    private transient GameContactListener3 contactListener3;
    private  transient Music backgroundMusic;
    private transient  Main game;
    private  transient SpriteBatch spriteBatch;
    private transient Texture backgroundTexture;
    public List<Block> blocks;
    public List<StaticBlock> Staticblocks;
    public List<IceBlock> Iceblocks;
    public List<Stone> stones;
    private List<Pig> pigs;
    public List<TNT> tnts;
    private List<RedBird> redBirds;
    private List<YellowBird> yellowBirds;
    private List<BlackBird> blackBirds;
    private transient Texture pauseTexture, winTexture, loseTexture, slingshotTexture;
    private transient OrthographicCamera camera;
    public transient World world;
    private transient Viewport viewport;
    private transient Vector3 touchPos;
    private transient Sprite pauseSprite;
    private transient Sprite slingshotSprite;
    private transient Box2DDebugRenderer debugRenderer;
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1080;
    private transient Sound buttonClickSound;
    private static final float WAIT_TIME = 20.0f;
    private int numberofbirds = 4;
    private int numberofpigs = 2;
    public Level3(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        // Initialize Box2D world with gravity
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Load textures
        backgroundTexture = new Texture("11.png");
        pauseTexture = new Texture("pause.png");
        winTexture = new Texture("you_win.png");
        loseTexture = new Texture("you_lose.png");
        slingshotTexture = new Texture("slingshot1.png");
        Iceblocks=new ArrayList<>();
        // Initialize pause button sprite
        pauseSprite = new Sprite(pauseTexture);
        pauseSprite.setSize(WORLD_WIDTH * 0.1f, WORLD_HEIGHT * 0.1f);
        pauseSprite.setPosition(10, WORLD_HEIGHT - pauseSprite.getHeight() - 10);

        // Initialize slingshot sprite
        slingshotSprite = new Sprite(slingshotTexture);
        slingshotSprite.setSize(WORLD_WIDTH * 0.15f, WORLD_HEIGHT * 0.3f);
        slingshotSprite.setPosition(100, 60); // Adjusted position

        // Load audio assets
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level1.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        // Initialize game objects
        Staticblocks=new ArrayList<>();
        blackBirds=new ArrayList<>();
        stones=new ArrayList<>();
        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        redBirds = new ArrayList<>();
        yellowBirds = new ArrayList<>();
        tnts=new ArrayList<>();
        createMap();
        touchPos = new Vector3();

        this.contactListener3 = new GameContactListener3(this);
        world.setContactListener(this.contactListener3);
        shapeRenderer = new ShapeRenderer();
        healthFont = new BitmapFont();
        healthFont.setColor(Color.WHITE);
        healthFont.getData().setScale(2);
    }
    private void renderTrajectory(Vector2 origin, Vector2 initialVelocity, Vector2 acceleration, int pointCount, float deltaTime) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float opacity = 1.0f;
        float opacityDecrement = 1.0f / pointCount;
        float maxDotSize = 5f;
        float minDotSize = 2f;

        for (int step = 1; step <= pointCount; step++) {
            float elapsedTime = step * deltaTime;

            // Calculate position using physics equations
            Vector2 currentPos = new Vector2(
                origin.x + initialVelocity.x * elapsedTime,
                origin.y + initialVelocity.y * elapsedTime +
                    0.5f * acceleration.y * elapsedTime * elapsedTime
            );

            // Calculate dot size with linear interpolation
            float dotSize = maxDotSize - (step * (maxDotSize - minDotSize) / pointCount);

            // Draw the dot
            shapeRenderer.circle(currentPos.x * 100, currentPos.y * 100, dotSize);

            // Update opacity
            opacity -= opacityDecrement;
            opacity = Math.max(0, opacity);

            // Stop if projectile hits ground
            if (currentPos.y < 0) {
                break;
            }
        }

        shapeRenderer.end();
    }
    private void createMap() {
        // Base positions for blocks
        float blockStartX = WORLD_WIDTH * 0.7f; // Position blocks on the right side
        float blockStartY = 60; // Ground position for the first block
        StaticBlock groundBlock = new StaticBlock(world, WORLD_WIDTH / 2, blockStartY, "base.png", WORLD_WIDTH, 1f, false,1f);
        groundBlock.getBody().setType(BodyDef.BodyType.StaticBody);
//        Block leftwall = new Block(world, -12, WORLD_HEIGHT / 2, "vertical.png", 0.1f, WORLD_HEIGHT, false, 1f);
//        leftwall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static
//
//
//        Block topWall = new Block(world, WORLD_WIDTH / 2, WORLD_HEIGHT - 10, "horizontal.png", WORLD_WIDTH, 0.1f, false, 1f);
//        topWall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static
        Block rightWall = new Block(world, WORLD_WIDTH - 10, WORLD_HEIGHT / 2, "vertical.png", 0.1f, WORLD_HEIGHT, false, 1f);
        rightWall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static
        // **Upper wall**
        Block upperWall = new Block(world, WORLD_WIDTH / 2, WORLD_HEIGHT - 10, "horizontal.png", WORLD_WIDTH, 0.1f, false, 1f);
        upperWall.getBody().setType(BodyDef.BodyType.StaticBody);

// Add the right wall to the blocks list
        blocks.add(rightWall);
//        blocks.add(leftwall);
        blocks.add(upperWall);
        Block block1 = new Block(world,1500, 240, "wooden_block.png", 0.2f, 0.2f,true,1f);
        Block block2 = new Block(world,1500, 140, "wooden_block.png", 0.25f, 0.25f,true,1f);
        Block block4 = new Block(world,1000, 240, "wooden_block.png", 0.2f, 0.2f,true,1f); // First block
        Block block5 = new Block(world,1000, 140, "wooden_block.png", 0.25f, 0.25f,true,1f);
        IceBlock middle = new IceBlock(world,1250, 250, "horizontal_ice.png", 1.7f, 1f,true,1f);
        TNT tnt = new TNT(world, 1250, 150, "tnt.png", 0.12f, 0.12f, 1f); // Position, size, density
        Block t1 = new Block(world,1520, 650, "triangle.png", 01f, 01f,true,1f);
        Block t2 = new Block(world,990, 650, "triangle.png", 01f, 01f,true,1f);
        //TNT tnt2 = new TNT(world, 900, 150, "tnt.png", 0.12f, 0.12f, 1f);
        Block l_vertical_l = new Block(world,1460,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block l_vertical_r = new Block(world,1580,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block horizontal_r = new Block(world,1510,550 , "horizontal.png", 0.8f, 0.6f,true,1f);

        Pig pig1_l = new Pig(world,1500, 350, "helment_pig.png", 0.7f, 0.7f);
        pig1_l.health = 440.0f;
        Stone stone1=new Stone(world,1220,350,"2.png",0.8f,0.8f,true,5f);

        Block r_vertical_l = new Block(world,940,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block r_vertical_r = new Block(world,1050,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block horizontal_l = new Block(world,990,550 , "horizontal.png", 0.8f, 0.6f,true,1f);
//        Block horizontal_m = new Block(world,1200,550 , "horizontal.png", 0.7f, 0.6f,true,1f);



        Pig pig1_r = new Pig(world,1000, 350, "helment_pig.png", 0.7f, 0.7f);
        pig1_r.health = 440.0f;
        RedBird redBird1 = new RedBird(world, 130, 80, 1.3f, 1.3f);
        RedBird redBird2 = new RedBird(world, 200, 80, 1.3f, 1.3f);
        YellowBird yellowbird = new YellowBird(world, 80, 80, 1.6f, 1.6f);
        BlackBird blackbird = new BlackBird(world,30,80,1.6f,1.6f,this);
        Iceblocks.add(middle);
        Staticblocks.add(groundBlock);
        blocks.add(t1);
        blocks.add(t2);
        blocks.add(block1);
        blocks.add(block2);
        blocks.add(block4);
        blocks.add(block5);
        blocks.add(r_vertical_r);
        blocks.add(r_vertical_l);
        blocks.add(horizontal_l);
        blocks.add(l_vertical_l);
        blocks.add(l_vertical_r);
        //blocks.add(rightWall);
        blocks.add(horizontal_r);
        redBirds.add(redBird1);
        redBirds.add(redBird2);
        yellowBirds.add(yellowbird);
        pigs.add(pig1_r);
        pigs.add(pig1_l);
        tnts.add(tnt);

        stones.add(stone1);
        blackBirds.add(blackbird);
    }
    public void saveGameState() {
        DataofLevel3 state = new DataofLevel3();
        for (RedBird bird : redBirds) {
            state.birdPositionsred.add(bird.getBody().getPosition());
            state.birdstatusred.add(bird.isshot);
        }
        for (YellowBird bird : yellowBirds) {
            state.birdPositionsyellow.add(bird.getBody().getPosition());
            state.birdstatusyellow.add(bird.isshot);
        }
        for (BlackBird bird : blackBirds) {
            state.birdPositionsblack.add(bird.getBody().getPosition());
            state.birdstatusblack.add(bird.isshot);
        }
        for (Pig pig : pigs) {
            state.pigPositions.add(pig.getBody().getPosition());
        }
        for (Block block : blocks) {
            state.blockPositions.add(block.getBody().getPosition());
        }
        for (TNT block : tnts) {
            state.blockPositions.add(block.getBody().getPosition());

        }


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game_state3.ser"))) {
            oos.writeObject(state);
            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game_state3.ser"))) {
            DataofLevel3 state = (DataofLevel3) ois.readObject();

            // Restore birds' positions
            for (int i = 0; i < state.birdPositionsred.size(); i++) {
                Vector2 position = state.birdPositionsred.get(i);
                boolean isshot = state.birdstatusred.get(i);
                redBirds.get(i).getBody().setTransform(position, 0);
                redBirds.get(i).isshot = isshot;

            }
            for (int i = 0; i < state.birdPositionsyellow.size(); i++) {
                Vector2 position = state.birdPositionsyellow.get(i);
                boolean isshot = state.birdstatusyellow.get(i);
                yellowBirds.get(i).getBody().setTransform(position, 0);
                yellowBirds.get(i).isshot = isshot;

            }
            for (int i = 0; i < state.birdPositionsblack.size(); i++) {
                Vector2 position = state.birdPositionsblack.get(i);
                boolean isshot = state.birdstatusblack.get(i);
                blackBirds.get(i).getBody().setTransform(position, 0);
                blackBirds.get(i).isshot = isshot;

            }

            // Restore pigs' positions
            for (int i = 0; i < state.pigPositions.size(); i++) {
                Vector2 position = state.pigPositions.get(i);
                pigs.get(i).getBody().setTransform(position, 0);
            }

            // Restore blocks' positions
            for (int i = 0; i < state.blockPositions.size(); i++) {
                Vector2 position = state.blockPositions.get(i);
                blocks.get(i).getBody().setTransform(position, 0);
            }

            for (int i = 0; i < state.tntpositions.size(); i++) {
                Vector2 position = state.tntpositions.get(i);
                tnts.get(i).getBody().setTransform(position, 0);

            }

            System.out.println("Game state loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(Color.BLACK);
        camera.update();

        // Update physics world
        world.step(1 / 60f, 6, 2);
        contactListener3.processBodyRemovalQueue();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Draw background
        spriteBatch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw slingshot
        slingshotSprite.draw(spriteBatch);
        blocks.removeIf(block -> block.getBody() == null);
        // Draw game objects
        for (Block block : blocks) {
            block.render(spriteBatch);
        }
        for (StaticBlock block : Staticblocks) {
            block.render(spriteBatch);
        }
        for (IceBlock block : Iceblocks) {
            block.render(spriteBatch);
        }
        for (TNT tnt : tnts) {
            tnt.render(spriteBatch);
        }
        float healthY = WORLD_HEIGHT - 50;
        for (int i = 0; i < pigs.size(); i++) {
            Pig pig = pigs.get(i);
            pig.render(spriteBatch);

            // Display pig health in top right corner
            String healthText = "Pig " + (i + 1) + " Health: " + pig.health;
            healthFont.draw(spriteBatch, healthText, WORLD_WIDTH - 300, healthY - (i * 40));
        }
        for (BlackBird b : blackBirds) {
            b.render(spriteBatch);
        }
        for (Stone stone : stones) {
            stone.render(spriteBatch);
        }
        // Draw birds
        for (RedBird bird : redBirds) {
            bird.render(spriteBatch);
        }
        for (YellowBird bird : yellowBirds) {
            bird.render(spriteBatch);
        }

        // Draw pause button
        pauseSprite.draw(spriteBatch);

        spriteBatch.end();
        for (RedBird bird : redBirds) {
            if (bird.isDragging) {
                Vector2 birdPosition = bird.getBody().getPosition();
                Vector2 slingshotPosition = bird.slingshotPosition;
                Vector2 launchVelocity = new Vector2(
                    slingshotPosition.x - birdPosition.x,
                    slingshotPosition.y - birdPosition.y
                ).scl(8.0f);

                renderTrajectory(birdPosition, launchVelocity, new Vector2(0, -9.8f), 100, 0.1f);
                break;
            }
        }
        for (YellowBird bird : yellowBirds) {
            if (bird.isDragging) {
                Vector2 birdPosition = bird.getBody().getPosition();
                Vector2 slingshotPosition = bird.slingshotPosition;
                Vector2 launchVelocity = new Vector2(
                    slingshotPosition.x - birdPosition.x,
                    slingshotPosition.y - birdPosition.y
                ).scl(9.3f);

                renderTrajectory(birdPosition, launchVelocity, new Vector2(0, -9.8f), 100, 0.1f);
                break;
            }
        }
        for (BlackBird bird : blackBirds) {
            if (bird.isDragging) {
                Vector2 birdPosition = bird.getBody().getPosition();
                Vector2 slingshotPosition = bird.slingshotPosition;
                Vector2 launchVelocity = new Vector2(
                    slingshotPosition.x - birdPosition.x,
                    slingshotPosition.y - birdPosition.y
                ).scl(8.0f);

                renderTrajectory(birdPosition, launchVelocity, new Vector2(0, -9.8f), 100, 0.1f);
                break;
            }
        }
        // Handle input
        handleInput();
        checkLevelStatus();

        debugRenderer.render(world, camera.combined);
    }


    private boolean isMovementStopped() {
        float velocityThreshold = 0.1f;
        boolean allStopped = true;

        // Check birds
        for (RedBird bird : redBirds) {
            if (bird != null && bird.getBody() != null) {
                Vector2 velocity = bird.getBody().getLinearVelocity();
                if (velocity.len() > velocityThreshold) {
                    return false;
                }
            }
        }
        for (YellowBird bird : yellowBirds) {
            if (bird != null && bird.getBody() != null) {
                Vector2 velocity = bird.getBody().getLinearVelocity();
                if (velocity.len() > velocityThreshold) {
                    return false;
                }
            }
        }
        for (BlackBird bird : blackBirds) {
            if (bird != null && bird.getBody() != null) {
                Vector2 velocity = bird.getBody().getLinearVelocity();
                if (velocity.len() > velocityThreshold) {
                    return false;
                }
            }
        }

        // Check pigs
        for (Pig pig : pigs) {
            if (pig != null && pig.getBody() != null) {
                Vector2 velocity = pig.getBody().getLinearVelocity();
                if (velocity.len() > velocityThreshold) {
                    return false;
                }
            }
        }

        // Check blocks
        for (Block block : blocks) {
            if (block != null && block.getBody() != null) {
                Vector2 velocity = block.getBody().getLinearVelocity();
                if (velocity.len() > velocityThreshold) {
                    return false;
                }
            }
        }

        return true;
    }
    private void checkLevelStatus() {
        if(!isMovementStopped()){
            return;
        }
        int k = 0;
        int z = 0;
        for (RedBird bird : redBirds) {
            if (bird.isshot == true) {
                k = k + 1;
            }
        }
        for (YellowBird bird : yellowBirds) {
            if (bird.isshot == true) {
                k = k + 1;
            }
        }
        for (BlackBird bird : blackBirds) {
            if (bird.isshot == true) {
                k = k + 1;
            }
        }
        for (Pig pig : pigs) {
//            float terr = pig.getSprite().getY();
//            if (terr < 103) {
//                z = z + 1;
//            }
            if(pig.isdead == true){
                z = z+1;
            }

        }
        if (z == numberofpigs) {
            game.setScreen(game.Win3);
        } else if (k == numberofbirds && z < numberofpigs) {
            game.setScreen(game.lose3);
        }

    }
    private void handleInput() {
        if (Gdx.input.justTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            // Check for bird selection
            for (RedBird bird : redBirds) {
                if (bird.getSprite().getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    if (!bird.isAtSlingshot) {
                        // Move the bird to the slingshot
                        bird.moveToSlingshot(
                            255,
                            310
                        );
                    } else if (!bird.isDragging) {
                        // Activate for dragging
                        bird.activateForDragging();
                    }
                    break;
                }
            }
            for (YellowBird bird : yellowBirds) {
                if (bird.getSprite().getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    if (!bird.isAtSlingshot) {
                        // Move the bird to the slingshot
                        bird.moveToSlingshot(
                            255,
                            310
                        );
                    } else if (!bird.isDragging) {
                        // Activate for dragging
                        bird.activateForDragging();
                    }
                    break;
                }
            }
            for (BlackBird bird : blackBirds) {
                if (bird.getSprite().getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    if (!bird.isAtSlingshot) {
                        // Move the bird to the slingshot
                        bird.moveToSlingshot(
                            255,
                            310
                        );
                    } else if (!bird.isDragging) {
                        // Activate for dragging
                        bird.activateForDragging();
                    }
                    break;
                }
            }

            if (pauseSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                buttonClickSound.play();
                game.setScreen(game.PauseScreen3);
            }
        }


        if (Gdx.input.isTouched() && !Gdx.input.justTouched()) {
            for (RedBird bird : redBirds) {
                if (bird.isDragging) {
                    // Update dragging
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(touchPos);
                    bird.updateDragging(touchPos.x, touchPos.y);
                    break;
                }
            }
            for (YellowBird bird : yellowBirds) {
                if (bird.isDragging) {
                    // Update dragging
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(touchPos);
                    bird.updateDragging(touchPos.x, touchPos.y);
                    break;
                }
            }
            for (BlackBird bird : blackBirds) {
                if (bird.isDragging) {
                    // Update dragging
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(touchPos);
                    bird.updateDragging(touchPos.x, touchPos.y);
                    break;
                }
            }
        }


        if (!Gdx.input.isTouched()) {
            for (RedBird bird : redBirds) {
                if (bird.isDragging) {
                    bird.shoot();
                    break;
                }
            }
            for (YellowBird bird : yellowBirds) {
                if (bird.isDragging) {
                    bird.shoot();
                    break;
                }
            }
            for (BlackBird bird : blackBirds) {
                if (bird.isDragging) {
                    bird.shoot();
                    break;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            blackBirds.get(0).triggerExplosion(blackBirds.get(0).getPosition());
            System.out.println("black bird exploded");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            saveGameState();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            loadGameState();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        pauseTexture.dispose();
        slingshotTexture.dispose();
        winTexture.dispose();
        loseTexture.dispose();
        backgroundMusic.dispose();
        buttonClickSound.dispose();
        world.dispose();
        debugRenderer.dispose();

        for (RedBird bird : redBirds) {
            bird.dispose();
        }
        for (YellowBird bird : yellowBirds) {
            bird.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        for (Block block : blocks) {
            block.dispose();
        }
    }

    @Override
    public void show() {
        if(game.music%2==0){
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
    }
    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
