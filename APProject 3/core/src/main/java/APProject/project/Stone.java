package APProject.project;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Stone extends Block implements Serializable {
    private static final float PIXELS_TO_METERS = 100f;
    private Body body;

    public Stone(World world, float x, float y, String texturePath, float scaleX, float scaleY, boolean xxxx, float density) {
        super(world, x, y, texturePath, scaleX, scaleY, xxxx, density);

        // Recreate the Box2D body with a circular shape
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS); // Convert to meters
        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((sprite.getWidth() / 2) / PIXELS_TO_METERS); // Use sprite width for radius

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = density;
        fixtureDef.friction = 0.1f; // Adjust friction for rolling behavior
        fixtureDef.restitution = 0.3f; // Adjust bounciness

        body.createFixture(fixtureDef);
        circleShape.dispose();

        body.setUserData(this); // Associate this Stone object with its body
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();

        // Update sprite position and rotation based on Box2D body
        sprite.setPosition(position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        sprite.draw(batch);
    }

    @Override
    public Body getBody() {
        return body;
    }
}
