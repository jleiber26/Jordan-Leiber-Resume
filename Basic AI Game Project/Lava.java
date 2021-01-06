
/**
 * Lava is a Terrain type that kills Dwarves
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;

public class Lava extends Terrain{
    // instance variables 

    /**
     * Constructor for objects of class Lava
     */
    public Lava()
    {
        // initialise instance variables
    }

    /**
     * If a Dwarf touches Lava kill them (set them to null)
     *
     * @param  input a Dwarf stepping into the Lava
     */
    public void run(Dwarf input)
    {
        //Kill the Dwarf by setting them to null
        input = null;
    }

    /**
     * Put the Lava terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */    
    public void paint(Graphics g, int boxWidth, int x, int y){
        g.setColor(Color.red);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
