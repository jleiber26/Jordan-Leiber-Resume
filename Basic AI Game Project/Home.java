
/**
 * Home is a Terrain type where Dwarves start. Gold is returned here. 
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;

public class Home extends Terrain{
    // instance variables 
    public int goldContained = 0;

    /**
     * Constructor for objects of class Home
     */
    public Home()
    {
        // initialise instance variables
    }

    /**
     * Adds Gold to the total 
     *
     */
    public void addGold(){
        goldContained++;
    }

    /**
     * Returns the amount of Gold that has been returned Home
     *
     * @return the amount of Gold
     */
    public int getGoldContained(){
        return goldContained;
    }

    /**
     * Put the Home terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */
    public void paint(Graphics g, int boxWidth, int x, int y){
        g.setColor(Color.cyan);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
