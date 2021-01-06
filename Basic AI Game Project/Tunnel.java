
/**
 * Tunnel is a Terrain type that Dwarves can move through
 * 
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;

public class Tunnel extends Terrain{
    // instance variables 

    /**
     * Constructor for objects of class Tunnel
     */
    public Tunnel()
    {
        // initialise instance variables
    }

    /**
     * Put the Tunnel terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */   
    public void paint(Graphics g, int boxWidth, int x, int y){
        g.setColor(Color.gray);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
