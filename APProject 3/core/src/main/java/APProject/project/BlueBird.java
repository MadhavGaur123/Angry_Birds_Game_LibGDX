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
public class BlueBird extends Bird implements Serializable {
    private boolean hasSplit = false;
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
    public Level2 level;
    World world;
    public BlueBird(World world, float x, float y, float scaleX, float scaleY,Level2 level) {
        super(x, y, "blue_bird.png", scaleX, scaleY);
        initialPosition = new Vector2(x, y);

        flyingSound = Gdx.audio.newSound(Gdx.files.internal("flying.mp3"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.mp3"));
        slingshotPosition = new Vector2();
        birdTexture = new Texture("blue_bird.png");
        sprite = new Sprite(birdTexture);
        float width = 32 * scaleX;
        float height = 32 * scaleY;
        sprite.setSize(width, height);
        this.level=level;
        sprite.setOriginCenter();
        sprite.setPosition(x - width / 2, y - height / 2);
        this.world=world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth() / 2) / PIXELS_TO_METERS, (sprite.getHeight() / 2) / PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef);
        shape.dispose();
        updateSpritePosition();
        body.setUserData(this);
    }
    private void updateSpritePosition() {
        Vector2 position = body.getPosition();
        sprite.setPosition(
            position.x * PIXELS_TO_METERS - sprite.getWidth() / 2, position.y * PIXELS_TO_METERS - sprite.getHeight() / 2
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
    public void split() {
        if (hasSplit) return;
        hasSplit = true;
        Vector2 currentPosition = body.getPosition();
        Vector2 currentVelocity = body.getLinearVelocity();
        float verticalOffset = 0.5f;
        BlueBird birdAbove = new BlueBird(world, currentPosition.x * PIXELS_TO_METERS, (currentPosition.y + verticalOffset) * PIXELS_TO_METERS, 1.3f, 1.3f,level);
        BlueBird birdBelow = new BlueBird(world,currentPosition.x * PIXELS_TO_METERS, (currentPosition.y - verticalOffset) * PIXELS_TO_METERS, 1.3f, 1.3f,level);
        BlueBird active = new BlueBird(world, currentPosition.x * PIXELS_TO_METERS, (currentPosition.y ) * PIXELS_TO_METERS, 1.3f, 1.3f,level);
        birdAbove.getBody().setType(BodyDef.BodyType.DynamicBody);
        birdBelow.getBody().setType(BodyDef.BodyType.DynamicBody);
        active.getBody().setType(BodyDef.BodyType.DynamicBody);
        birdAbove.getBody().setLinearVelocity(currentVelocity);
        birdBelow.getBody().setLinearVelocity(currentVelocity);
        active.getBody().setLinearVelocity(currentVelocity);
        level.blueBirds.add(birdAbove);
        level.blueBirds.add(birdBelow);
        level.blueBirds.add(active);
        level.blueBirds.remove(this);
        this.body.setActive(false);
    }
    public void shoot() {
        if (isDragging) {
            isDragging = false;
            isshot =true;
            Vector2 currentPos = body.getPosition();
            Vector2 launchDir = new Vector2(
                slingshotPosition.x - currentPos.x, slingshotPosition.y - currentPos.y
            );
            flyingSound.play();
            shotSound.play();
            float force = 10.0f;
            body.setLinearVelocity(launchDir.scl(force));
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
    public void dispose(){
        birdTexture.dispose();
    }
    public Sprite getSprite(){
        return sprite;
    }
    public Body getBody(){
        return body;
    }

}


