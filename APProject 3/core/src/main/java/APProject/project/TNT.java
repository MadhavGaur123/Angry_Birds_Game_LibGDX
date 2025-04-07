package APProject.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

public class TNT {
    private Texture texture;
    private Sprite sprite;
    private Body body;
    private float scaleX, scaleY;
    private static final float PIXELS_TO_METERS = 100f;
    public boolean isshot;

    // Fixed explosion properties
    private static final float BLAST_RADIUS = 3f; // Fixed blast radius
    private static final float BLAST_POWER = 100f; // Fixed blast power

    public TNT(World world, float x, float y, String texturePath, float scaleX, float scaleY, float density) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.texture = new Texture(Gdx.files.internal(texturePath));

        // Create Sprite for the TNT
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scaleX, texture.getHeight() * scaleY);
        sprite.setOriginCenter();
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);

        // Box2D Body setup
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS); // Convert to meters
        body = world.createBody(bodyDef);

        // PolygonShape for TNT (rectangle shape)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / PIXELS_TO_METERS, sprite.getHeight() / 2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        updateSpritePosition();
        body.setUserData(this);
    }

    private void updateSpritePosition() {
        Vector2 position = body.getPosition();
        sprite.setPosition(position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2);
    }

    public void render(SpriteBatch batch) {
        updateSpritePosition(); // Ensure the sprite position is up to date
        sprite.draw(batch);
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    private void destroy() {
        body.getWorld().destroyBody(body); // Remove from the physics world
        dispose(); // Dispose of the texture
    }

    public Body getBody() {

        return body;
    }

//    public void triggerExplosion(World world) {
//        Vector2 explosionCenter = body.getPosition();
//
//        // Query the area for affected objects
//        world.QueryAABB(fixture -> {
//                Body targetBody = fixture.getBody();
//
//                // Skip static bodies
//                if (targetBody.getType() == BodyDef.BodyType.StaticBody) {
//                    return true;
//                }
//
//                // Apply a blast impulse
//                Vector2 direction = targetBody.getWorldCenter().cpy().sub(explosionCenter);
//                float distance = direction.len();
//
//                if (distance > 0 && distance <= BLAST_RADIUS) {
//                    direction.nor(); // Normalize direction
//                    float impulseStrength = BLAST_POWER / distance;
//                    targetBody.applyLinearImpulse(direction.scl(impulseStrength), targetBody.getWorldCenter(), true);
//                }
//                return true;
//            },
//            explosionCenter.x - BLAST_RADIUS, explosionCenter.y - BLAST_RADIUS,
//            explosionCenter.x + BLAST_RADIUS, explosionCenter.y + BLAST_RADIUS);
//
//        // Destroy the TNT after explosion
//        destroy();
//    }
public void triggerExplosion(World world) {
    // Remove TNT body
    if (this.body != null) {
        world.destroyBody(this.body);
        this.body = null;
    }

    // Add explosion effect logic
    System.out.println("Explosion triggered!");
    // You can spawn particles, play sounds, or deal damage here
}
    public void makeDynamic() {
        body.setType(BodyDef.BodyType.DynamicBody);

    }
}
