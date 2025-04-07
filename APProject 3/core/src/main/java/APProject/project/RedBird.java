
package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;


import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class RedBird extends Bird implements Serializable {
    private Body body;
    public boolean isDragging = false;
    public boolean isAtSlingshot = false;
    private Vector2 initialPosition;
    private transient Sound flyingSound;
    private transient Sound shotSound;
    public Vector2 slingshotPosition;
    private Sprite sprite;
    private static final float PIXELS_TO_METERS = 100f;
    private Texture birdTexture;
    public boolean isshot = false;

    public RedBird(World world, float x, float y, float scaleX, float scaleY) {
        super(x, y, "redbird.png", scaleX, scaleY);


        flyingSound = Gdx.audio.newSound(Gdx.files.internal("flying.mp3"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.mp3"));
        // Store initial position in pixels
        initialPosition = new Vector2(x, y);

        // Initialize slingshot position
        slingshotPosition = new Vector2();

        // Load texture and create sprite
        birdTexture = new Texture("redbird.png");
        sprite = new Sprite(birdTexture);

        // Set sprite size (assuming the texture is 32x32 pixels - adjust if different)
        float width = 32 * scaleX;
        float height = 32 * scaleY;
        sprite.setSize(width, height);

        // Center the sprite
        sprite.setOriginCenter();
        sprite.setPosition(x - width / 2, y - height / 2);

        // Box2D Body setup
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Static at first
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        // Define a box shape based on the sprite's width and height
        shape.setAsBox((sprite.getWidth() / 2) / PIXELS_TO_METERS, (sprite.getHeight() / 2) / PIXELS_TO_METERS);// Use sprite width for collision circle

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Initial position update
        updateSpritePosition();
        body.setUserData(this);
    }

    private void updateSpritePosition() {
        Vector2 position = body.getPosition();
        sprite.setPosition(
            position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2
        );
    }

    public void moveToSlingshot(float x, float y) {
        isAtSlingshot = true; // Mark the bird as being at the slingshot
        slingshotPosition.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS); // Store slingshot position
        body.setTransform(slingshotPosition, 0); // Move body to slingshot
        updateSpritePosition();
    }

    public void activateForDragging() {
        body.setType(BodyDef.BodyType.DynamicBody); // Change to dynamic
        isDragging = true;
    }

    public void updateDragging(float dragX, float dragY) {
        if (isDragging) {
            body.setTransform(dragX / PIXELS_TO_METERS, dragY / PIXELS_TO_METERS, 0);
            updateSpritePosition();
        }
    }

    public void shoot() {
        if (isDragging) {
            isDragging = false;
            isshot =true;
            Vector2 currentPos = body.getPosition();
            Vector2 launchDir = new Vector2(
                slingshotPosition.x - currentPos.x,
                slingshotPosition.y - currentPos.y
            );
            flyingSound.play();
            shotSound.play();
            float force = 8.0f; // Adjust force as needed
            body.setLinearVelocity(launchDir.scl(force)); // Apply scaled force
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isDragging) {
            updateSpritePosition();
        }
        sprite.draw(batch);
    }

    @Override
    public void reset() {
        body.setType(BodyDef.BodyType.StaticBody);
        body.setTransform(initialPosition.x / PIXELS_TO_METERS, initialPosition.y / PIXELS_TO_METERS, 0);
        body.setLinearVelocity(0, 0);
        isDragging = false;
        isAtSlingshot = false;
        updateSpritePosition();
    }

    public void dispose() {
        birdTexture.dispose();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }
}


