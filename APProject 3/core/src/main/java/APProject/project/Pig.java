package APProject.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;
public class Pig implements Serializable{
    private Body body;
    private Sprite sprite;
    private Texture pigTexture;
    private static final float PIXELS_TO_METERS = 100f;  // Conversion factor from pixels to meters
    private float initialX, initialY;
    public float health;
    public boolean isdead;
    public String texturepath;


    public Pig(World world, float x, float y, String texturePath, float scaleX, float scaleY) {
        texturepath = texturePath;
        initialX = x;
        initialY = y;
        health = 250.0f;
        isdead = false;
        // Load texture and create sprite
        pigTexture = new Texture(texturePath);
        sprite = new Sprite(pigTexture);

        // Set sprite size (scaled based on the passed scaleX and scaleY)
        sprite.setSize(pigTexture.getWidth() * scaleX, pigTexture.getHeight() * scaleY);
        sprite.setOriginCenter();  // Set the origin to the center of the sprite
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);

        // Box2D Body setup
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;  // Dynamic body since the pig will move
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);

        // Create a rectangular collision shape for the pig
        PolygonShape shape = new PolygonShape();
        // Define a box shape based on the sprite's width and height
        shape.setAsBox((sprite.getWidth() / 2) / PIXELS_TO_METERS, (sprite.getHeight() / 2) / PIXELS_TO_METERS);

        // Create fixture for the pig
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;  // You can adjust density if needed
        fixtureDef.friction = 0.6f;  // Adjust friction as needed
        fixtureDef.restitution = 0.1f;  // Adjust restitution (bounciness) as needed
        body.createFixture(fixtureDef);

        shape.dispose();  // Dispose of the shape after creating the fixture
        body.setUserData(this);
        // Set initial position for the sprite based on the body position
        updateSpritePosition();
    }

    private void updateSpritePosition() {
        // Update the sprite position based on the Box2D body position
        Vector2 position = body.getPosition();
        sprite.setPosition(
            position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2
        );
    }

    public void render(SpriteBatch batch) {
        // Update sprite position based on the body position, then draw the sprite
        updateSpritePosition();
        if (body.getLinearVelocity().y == 0) { // Bird is on the ground
            body.setLinearVelocity(0, body.getLinearVelocity().y); // Stop horizontal movement
        }
        sprite.draw(batch);
    }

    public void reset() {
        // Reset the pig back to its initial position
        body.setTransform(initialX / PIXELS_TO_METERS, initialY / PIXELS_TO_METERS, 0);
        body.setLinearVelocity(0, 0);
        updateSpritePosition();
    }

    public void dispose() {
        // Dispose of the pig texture
        pigTexture.dispose();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }
}
