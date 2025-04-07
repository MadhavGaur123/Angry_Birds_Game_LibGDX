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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import java.util.Map;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
public class Level2 implements Screen , Serializable {
    private transient BitmapFont healthFont;
    private transient ShapeRenderer shapeRenderer;
    private transient  GameContactListener2 contactListener2;
    private transient Music backgroundMusic;
    private transient final Main game;
    private transient SpriteBatch spriteBatch;
    private transient Texture backgroundTexture;
    public List<Block> blocks;
    public List<Integer> blockstoRemove;
    public List<StaticBlock> Staticblocks;
    private List<Pig> pigs;
    private List<RedBird> redBirds;
    public List<BlueBird> blueBirds;
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
    private int numberofbirds = 3;
    private int numberofpigs = 2;

    public Level2(Main game) {
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

        // Initialize pause button sprite
        pauseSprite = new Sprite(pauseTexture);
        pauseSprite.setSize(WORLD_WIDTH * 0.1f, WORLD_HEIGHT * 0.1f);
        pauseSprite.setPosition(10, WORLD_HEIGHT - pauseSprite.getHeight() - 10);
        Staticblocks=new ArrayList<>();
        slingshotSprite = new Sprite(slingshotTexture);
        slingshotSprite.setSize(WORLD_WIDTH * 0.15f, WORLD_HEIGHT * 0.3f);
        slingshotSprite.setPosition(100, 60); // Adjusted position

        // Load audio assets
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level1.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        // Initialize game objects
        blocks = new ArrayList<>();
        blockstoRemove = new ArrayList<>();
        pigs = new ArrayList<>();
        redBirds = new ArrayList<>();
        blueBirds = new ArrayList<>();

        createMap();
        touchPos = new Vector3();

        //Set up contact listener//
        this.contactListener2=new GameContactListener2(this);
        world.setContactListener(this.contactListener2);
        shapeRenderer = new ShapeRenderer();
        healthFont = new BitmapFont();
        healthFont.setColor(Color.WHITE);
        healthFont.getData().setScale(2);
    }

    private void createMap() {
        // Base positions for blocks
        float blockStartX = WORLD_WIDTH * 0.7f; // Position blocks on the right side
        float blockStartY = 60; // Ground position for the first block
        Block groundBlock = new Block(world, WORLD_WIDTH / 2, blockStartY, "base.png", WORLD_WIDTH, 1f, false,1f);
        groundBlock.getBody().setType(BodyDef.BodyType.StaticBody);
        // Create a vertical boundary block on the right edge of the world
        Block rightWall = new Block(world, WORLD_WIDTH - 10, WORLD_HEIGHT / 2, "vertical.png", 0.1f, WORLD_HEIGHT, false, 1f);
        rightWall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static

// Add the right wall to the blocks list
        blocks.add(rightWall);



// Create a top wall (static horizontal block)
        Block topWall = new Block(world, WORLD_WIDTH / 2, WORLD_HEIGHT - 10, "horizontal.png", WORLD_WIDTH, 0.1f, false, 1f);
        topWall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static
        Block block1 = new Block(world,1500, 240, "rock.png", 1.75f, 1.75f,true,1f);
        Block block2 = new Block(world,1500, 140, "rock.png", 1.75f, 1.75f,true,1f);
        Block block4 = new Block(world,1000, 240, "rock.png", 1.75f, 1.75f,true,1f); // First block
        Block block5 = new Block(world,1000, 140, "rock.png", 1.75f, 1.75f,true,1f);


        Block l_vertical_l = new Block(world,1440,450 , "vertical_stone.png", 1f, 0.6f,true,1f);
        Block l_vertical_r = new Block(world,1560,450 , "vertical_stone.png", 1f, 0.6f,true,1f);
        Block horizontal_r = new Block(world,1500,550 , "horizontal_stone.png", 0.7f, 0.6f,true,1f);


        Block r_vertical_l = new Block(world,940,450 , "vertical_stone.png", 1f, 0.6f,true,1f);
        Block r_vertical_r = new Block(world,1060,450 , "vertical_stone.png", 1f, 0.6f,true,1f);
        Block horizontal_l = new Block(world,1000,550 , "horizontal_stone.png", 0.7f, 0.6f,true,1f);


        Pig pig1_l = new Pig(world,1500, 350, "big.png", 0.7f, 0.7f);
        pig1_l.health = 330.0f;
        Pig pig1_r = new Pig(world,1000, 350, "big.png", 0.7f, 0.7f);
        pig1_r.health = 330.0f;


        RedBird redBird1 = new RedBird(world, 100, 80, 1.3f, 1.3f);
        RedBird redBird2 = new RedBird(world, 200, 80, 1.3f, 1.3f);
        BlueBird blueBird=new BlueBird(world,150,80,1.3f,1.3f,this);


        blocks.add(groundBlock);
        blocks.add(rightWall);
        blocks.add(block1);
        blocks.add(block2);
        blocks.add(block4);
        blocks.add(block5);
        blocks.add(r_vertical_r);
        blocks.add(r_vertical_l);
        blocks.add(horizontal_l);
        blocks.add(l_vertical_l);
        blocks.add(l_vertical_r);
        blocks.add(horizontal_r);
        redBirds.add(redBird1);
        redBirds.add(redBird2);
        blueBirds.add(blueBird);
        pigs.add(pig1_r);
        pigs.add(pig1_l);

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
    public void saveGameState() {
        DataofLevel2 state = new DataofLevel2();

        for (RedBird bird : redBirds) {
            state.birdPositionsred.add(bird.getBody().getPosition());
            state.birdstatusred.add(bird.isshot);
        }
        for (BlueBird bird : blueBirds) {
            state.birdPositionsblue.add(bird.getBody().getPosition());
            state.birdstatusblue.add(bird.isshot);
        }
        for (Pig pig : pigs) {
            state.pigPositions.add(pig.getBody().getPosition());
        }
        for (Block block : blocks) {
            state.blockPositions.add(block.getBody().getPosition());
        }



        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game_state2.ser"))) {
            oos.writeObject(state);
            System.out.println("Game state saved successfully.");
            System.out.println(state.blockPositions.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game_state2.ser"))) {
            DataofLevel2 state = (DataofLevel2) ois.readObject();
            for (int i = 0; i < state.birdPositionsred.size(); i++) {
                Vector2 position = state.birdPositionsred.get(i);
                boolean isshot = state.birdstatusred.get(i);
                redBirds.get(i).getBody().setTransform(position, 0);
                redBirds.get(i).isshot = isshot;
                if(isshot == true){
                    redBirds.get(i).getBody().setType(BodyDef.BodyType.StaticBody);
                }
            }
            for (int i = 0; i < state.birdPositionsblue.size(); i++) {
                Vector2 position = state.birdPositionsblue.get(i);
                boolean isshot = state.birdstatusblue.get(i);
                blueBirds.get(i).getBody().setTransform(position, 0);
                blueBirds.get(i).isshot = isshot;
                if(isshot == true){
                    blueBirds.get(i).getBody().setType(BodyDef.BodyType.StaticBody);
                }
            }
            for (int i = 0; i < state.pigPositions.size(); i++) {
                Vector2 position = state.pigPositions.get(i);
                pigs.get(i).getBody().setTransform(position, 0);
            }

            for (int i = 0; i < state.blockPositions.size(); i++) {
                Vector2 position = state.blockPositions.get(i);
                blocks.get(i).getBody().setTransform(position, 0);


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
        contactListener2.processBodyRemovalQueue();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        slingshotSprite.draw(spriteBatch);
        for (Block block : blocks) {
            block.render(spriteBatch);
        }
        float healthY = WORLD_HEIGHT - 50; // Starting Y position for health display
        for (int i = 0; i < pigs.size(); i++) {
            Pig pig = pigs.get(i);
            pig.render(spriteBatch);

            // Display pig health in top right corner
            String healthText = "Pig " + (i + 1) + " Health: " + pig.health;
            healthFont.draw(spriteBatch, healthText, WORLD_WIDTH - 300, healthY - (i * 40));
        }
        for (StaticBlock s : Staticblocks) {
            s.render(spriteBatch);
        }
        for (RedBird bird : redBirds) {
            bird.render(spriteBatch);
        }
        for (BlueBird bird : blueBirds) {
            bird.render(spriteBatch);
        }
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
        for (BlueBird bird : blueBirds) {
            if (bird.isDragging) {
                Vector2 birdPosition = bird.getBody().getPosition();
                Vector2 slingshotPosition = bird.slingshotPosition;
                Vector2 launchVelocity = new Vector2(
                    slingshotPosition.x - birdPosition.x,
                    slingshotPosition.y - birdPosition.y
                ).scl(10.0f);

                renderTrajectory(birdPosition, launchVelocity, new Vector2(0, -9.8f), 100, 0.1f);
                break;
            }
        }
        checkLevelStatus();
        handleInput();
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
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
        for (BlueBird bird : blueBirds) {
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
        if (!isMovementStopped()) {
            return;
        }
        int k = 0;
        int z = 0;
        for (RedBird bird : redBirds) {
            if (bird.isshot == true) {
                k = k + 1;
            }
        }
        for (BlueBird bird : blueBirds) {
            if (bird.isshot == true) {
                k = k + 1;
            }
        }
        for (Pig pig : pigs) {
            if(pig.isdead == true){
                z = z + 1;
            }
        }
        if (z == numberofpigs) {
            game.setScreen(game.Win2);
            game.setWins(2);
        } else if (k == numberofbirds && z != numberofpigs) {
            game.setScreen(game.lose2);
        }
    }
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);
            for (RedBird bird : redBirds) {
                if (bird.getSprite().getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    if (!bird.isAtSlingshot) {
                        bird.moveToSlingshot(
                            255,310
                        );
                    } else if (!bird.isDragging) {
                        bird.activateForDragging();
                    }
                    break;
                }
            }
            for (BlueBird bird : blueBirds) {
                if (bird.getSprite().getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                    if (!bird.isAtSlingshot) {
                        bird.moveToSlingshot(
                            255, 310
                        );
                    } else if (!bird.isDragging) {
                        bird.activateForDragging();
                    }
                    break;
                }
            }
            if (pauseSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                buttonClickSound.play();
                game.setScreen(game.PauseScreen2);
            }
        }
        if (Gdx.input.isTouched() && !Gdx.input.justTouched()) {
            for (RedBird bird : redBirds) {
                if (bird.isDragging) {
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    viewport.unproject(touchPos);
                    bird.updateDragging(touchPos.x, touchPos.y);
                    break;
                }
            }
            for (BlueBird bird : blueBirds) {
                if (bird.isDragging) {
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
            for (BlueBird bird : blueBirds) {
                if (bird.isDragging) {
                    bird.shoot();
                    break;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            blueBirds.get(0).split();
            for(BlueBird i : blueBirds){
                i.isshot = true;
            }
            numberofbirds = numberofbirds + 2;

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
        for (BlueBird bird : blueBirds) {
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
