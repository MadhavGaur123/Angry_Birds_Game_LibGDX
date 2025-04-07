package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.Serializable;

public class StartScreen implements Screen , Serializable {
    private Music backgroundMusic;
    private final Main game;
    private Texture backgroundTexture;
    private Texture startTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite startSprite;
    private Vector3 touchPos;
    private Sound buttonClickSound;
    public StartScreen(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 480);
        backgroundTexture = new Texture("background1.png");
        startTexture = new Texture("play_button2.png");
        startSprite = new Sprite(startTexture);

        float spriteWidth = viewport.getWorldWidth() * 0.2f;
        float spriteHeight = viewport.getWorldHeight() * 0.2f;
        startSprite.setSize(spriteWidth, spriteHeight);
        startSprite.setPosition(viewport.getWorldWidth() * 0.4f-20, viewport.getWorldHeight() * 0.4f-170);

        touchPos = new Vector3();
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
    }

    @Override
    public void show() {
        System.out.println("Start Screen is now active.");

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        startSprite.draw(spriteBatch);
        spriteBatch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);
            if (startSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Start button touched! Switching screens...");
                buttonClickSound.play();
                game.setScreen(game.HomeScreen);
            }


        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        startTexture.dispose();
    }
}
