

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class GoldTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class GoldTest
{
    /**
     * Default constructor for test class GoldTest
     */
    public GoldTest()
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
        Gold gold1 = new Gold();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        gold1.run();
        assertEquals("brokenGoldNotTaken", gold1.getState());
        gold1.takeGold();
        gold1.takeGold();
        gold1.takeGold();
        gold1.takeGold();
        gold1.takeGold();
        gold1.run();
        assertEquals("done", gold1.getState());
    }
}

