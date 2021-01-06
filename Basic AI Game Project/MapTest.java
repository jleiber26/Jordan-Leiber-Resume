
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MapTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MapTest
{
    /**
     * Default constructor for test class MapTest
     */
    public MapTest()
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
    public void testSetMap()
    {
        Map map1 = new Map();
        map1.setMap4(12, 12, 5, 5, false);
        boolean isSame = map1.getLocation(4, 4).getTerrain() instanceof Rock;
        boolean isSame2 = map1.getLocation(6, 2).getTerrain() instanceof Lava;
        boolean isSame3 = map1.getLocation(8, 1).getTerrain() instanceof Gold;
        
        assertEquals(isSame, true);
        assertEquals(isSame2, true);
        assertEquals(isSame3, true);
    }

    @Test
    public void testMoneyManipulation()
    {
        Map map1 = new Map();
        map1.decreaseMoney(100);
        assertEquals(9900, map1.getMoney());
        map1.decreaseMoney(10000);
        assertEquals(-100, map1.getMoney());
    }
}


