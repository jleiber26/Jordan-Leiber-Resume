
/**
 * An abstract class to define the type of Terrain a Location takes
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */
import java.awt.Color;
import java.awt.Graphics;

public abstract class Terrain
{
    // instance variables 

    /**
     * Constructor for objects of class Terrain
     */
    public Terrain()
    {
        // initialise instance variables
    }

    /**
     * Prints a character representation of the terrain
     * 
     */
    public void printType(){
        // put your code here
        System.out.println("T");
    }
    
    public abstract void paint(Graphics g, int boxWidth, int x, int y);
    
}
