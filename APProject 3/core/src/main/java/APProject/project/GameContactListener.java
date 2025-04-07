package APProject.project;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.*;
public class GameContactListener implements ContactListener {
    private Level1 level;
    private static final float MIN_IMPACT_VELOCITY = 2.0f;
    public List<Body> bodyRemovalQueue;
    private transient Sound destroySound;
    public GameContactListener(Level1 level) {
        this.level = level;
        destroySound = Gdx.audio.newSound(Gdx.files.internal("destroy.mp3"));
        this.bodyRemovalQueue = new ArrayList<>();
    }
    @Override
    public void beginContact(Contact contact) {
    }
    @Override
    public void endContact(Contact contact) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        float maxImpulse = 0;
        float[] impulses = impulse.getNormalImpulses();
        for (float imp : impulses) {
            maxImpulse = Math.max(maxImpulse, imp);
        }
        if (userDataA instanceof Pig) {
            System.out.println("Pig");
            Pig pig = (Pig) userDataA;
            float damage = maxImpulse * 8.5f;
            pig.health -= damage;
            System.out.println("health Remaining of Pig:" + pig.health);
            if(pig.health <= 0) {
                pig.isdead = true;
            }
        } else if (userDataB instanceof Pig) {
            System.out.println("Pig");
            Pig pig = (Pig) userDataB;
            float damage = maxImpulse * 8.5f;
            pig.health -= damage;
            System.out.println("health Remaining of Pig:" + pig.health);
            if(pig.health <= 0) {

                pig.isdead = true;

            }

        }
        if (maxImpulse > MIN_IMPACT_VELOCITY) {
            if (userDataA instanceof RedBird && userDataB instanceof Block) {
                System.out.println("RedBird");
                RedBird bird = (RedBird) userDataA;
                Block block = (Block) userDataB;
                if (level.blocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyB);
                        destroySound.play();
                        level.score+=100;
                    }
                }
            }
            else if (userDataA instanceof Block && userDataB instanceof RedBird) {
                RedBird bird = (RedBird) userDataB;
                Block block = (Block) userDataA;
                if (level.blocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyA);
                        destroySound.play();
                    }
                }
            }
        }
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    public void processBodyRemovalQueue() {
        System.out.println(bodyRemovalQueue.size());
        for (Body body : bodyRemovalQueue) {
            Block block = (Block) body.getUserData();
            if (body != null && body.getType() == BodyDef.BodyType.DynamicBody && block.hitcount > 0) {
                if (body.getUserData() instanceof Block) {
                    level.blocks.remove(block);
                    level.world.destroyBody(body);
                    System.out.println("Block removed!");
                }
            }
        }
        bodyRemovalQueue.clear();
    }
}
