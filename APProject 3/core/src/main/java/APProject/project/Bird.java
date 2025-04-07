package APProject.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import java.io.Serializable;
public class Bird implements Serializable {
    protected Sprite sprite;

    public Bird(float x, float y, String texturePath, float scaleX, float scaleY) {
        sprite = new Sprite(new Texture(texturePath));
        sprite.setScale(scaleX, scaleY);
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void reset() {
        // Can be overridden in subclasses
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
