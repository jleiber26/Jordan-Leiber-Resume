

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class DwarfTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DwarfTest
{
    /**
     * Default constructor for test class DwarfTest
     */
    public DwarfTest()
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
    public void testMove()
    {
        Dwarf dwarf2 = new Dwarf(new Location [30][30] );
        dwarf2.moveX();
        dwarf2.moveX();
        assertEquals(2, dwarf2.getXLoc());
        dwarf2.moveY();
        dwarf2.moveY();
        assertEquals(2, dwarf2.getYLoc());
        dwarf2.moveX();
        dwarf2.moveY();
        assertEquals(3, dwarf2.getXLoc());
        assertEquals(3, dwarf2.getYLoc());
        
    }

    @Test
    public void testSetXY()
    {
        Dwarf dwarf1 = new Dwarf(new Location[30][30]);
        dwarf1.setX(12);
        dwarf1.setY(5);
        assertEquals(12, dwarf1.getXLoc());
        assertEquals(5, dwarf1.getYLoc());
    }

    @Test
    public void testSetCurLocation()
    {
        Dwarf dwarf1 = new Dwarf(new Location[30][30]);
        dwarf1.setLocation(new Location(new Tunnel(), 5, 2));
        boolean tunnel = dwarf1.getCurLocation().getTerrain() instanceof Tunnel;
        assertEquals(tunnel, true);
    }

    @Test
    public void testAddGold()
    {
        Dwarf dwarf1 = new Dwarf(new Location[30][30]);
        dwarf1.addGold();
        dwarf1.addGold();
        dwarf1.addGold();
        dwarf1.addGold();
        dwarf1.addGold();
        assertEquals(5, dwarf1.getGoldStored());
    }

}






