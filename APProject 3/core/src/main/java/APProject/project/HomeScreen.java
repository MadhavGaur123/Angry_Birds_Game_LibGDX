package APProject.project;
import com.badlogic.gdx.Gdx;
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

public class HomeScreen implements Screen , Serializable {
    private Music backgroundMusic;
    private final Main game;
    private Texture backgroundTexture;
    private Texture settingsTexture;
    private Texture playTexture;
    private Texture backButtonTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite settingsSprite;
    private Sprite backButtonSprite;
    private Sprite playSprite;
    private Vector3 touchPos;
    private Sound buttonClickSound;
    public HomeScreen(Main game) {
        this.game = game;
        this.spriteBatch = new SpriteBatch();
        this.viewport = new FitViewport(800, 480);
        this.backgroundTexture = new Texture("menu_background.jpg");
        this.settingsTexture = new Texture("Settings.png");
        this.playTexture = new Texture("Play.png");
        this.backButtonTexture = new Texture("BackButton.png");
        this.settingsSprite = new Sprite(settingsTexture);
        this.playSprite = new Sprite(playTexture);
        this.backButtonSprite = new Sprite(backButtonTexture);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        float spriteWidth = viewport.getWorldWidth() * 0.1f;  // Reduced size for play button
        float spriteHeight = viewport.getWorldHeight() * 0.1f;
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        playSprite.setSize(spriteWidth, spriteHeight);

        // Smaller size for the settings button
        float settingsSpriteWidth = viewport.getWorldWidth() * 0.1f;  // 10% of screen width
        float settingsSpriteHeight = viewport.getWorldHeight() * 0.1f; // 10% of screen height
        settingsSprite.setSize(settingsSpriteWidth, settingsSpriteHeight);

        // Smaller size for the back button
        float backButtonWidth = viewport.getWorldWidth() * 0.1f;  // 10% of screen width
        float backButtonHeight = viewport.getWorldHeight() * 0.1f; // 10% of screen height
        backButtonSprite.setSize(backButtonWidth, backButtonHeight);

        float totalWidth = spriteWidth + viewport.getWorldWidth() * 0.1f;
        float startX = (viewport.getWorldWidth()-totalWidth)/2+40;
        float spriteY = viewport.getWorldHeight()*0.4f+40;
        playSprite.setPosition(startX, spriteY);

        float settingsX = viewport.getWorldWidth() - settingsSpriteWidth - 20; // 20px margin from the right
        float settingsY = viewport.getWorldHeight() - settingsSpriteHeight - 20; // 20px margin from the top
        settingsSprite.setPosition(settingsX, settingsY);

        float backX = 20;  // 20px margin from the left
        float backY = viewport.getWorldHeight() - backButtonHeight - 20; // 20px margin from the top
        backButtonSprite.setPosition(backX, backY);

        this.touchPos = new Vector3();
    }

    @Override
    public void show() {
        if(game.music%2==0){
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
        System.out.println("Home Screen is now active.");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        playSprite.draw(spriteBatch);
        settingsSprite.draw(spriteBatch);
        backButtonSprite.draw(spriteBatch);
        spriteBatch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);
            if (settingsSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Settings button touched");
                buttonClickSound.play();
                game.goToSettings(this);
            } else if (playSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Play button touched");
                buttonClickSound.play();
                game.setScreen(game.PlayScreen);
            } else if (backButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Back Button touched");
                buttonClickSound.play();
                game.setScreen(game.StartScreen);
            }
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
        backgroundMusic.stop();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        settingsTexture.dispose();
        playTexture.dispose();
        backButtonTexture.dispose();
        backgroundMusic.dispose();

    }
}
