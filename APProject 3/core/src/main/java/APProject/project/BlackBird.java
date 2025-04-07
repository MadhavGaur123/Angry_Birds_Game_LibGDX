package APProject.project;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;
public class BlackBird extends Bird implements Serializable {
    private Body body;
    public boolean isDragging = false;
    public boolean isAtSlingshot = false;
    private Vector2 initialPosition;
    public Vector2 slingshotPosition;
    private transient Sound flyingSound;
    private transient Sound shotSound;
    private Sprite sprite;
    private static final float PIXELS_TO_METERS = 100f;
    private Texture birdTexture;
    public boolean isshot = false;
    private static final float EXPLOSION_RADIUS=1.0f;
    public Level3 level;
    private static final float EXPLOSION_FORCE = 20.0f;
    World world;

    public BlackBird(World world, float x, float y, float scaleX, float scaleY,Level3 level) {
        super(x, y,"blackbird.png", scaleX, scaleY);
        initialPosition = new Vector2(x, y);

        // Initialize slingshot position

        flyingSound = Gdx.audio.newSound(Gdx.files.internal("flying.mp3"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.mp3"));
        slingshotPosition = new Vector2();
        this.level=level;
        // Load texture and create sprite
        birdTexture = new Texture("blackbird.png");
        sprite = new Sprite(birdTexture);

        // Set sprite size (assuming the texture is 32x32 pixels - adjust if different)
        float width = 32 * scaleX;
        float height = 32 * scaleY;
        sprite.setSize(width, height);

        // Center the sprite
        sprite.setOriginCenter();
        sprite.setPosition(x - width / 2, y - height / 2);
        this.world=world;
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
public void triggerExplosion(Vector2 explosionCenter) {
    // Print the explosion center for debugging purposes
    System.out.println("Explosion center: " + explosionCenter);

    // Define the AABB query area based on explosion radius
    float lowerX = explosionCenter.x - EXPLOSION_RADIUS;
    float lowerY = explosionCenter.y - EXPLOSION_RADIUS;
    float upperX = explosionCenter.x + EXPLOSION_RADIUS;
    float upperY = explosionCenter.y + EXPLOSION_RADIUS;

    // Query the world for all bodies within the AABB area
    level.world.QueryAABB(fixture -> {
        Body body = fixture.getBody();
        if (body != null && body.getType() == BodyDef.BodyType.DynamicBody) {
            // Print the body being checked for debugging
            System.out.println("Checking body: " + body.getUserData());

            // Calculate the direction and distance from the explosion center to the body
            Vector2 bodyPosition = body.getPosition();
            Vector2 explosionDir = bodyPosition.cpy().sub(explosionCenter);
            float distance = explosionDir.len();

            // If the body is within the explosion radius
            if (distance < EXPLOSION_RADIUS && distance > 0) {
                // Normalize the direction vector
                explosionDir.nor();

                // Calculate the force based on the inverse square of the distance
                float force = EXPLOSION_FORCE / (distance * distance);
                Vector2 explosionForce = explosionDir.scl(force);

                // Apply the explosion force to the body
                body.applyLinearImpulse(explosionForce, body.getWorldCenter(), true);

                // Debugging output for the applied force
                System.out.println("Applied explosion force: " + explosionForce + " to body: " + body.getUserData());
            }
        }
        return true; // Continue querying other bodies
    }, lowerX, lowerY, upperX, upperY); // Define the query box using AABB
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
    public Vector2 getPosition() {
        return body.getPosition().cpy().scl(PIXELS_TO_METERS); // Convert from Box2D units to pixels
    }
    public Body getBody() {
        return body;
    }
}



