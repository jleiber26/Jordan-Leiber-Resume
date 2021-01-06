
/**
 * Pit is a Terrain type that kills Dwarves
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;

public class Pit extends Terrain{
    // instance variables 

    /**
     * Constructor for objects of class Lava
     */
    public Pit(){
        // initialise instance variables
    }

    /**
     * If a Dwarf falls into the Pit kill them (set them to null)
     *
     * @param  input a Dwarf stepping into the Pit
     */
    public void run(Dwarf input){
        //Kill the Dwarf by setting them to null
        input = null;
    }

    /**
     * Put the Pit terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */     
    public void paint(Graphics g, int boxWidth, int x, int y){
        Color c = new Color(150,75,0);
        g.setColor(c);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
