package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import java.io.Serializable;

public class Block implements Serializable {
    public boolean isShot = false;
    public String texturePath;
    public Texture texture;
    public Sprite sprite;
    private Body body;
    private float scaleX, scaleY;
    private static final float PIXELS_TO_METERS = 100f;
    private boolean xxxx;
    public int hitcount;
    public float damageThreshold = 0f; // Accumulated damage
    public float maxDamage = 0f;

    public Block(World world, float x, float y, String texturePath, float scaleX, float scaleY, boolean xxxx,float density) {
        this.texturePath = texturePath;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.xxxx = xxxx;
        hitcount = 0;

        // Create Sprite for the block
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scaleX, texture.getHeight() * scaleY);
        sprite.setOriginCenter();
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);

        // Box2D Body setup
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS); // Convert to meters
        body = world.createBody(bodyDef);

        // PolygonShape for block (rectangle shape)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / PIXELS_TO_METERS, sprite.getHeight() / 2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.3f; // Bounciness

        // Create the fixture for Box2D body
        body.createFixture(fixtureDef);
        shape.dispose();

        // Set user data to associate body with this block object
        body.setUserData(this);

        updateSpritePosition();
    }
    public boolean isDynamic() {
        return body.getType() == BodyDef.BodyType.DynamicBody;
    }

    // Method to update sprite position based on body position
    private void updateSpritePosition() {
        Vector2 position = body.getPosition();
        sprite.setPosition(position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees); // Apply rotation based on body angle
    }

    // Render method to draw the block
    public void render(SpriteBatch batch) {
        updateSpritePosition(); // Ensure the sprite position is up to date
        sprite.draw(batch);
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
    public Body getBody() {
        return body;
    }
    public String getTexturePath() {
        return texturePath;
    }

    public void makeDynamic() {
        body.setType(BodyDef.BodyType.DynamicBody);
    }
}

