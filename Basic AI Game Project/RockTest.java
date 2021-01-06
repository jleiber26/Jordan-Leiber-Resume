

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RockTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RockTest
{
    /**
     * Default constructor for test class RockTest
     */
    public RockTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testRun()
    {
        Rock rock1 = new Rock();
        assertEquals("standing", rock1.getState());
        rock1.run();
        rock1.run();
        rock1.run();
        assertEquals("broken", rock1.getState());
    }
}

