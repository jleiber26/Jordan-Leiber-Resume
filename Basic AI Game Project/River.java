
/**
 * River is a Terrain type that moves a Dwarf in a particular direction
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class River extends Terrain{
    // instance variables 

    //Define which way you move when you are done in one river block
    private String currentDirection;

    /**
     * Constructor for objects of class River
     * 
     */
    public River(String currentDirection){
        // initialise instance variables
        this.currentDirection = currentDirection;
    }

    /**
     * Returns the direction of the flow
     * 
     * @return the direction of flow
     */
    public String getCurrentDirection(){
        return currentDirection;
    }

    /**
     * Put the River terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */
    public void paint(Graphics g, int boxWidth, int x, int y){
        g.setColor(Color.blue);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
