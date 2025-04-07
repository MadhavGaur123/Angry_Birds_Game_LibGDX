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

public class SettingsClass implements Screen {
    private final Main game;
    private final Screen previousScreen; // Store the previous screen
    private Texture backgroundTexture;
    private Texture HowtoPlayTexture;
    private Texture PauseGameTexture;
    private Texture BackTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite HowtoPlaySprite;
    private Sprite PauseGameSprite;
    private Sprite BackSprite;
    private Vector3 touchPos;
    private Music backgroundMusic;
    private Sound buttonClickSound;
    // Modify the constructor to accept the previous screen
    public SettingsClass(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen; // Store the previous screen reference
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 480);
        backgroundTexture = new Texture("pause_screen.png");
        HowtoPlayTexture = new Texture("question_mark.png");
        PauseGameTexture= new Texture("music.png");
        BackTexture = new Texture("BackButton.png");
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        HowtoPlaySprite = new Sprite(HowtoPlayTexture);
        PauseGameSprite = new Sprite(PauseGameTexture);
        BackSprite = new Sprite(BackTexture);

        float spriteWidth = viewport.getWorldWidth() * 0.15f;
        float spriteHeight = viewport.getWorldHeight() * 0.15f;
        HowtoPlaySprite.setSize(spriteWidth, spriteHeight);
        PauseGameSprite.setSize(spriteWidth, spriteHeight);
        BackSprite.setSize(spriteWidth, spriteHeight);

        // Calculate positions to align sprites
        float totalWidth = spriteWidth * 3 + viewport.getWorldWidth() * 0.2f; // 3 sprites + 20% of screen width for spacing
        float startX = (viewport.getWorldWidth() - totalWidth) / 2;
        float spriteY = viewport.getWorldHeight() * 0.5f; // Positioned at the middle of the screen
        float spacing = viewport.getWorldWidth() * 0.1f;  // 10% of screen width for spacing

        HowtoPlaySprite.setPosition(startX, spriteY);
        PauseGameSprite.setPosition(startX + spriteWidth + spacing, spriteY);
        BackSprite.setPosition(startX + (spriteWidth + spacing) * 2, spriteY);

        touchPos = new Vector3();
    }

    @Override
    public void show() {
        if(game.music%2==0){
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
        System.out.println("Settings Screen is now active.");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        HowtoPlaySprite.draw(spriteBatch);
        BackSprite.draw(spriteBatch);
        PauseGameSprite.draw(spriteBatch);
        spriteBatch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (HowtoPlaySprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("How To Play Selected");
                // Add How To Play logic here;
                buttonClickSound.play();
            } else if (BackSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Back Button Selected");
                game.setScreen(previousScreen); // Go back to the previous screen
                buttonClickSound.play();
            } else if (PauseGameSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Pause Game Button Selected");
                buttonClickSound.play();
                game.music+=1;
                backgroundMusic.stop();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(previousScreen); // Go back to the previous screen on ESC
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
        BackTexture.dispose();
        HowtoPlayTexture.dispose();
        PauseGameTexture.dispose();
    }
}
