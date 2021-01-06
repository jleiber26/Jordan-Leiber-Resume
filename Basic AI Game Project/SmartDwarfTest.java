

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class SmartDwarfTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SmartDwarfTest
{
    /**
     * Default constructor for test class SmartDwarfTest
     */
    public SmartDwarfTest()
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
    public void testIsDanger()
    {
        SmartDwarf smartDwa1 = new SmartDwarf(new Location[30][30]);
        smartDwa1.addDanger(new Location(new Lava(), 5, 5));
        assertEquals(true, smartDwa1.isDanger(5, 5));
        assertEquals(false, smartDwa1.isDanger(12, 12));
    }
}

