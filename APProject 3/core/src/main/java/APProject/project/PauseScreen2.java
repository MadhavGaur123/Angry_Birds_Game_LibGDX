package APProject.project;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
public class PauseScreen2 implements Screen {
    private final Main game;
    private Texture backgroundTexture;
    private Texture ResumeTexture;
    private Texture ExitTexture;
    private Texture RestartTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite ResumeSprite;
    private Sprite ExitSprite;
    private Sprite RestartSprite;
    private Vector3 touchPos;
    private Sound buttonClickSound;
    public PauseScreen2(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 480);
        backgroundTexture = new Texture("pause_screen.png");
        ResumeTexture = new Texture("BackButton.png");
        ExitTexture = new Texture("Exit.png");
        RestartTexture = new Texture("restart.png");
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        ResumeSprite = new Sprite(ResumeTexture);
        float spriteWidth = viewport.getWorldWidth() * 0.2f;
        float spriteHeight = viewport.getWorldHeight() * 0.2f;
        ResumeSprite.setSize(spriteWidth, spriteHeight);
        ResumeSprite.setPosition(viewport.getWorldWidth() * 0.4f, viewport.getWorldHeight() * 0.5f);
        ExitSprite = new Sprite(ExitTexture);
        ExitSprite.setSize(spriteWidth, spriteHeight);
        ExitSprite.setPosition(viewport.getWorldWidth() * 0.4f, viewport.getWorldHeight() * 0.3f);
        RestartSprite = new Sprite(RestartTexture);
        RestartSprite.setSize(spriteWidth, spriteHeight);
        RestartSprite.setPosition(viewport.getWorldWidth() * 0.4f, viewport.getWorldHeight() * 0.1f);
        touchPos = new Vector3();
    }
    @Override
    public void show() {
        System.out.println("Pause Screen is now active.");
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        ResumeSprite.draw(spriteBatch);
        ExitSprite.draw(spriteBatch);
        RestartSprite.draw(spriteBatch);
        spriteBatch.end();
        handleInput();
    }
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);
            if (ResumeSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Resume button touched! Switching to Level1...");
                buttonClickSound.play();
                game.setScreen(game.Level2);
            }
            if (ExitSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Exit button touched! Returning to HomeScreen...");
                buttonClickSound.play();
                game.setScreen(game.HomeScreen);
            }
            if (RestartSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Restart button touched! Restarting Level1...");
                buttonClickSound.play();
                game.setScreen(new Level2(game));
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
        ResumeTexture.dispose();
        ExitTexture.dispose();
        RestartTexture.dispose();
    }
}
