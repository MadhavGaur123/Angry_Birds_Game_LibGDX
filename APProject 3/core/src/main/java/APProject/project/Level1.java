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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Level1 implements Screen, Serializable {
    private transient BitmapFont healthFont;
    private transient ShapeRenderer shapeRenderer;
    private transient GameContactListener contactListener;
    private transient Music backgroundMusic;
    private transient final Main game;
    private transient SpriteBatch spriteBatch;
    private transient Texture backgroundTexture;
    public List<Block> blocks;
    public List<Pig> pigs;
    public List<RedBird> redBirds;
    private transient Texture pauseTexture, winTexture, loseTexture, slingshotTexture;
    private  transient OrthographicCamera camera;
    public transient World world;
    private transient Viewport viewport;
    private transient Vector3 touchPos;
    private transient Sprite pauseSprite;
    private transient Sprite slingshotSprite;
    private transient Box2DDebugRenderer debugRenderer;
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1080;
    private transient Sound buttonClickSound;
    private transient Sound selectSound;
    private transient Sound stretchSound;
    private static final float WAIT_TIME = 20.0f;
    private int numberofbirds = 2;
    private int numberofpigs = 1;
    ScoreDisplay scoreDisplay;
    public int score;
    public Level1(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        world = new World(new Vector2(0, -9.8f), true);
        backgroundTexture = new Texture("11.png");
        pauseTexture = new Texture("pause.png");
        winTexture = new Texture("you_win.png");
        loseTexture = new Texture("you_lose.png");
        slingshotTexture = new Texture("slingshot1.png");

        pauseSprite = new Sprite(pauseTexture);
        pauseSprite.setSize(WORLD_WIDTH * 0.1f, WORLD_HEIGHT * 0.1f);
        pauseSprite.setPosition(10, WORLD_HEIGHT - pauseSprite.getHeight() - 10);

        slingshotSprite = new Sprite(slingshotTexture);
        slingshotSprite.setSize(WORLD_WIDTH * 0.15f, WORLD_HEIGHT * 0.3f);
        slingshotSprite.setPosition(100, 60); // Adjusted position


        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level1.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        selectSound = Gdx.audio.newSound(Gdx.files.internal("select.mp3"));
        stretchSound = Gdx.audio.newSound(Gdx.files.internal("stretch.mp3"));

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        score=0;
        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        redBirds = new ArrayList<>();
        scoreDisplay=new ScoreDisplay(score);
        createMap();
        touchPos = new Vector3();
        this.contactListener = new GameContactListener(this);
        world.setContactListener(this.contactListener);
        shapeRenderer = new ShapeRenderer();
        healthFont = new BitmapFont();
        healthFont.setColor(Color.WHITE);
        healthFont.getData().setScale(2);



    }
    public Level1() {
        this.game = null;
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        world = new World(new Vector2(0, -9.8f), true);
        backgroundTexture = new Texture("11.png");
        pauseTexture = new Texture("pause.png");
        winTexture = new Texture("you_win.png");
        loseTexture = new Texture("you_lose.png");
        slingshotTexture = new Texture("slingshot1.png");

        pauseSprite = new Sprite(pauseTexture);
        pauseSprite.setSize(WORLD_WIDTH * 0.1f, WORLD_HEIGHT * 0.1f);
        pauseSprite.setPosition(10, WORLD_HEIGHT - pauseSprite.getHeight() - 10);

        slingshotSprite = new Sprite(slingshotTexture);
        slingshotSprite.setSize(WORLD_WIDTH * 0.15f, WORLD_HEIGHT * 0.3f);
        slingshotSprite.setPosition(100, 60); // Adjusted position


        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level1.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        selectSound = Gdx.audio.newSound(Gdx.files.internal("select.mp3"));
        stretchSound = Gdx.audio.newSound(Gdx.files.internal("stretch.mp3"));

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        score=0;
        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        redBirds = new ArrayList<>();
        scoreDisplay=new ScoreDisplay(score);
        createMap();
        touchPos = new Vector3();
        this.contactListener = new GameContactListener(this);
        world.setContactListener(this.contactListener);
        shapeRenderer = new ShapeRenderer();



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
        float blockStartX = WORLD_WIDTH * 0.7f;
        float blockStartY = 60;
        Block groundBlock = new Block(world, WORLD_WIDTH / 2, blockStartY, "base.png", WORLD_WIDTH, 1f, false,1f);
        groundBlock.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static
        Block rightWall = new Block(world, WORLD_WIDTH - 10, WORLD_HEIGHT / 2, "vertical.png", 0.1f, WORLD_HEIGHT, false, 1f);
        rightWall.getBody().setType(BodyDef.BodyType.StaticBody);  // Make it static

// Add the right wall to the blocks list
        blocks.add(rightWall);

        Block block1 = new Block(world,1500, 240, "wooden_block.png", 0.18f, 0.18f,true,1f);
        Block block2 = new Block(world,1500, 140, "wooden_block.png", 0.18f, 0.18f,true,1f);

        Block l_vertical_l = new Block(world,1440,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block l_vertical_r = new Block(world,1560,450 , "vertical.png", 1f, 0.6f,true,1f);
        Block horizontal_r = new Block(world,1500,550 , "horizontal.png", 0.7f, 0.6f,true,1f);

        Pig pig1_l = new Pig(world,1500, 350, "pig1.png", 0.06f, 0.06f);

        RedBird redBird1 = new RedBird(world, 100, 80, 1.3f, 1.3f);
        RedBird redBird2 = new RedBird(world, 200, 80, 1.3f, 1.3f);
        blocks.add(groundBlock);
        blocks.add(block1);
        blocks.add(block2);
        blocks.add(l_vertical_l);
        blocks.add(l_vertical_r);
        blocks.add(horizontal_r);
        redBirds.add(redBird1);
        redBirds.add(redBird2);
        pigs.add(pig1_l);
    }

    public void saveGameState() {
        DataofLevel1 state = new DataofLevel1();

        // Save birds' positions
        for (RedBird bird : redBirds) {
            state.birdPositions.add(bird.getBody().getPosition());
            state.birdstatus.add(bird.isshot);
        }

        // Save pigs' positions
        for (Pig pig : pigs) {
            state.pigPositions.add(pig.getBody().getPosition());
            state.pighealth.add(pig.health);
        }

        // Save blocks' positions
        for (Block block : blocks) {
            state.blockPositions.add(block.getBody().getPosition());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("game_state.ser"))) {
            oos.writeObject(state);
            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game_state.ser"))) {
            DataofLevel1 state = (DataofLevel1) ois.readObject();

            // Restore birds' positions
            for (int i = 0; i < state.birdPositions.size(); i++) {
                Vector2 position = state.birdPositions.get(i);
                boolean isshot = state.birdstatus.get(i);
                redBirds.get(i).getBody().setTransform(position, 0);
                redBirds.get(i).isshot = isshot;
                if(isshot == true){
                    redBirds.get(i).getBody().setType(BodyDef.BodyType.StaticBody);
                }

            }

            // Restore pigs' positions
            for (int i = 0; i < state.pigPositions.size(); i++) {
                Vector2 position = state.pigPositions.get(i);
                pigs.get(i).getBody().setTransform(position, 0);
                pigs.get(i).health = state.pighealth.get(i);
            }

            // Restore blocks' positions
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
        contactListener.processBodyRemovalQueue();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Draw background
        spriteBatch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw slingshot
        slingshotSprite.draw(spriteBatch);

        // Draw game objects
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

        // Draw birds
        for (RedBird bird : redBirds) {
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
        // Handle input
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
        for (Pig pig : pigs) {
//            float terr = pig.getSprite().getY();
//            if (terr < 103) {
//                z = z + 1;
//            }
            if(pig.isdead == true){
                z = z + 1;
            }
        }
        if (z == numberofpigs) {
            game.setScreen(game.Win);
            game.setWins(1);
        } else if (k == numberofbirds && z != numberofpigs) {
            game.setScreen(game.lose);
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
                        selectSound.play();
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
                game.setScreen(game.PauseScreen);
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
        }
        if (!Gdx.input.isTouched()) {
            for (RedBird bird : redBirds) {
                if (bird.isDragging) {
                    bird.shoot();
                    break;
                }
            }
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
