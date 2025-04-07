package APProject.project;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
public class GameContactListener3 implements ContactListener {
    private Level3 level;
    private static final float MIN_IMPACT_VELOCITY = 2.1f;
    private static final float MIN_IMPACT_VELOCITY2 = 0.1f;
    private static final float EXPLOSION_RADIUS = 5.0f;
    private static final float EXPLOSION_RADIUS2 = 1.0f;// Radius for the explosion effect
    private static final float EXPLOSION_FORCE = 8.0f;
    private static final float EXPLOSION_FORCE2 = 1f; // Force applied during explosion
    private transient Sound destroySound;
    private transient Sound tntSound;
    private transient Sound iceSound;
    private List<Body> bodyRemovalQueue;
    public GameContactListener3(Level3 level) {
        this.level = level;
        destroySound = Gdx.audio.newSound(Gdx.files.internal("destroy.mp3"));
        tntSound = Gdx.audio.newSound(Gdx.files.internal("tnt.mp3"));
        iceSound = Gdx.audio.newSound(Gdx.files.internal("ice.mp3"));
        this.bodyRemovalQueue = new ArrayList<>();
    }
    @Override
    public void beginContact(Contact contact) {
    }
    @Override
    public void endContact(Contact contact) {
        // Not needed for this implementation
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
        if (userDataA instanceof Pig && userDataB instanceof Block) {
            System.out.println("Pig");
            Pig pig = (Pig) userDataA;
            float damage = maxImpulse * 9.0f;
            pig.health -= damage;
            System.out.println("health Remaining of Pig:" + pig.health);
            if(pig.health <= 0) {
                pig.isdead = true;
            }
        } else if (userDataB instanceof Pig && userDataA instanceof Block) {
            System.out.println("Pig");
            Pig pig = (Pig) userDataB;
            float damage = maxImpulse * 9.0f;
            pig.health -= damage;
            System.out.println("health Remaining of Pig:" + pig.health);
            if(pig.health <= 0) {
                pig.isdead = true;
            }

        }
        if ((userDataA instanceof TNT && (userDataB instanceof RedBird || userDataB instanceof YellowBird ||userDataB instanceof Stone || userDataB instanceof BlackBird)) ||
            (userDataB instanceof TNT && (userDataA instanceof RedBird || userDataA instanceof YellowBird ||userDataA instanceof Stone || userDataA instanceof BlackBird))) {
            TNT tnt = (userDataA instanceof TNT) ? (TNT) userDataA : (TNT) userDataB;
            Body tntBody = (userDataA instanceof TNT) ? bodyA : bodyB;
            System.out.println("Explosion Triggered");
            tnt.isshot = true;
            tntSound.play();
            triggerExplosion(tntBody.getPosition());
            bodyRemovalQueue.add(tntBody);

        }
        if ((userDataA instanceof BlackBird && (userDataB instanceof Block)) ||
            (userDataB instanceof BlackBird && (userDataA instanceof Block))) {
            BlackBird bird = (userDataA instanceof BlackBird) ? (BlackBird) userDataA : (BlackBird) userDataB;
            Body birdBody = (userDataA instanceof BlackBird) ? bodyA : bodyB;
            System.out.println("Explosion Triggered");
            bird.isshot = true;
            triggerExplosion2(birdBody.getPosition());
            bodyRemovalQueue.add(birdBody);

        }
        if (maxImpulse > MIN_IMPACT_VELOCITY) {
            // Case when RedBird hits Block
            if (userDataA instanceof RedBird && userDataB instanceof Block) {
                RedBird bird = (RedBird) userDataA;
                Block block = (Block) userDataB;

                if (level.blocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyB);
                        destroySound.play();
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
            else if (userDataA instanceof Block && userDataB instanceof YellowBird) {
                YellowBird bird = (YellowBird) userDataB;
                Block block = (Block) userDataA;
                if (level.blocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyA);
                        destroySound.play();
                    }
                }
            }
            else if (userDataA instanceof YellowBird && userDataB instanceof Block) {
                YellowBird bird = (YellowBird) userDataA;
                Block block = (Block) userDataB;
                if (level.blocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyB);
                        destroySound.play();
                    }
                }
            }
        }
        if (maxImpulse > MIN_IMPACT_VELOCITY2) {
            // Case when RedBird hits Block
            if (userDataA instanceof RedBird && userDataB instanceof IceBlock) {
                RedBird bird = (RedBird) userDataA;
                IceBlock block = (IceBlock) userDataB;

                if (level.Iceblocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyB);
                        iceSound.play();
                    }
                }
            }
            else if (userDataA instanceof IceBlock && userDataB instanceof RedBird) {
                RedBird bird = (RedBird) userDataB;
                IceBlock block = (IceBlock) userDataA;
                if (level.Iceblocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyA);
                        iceSound.play();
                    }
                }
            }
            else if (userDataA instanceof IceBlock && userDataB instanceof YellowBird) {
                YellowBird bird = (YellowBird) userDataB;
                IceBlock block = (IceBlock) userDataA;
                if (level.Iceblocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyA);
                        iceSound.play();
                    }
                }
            }
            else if (userDataA instanceof YellowBird && userDataB instanceof IceBlock) {
                YellowBird bird = (YellowBird) userDataA;
                IceBlock block = (IceBlock) userDataB;
                if (level.Iceblocks.contains(block) && bird.isshot) {
                    block.hitcount++;
                    if (block.hitcount >= 0) {
                        bodyRemovalQueue.add(bodyB);
                        iceSound.play();
                    }
                }
            }
        }
    }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }
    private void triggerExplosion(Vector2 explosionCenter) {
        float lowerX = explosionCenter.x - EXPLOSION_RADIUS;
        float lowerY = explosionCenter.y - EXPLOSION_RADIUS;
        float upperX = explosionCenter.x + EXPLOSION_RADIUS;
        float upperY = explosionCenter.y + EXPLOSION_RADIUS;
        level.world.QueryAABB(fixture -> {
            Body body = fixture.getBody();
            if (body != null && body.getType() == BodyDef.BodyType.DynamicBody) {
                Vector2 bodyPosition = body.getPosition();
                Vector2 explosionDir = bodyPosition.cpy().sub(explosionCenter);
                float distance = explosionDir.len();
                if (distance < EXPLOSION_RADIUS && distance > 0) {
                    explosionDir.nor();
                    float force = EXPLOSION_FORCE / (distance * distance);
                    Vector2 explosionForce = explosionDir.scl(force);
                    body.applyLinearImpulse(explosionForce, body.getWorldCenter(), true);
                }
            }
            return true;
        }, lowerX, lowerY, upperX, upperY);
    }
    private void triggerExplosion2(Vector2 explosionCenter) {
        float lowerX = explosionCenter.x - EXPLOSION_RADIUS;
        float lowerY = explosionCenter.y - EXPLOSION_RADIUS;
        float upperX = explosionCenter.x + EXPLOSION_RADIUS;
        float upperY = explosionCenter.y + EXPLOSION_RADIUS;
        level.world.QueryAABB(fixture -> {
            Body body = fixture.getBody();
            if (body != null && body.getType() == BodyDef.BodyType.DynamicBody) {
                Vector2 bodyPosition = body.getPosition();
                Vector2 explosionDir = bodyPosition.cpy().sub(explosionCenter);
                float distance = explosionDir.len();
                if (distance < EXPLOSION_RADIUS2 && distance > 0) {
                    explosionDir.nor();
                    float force = EXPLOSION_FORCE2 / (distance * distance);
                    Vector2 explosionForce = explosionDir.scl(force);
                    body.applyLinearImpulse(explosionForce, body.getWorldCenter(), true);
                }
            }
            return true;
        }, lowerX, lowerY, upperX, upperY);
    }
    public void processBodyRemovalQueue() {
        System.out.println(bodyRemovalQueue.size());
        for (Body body : bodyRemovalQueue) {
            if (body != null && body.getType() == BodyDef.BodyType.DynamicBody) {
                Object userData = body.getUserData();
                if (userData instanceof Block) {
                    Block block = (Block) userData;
                    if (block.hitcount > 0) {
                        level.blocks.remove(block);
                        level.blocks.remove(block);
                        level.world.destroyBody(body);
                        System.out.println("Block removed!");
                    }
                }
                if (userData instanceof IceBlock) {
                    IceBlock block = (IceBlock) userData;
                    if (block.hitcount > 0) {
                        level.Iceblocks.remove(block);
                        level.Iceblocks.remove(block);
                        level.world.destroyBody(body);
                        System.out.println("Block removed!");
                    }
                }
                if (userData instanceof TNT) {
                    TNT tnt = (TNT) userData;
                    level.tnts.remove(tnt);
                    level.world.destroyBody(body);
                    System.out.println("TNT removed!");
                }
            }
        }
        bodyRemovalQueue.clear();
    }

}
