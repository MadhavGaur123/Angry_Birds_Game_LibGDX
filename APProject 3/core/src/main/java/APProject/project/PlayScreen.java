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

public class PlayScreen implements Screen, Serializable {
    private final Main game;
    private Texture backgroundTexture;
    private Texture level1Texture;
    private Texture level2Texture;
    private Texture level3Texture;
    private Texture level2winTexture;
    private Texture level3winTexture;
    private Texture backButtonTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Sprite level1Sprite;
    private Sprite level2Sprite;
    private Sprite level3Sprite;
    private Sprite level3winSprite;
    private Sprite level2winSprite;
    private Sprite backButtonSprite;
    private Vector3 touchPos;
    private Music backgroundMusic;
    private Sound buttonClickSound;
    private int levels;
    public PlayScreen(Main game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 480);
        backgroundTexture = new Texture("level_background.png");
        level1Texture = new Texture("level1.png");
        level2Texture = new Texture("level2.png");
        level3Texture = new Texture("level2.png");
        level2winTexture = new Texture("level2win.png");
        level3winTexture = new Texture("level3.png");
        backButtonTexture = new Texture("BackButton.png");
        level1Sprite = new Sprite(level1Texture);
        level2Sprite = new Sprite(level2Texture);
        level3Sprite = new Sprite(level3Texture);
        level2winSprite = new Sprite(level2winTexture);
        level3winSprite = new Sprite(level3winTexture);
        backButtonSprite = new Sprite(backButtonTexture);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("level_background.mp3"));
        float levelSpriteWidth = viewport.getWorldWidth() * 0.15f;
        float levelSpriteHeight = viewport.getWorldHeight() * 0.15f;
        level1Sprite.setSize(levelSpriteWidth, levelSpriteHeight);
        level2Sprite.setSize(levelSpriteWidth, levelSpriteHeight);
        level3Sprite.setSize(levelSpriteWidth, levelSpriteHeight);
        level2winSprite.setSize(levelSpriteWidth, levelSpriteHeight);
        level3winSprite.setSize(levelSpriteWidth, levelSpriteHeight);
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("confirm.mp3"));
        float backButtonWidth = viewport.getWorldWidth() * 0.1f;
        float backButtonHeight = viewport.getWorldHeight() * 0.1f;
        backButtonSprite.setSize(backButtonWidth, backButtonHeight);
        levels=1;
        float totalWidth = levelSpriteWidth * 3 + viewport.getWorldWidth() * 0.2f;
        float startX = (viewport.getWorldWidth() - totalWidth) / 2;
        float spriteY = viewport.getWorldHeight() * 0.5f;
        float spacing = viewport.getWorldWidth() * 0.1f;

        level1Sprite.setPosition(startX, spriteY);
        level2Sprite.setPosition(startX + levelSpriteWidth + spacing, spriteY);
        level3Sprite.setPosition(startX + (levelSpriteWidth + spacing) * 2, spriteY);
        level2winSprite.setPosition(startX + levelSpriteWidth + spacing, spriteY);
        level3winSprite.setPosition(startX + (levelSpriteWidth + spacing) * 2, spriteY);

        // Position BackButton in the top right corner
        float margin = viewport.getWorldWidth() * 0.02f; // 2% margin from the edges
        backButtonSprite.setPosition(viewport.getWorldWidth() - backButtonWidth - margin,
            viewport.getWorldHeight() - backButtonHeight - margin);

        touchPos = new Vector3();
    }
    @Override
    public void show() {
        if(game.music%2==0){
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        level1Sprite.draw(spriteBatch);
        if(game.wins<1){
            level2Sprite.draw(spriteBatch);
        }
        else{

            level2winSprite.draw(spriteBatch);
        }
        if(game.wins<2){
            level3Sprite.draw(spriteBatch);

        }
        else{
            level3winSprite.draw(spriteBatch);
        }
        backButtonSprite.draw(spriteBatch);
        spriteBatch.end();
        handleInput();
    }
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (level1Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Level 1 selected");
                buttonClickSound.play();
                game.setScreen(new Level1(game));
            }
            else if (game.wins>=1 && level2Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Level 2 selected");
                game.setScreen(new Level2(game));
            } else if (game.wins>=2 && level3Sprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Level 3 selected");
                game.setScreen(new Level3(game));

            }
            else if (backButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                System.out.println("Back Button Selected");
                buttonClickSound.play();
                game.setScreen(game.HomeScreen);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.HomeScreen);
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
        level1Texture.dispose();
        level2Texture.dispose();
        level3Texture.dispose();
        backButtonTexture.dispose();
        backgroundMusic.dispose();
    }
}
