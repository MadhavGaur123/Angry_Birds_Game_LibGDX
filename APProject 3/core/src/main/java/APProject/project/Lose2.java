package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.audio.Sound;

public class Lose2 implements Screen {
    private final Main game;
    private Texture backgroundTexture, dialogTexture, ExitTexture, RestartTexture, LevelsTexture;
    private Sprite dialogSprite, ExitSprite, RestartSprite, LevelsSprite;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Vector3 touchPos;
    private Sound buttonClickSound;
    private Music loseMusic;
    public Lose2(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 480);

        backgroundTexture = new Texture("pause_screen.png");
        dialogTexture = new Texture("lose.PNG");
        ExitTexture = new Texture("Exit.png");
        RestartTexture = new Texture("restart.png");
        LevelsTexture = new Texture("Play.png");
        loseMusic = Gdx.audio.newMusic(Gdx.files.internal("lose.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));

        // Make dialog more square and smaller
        dialogSprite = new Sprite(dialogTexture);
        dialogSprite.setSize(viewport.getWorldWidth() * 0.4f, viewport.getWorldHeight() * 0.5f);
        dialogSprite.setPosition(viewport.getWorldWidth() * 0.3f, viewport.getWorldHeight() * 0.5f);

        // Define smaller buttons and place them in a row below the dialog
        float buttonWidth = viewport.getWorldWidth() * 0.15f;
        float buttonHeight = viewport.getWorldHeight() * 0.1f;
        float buttonY = viewport.getWorldHeight() * 0.3f;

        RestartSprite = new Sprite(RestartTexture);
        RestartSprite.setSize(buttonWidth, buttonHeight);
        RestartSprite.setPosition(viewport.getWorldWidth() * 0.2f, buttonY);

        LevelsSprite = new Sprite(LevelsTexture);
        LevelsSprite.setSize(buttonWidth, buttonHeight);
        LevelsSprite.setPosition(viewport.getWorldWidth() * 0.4f, buttonY);

        ExitSprite = new Sprite(ExitTexture);
        ExitSprite.setSize(buttonWidth, buttonHeight);
        ExitSprite.setPosition(viewport.getWorldWidth() * 0.6f, buttonY);

        touchPos = new Vector3();
    }

    @Override
    public void show() {
        System.out.println("Lose Screen is now active.");
        loseMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        dialogSprite.draw(spriteBatch); // Draw dialog box
        RestartSprite.draw(spriteBatch); // Draw Restart button
        LevelsSprite.draw(spriteBatch);  // Draw Levels button
        ExitSprite.draw(spriteBatch);    // Draw Exit button
        spriteBatch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (RestartSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Restart button touched! Restarting Level1...");
                buttonClickSound.play();
                game.setScreen(new Level2(game));
            } else if (LevelsSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Levels button touched! Returning to HomeScreen...");
                buttonClickSound.play();
                game.setScreen(game.PlayScreen); // Use your Levels screen here
            } else if (ExitSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Exit button touched! Returning to HomeScreen...");
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
    public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        dialogTexture.dispose();
        ExitTexture.dispose();
        RestartTexture.dispose();
        LevelsTexture.dispose();
        buttonClickSound.dispose();
    }
}

