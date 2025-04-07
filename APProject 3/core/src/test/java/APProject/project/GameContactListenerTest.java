package APProject.project;

import com.badlogic.gdx.physics.box2d.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GameContactListenerTest {
    @Test
    public void testPigDiesFromHighImpact() {

        GameContactListener contactListener = new GameContactListener(null);
        Pig pig = new Pig(null, 0, 0, "pig.png", 1, 1);
        pig.health = 100;


        Contact contact = mock(Contact.class);
        Fixture fixtureA = mock(Fixture.class);
        Body bodyA = mock(Body.class);

        when(bodyA.getUserData()).thenReturn(pig);
        when(fixtureA.getBody()).thenReturn(bodyA);
        when(contact.getFixtureA()).thenReturn(fixtureA);

        // Create a high impact force (15 * 10 = 150 damage)
        ContactImpulse impulse = mock(ContactImpulse.class);
        float[] normalImpulses = {15.0f};
        when(impulse.getNormalImpulses()).thenReturn(normalImpulses);

        // Apply the impact
        contactListener.postSolve(contact, impulse);

        // Verify pig died from the impact
        assertTrue("Pig should be dead from high impact", pig.isdead);
        assertEquals("Pig health should be -50", -50.0f, pig.health, 0.01);
    }
}
