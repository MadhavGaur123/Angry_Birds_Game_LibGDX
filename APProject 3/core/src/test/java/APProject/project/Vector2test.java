package APProject.project;

import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Vector2test {
    private Vector2 vector;
    private static final float DELTA = 0.0001f;
    @Before
    public void setUp() {
        vector = new Vector2(5.0f, 12.0f);
    }
    @Test
    public void testLength() {
        assertEquals(13.0f, vector.len(), DELTA);
    }

    @Test
    public void testLengthSquared() {
        assertEquals(169.0f, vector.len2(), DELTA);
    }
    @Test
    public void testAdd() {
        Vector2 other = new Vector2(1.0f, 2.0f);
        vector.add(other);
        assertEquals(6.0f, vector.x, DELTA);
        assertEquals(14.0f, vector.y, DELTA);
    }

    @Test
    public void testSubtract() {
        Vector2 other = new Vector2(1.0f, 2.0f);
        vector.sub(other);
        assertEquals(4.0f, vector.x, DELTA);
        assertEquals(10.0f, vector.y, DELTA);
    }




}
