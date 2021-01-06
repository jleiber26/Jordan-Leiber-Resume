
/**
 * Rock is a Terrain type that stops Dwarves from moving through it. Can be hit in order to turn it into a Tunnel.
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class Rock extends Terrain{
    // instance variables

    //How many hits it takes to break the Rock
    private int strength;
    //The current state of the Rock
    private String state;

    /**
     * Constructor for objects of class Rock
     */
    public Rock()
    {
        // initialise instance variables
        Random random = new Random();
        //Each rock can be turned into a tunnel in a different amount of hits
        strength = random.nextInt(3);

        while(strength <= 0){
            strength = random.nextInt(3);

        }

        //Possible states: standing/broken
        state = "standing";

    }

    /**
     * Breaks the rock by one hit
     */
    public void hitRock(){
        strength--;
    }

    /**
     * Constructor for objects of class Rock
     * 
     * @return the state of the rock
     */
    public String getState(){
        return state;
    }

    /**
     * Just print out a letter representing what type of terrain this is. ONLY FOR TESTING DELETE THIS METHOD!!
     * 
     */
    public void printType()
    {
        // put your code here
        System.out.print("Ro");
    }

    /**
     * Update the state of the Rock
     */
    public void run(){
        //If the rock still has strength hit it
        if(strength > 0){
            hitRock();
        }
        else{
            state = "broken";
        }
    }

    /**
     * Put the Rock terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */
    public void paint(Graphics g, int boxWidth, int x, int y){
        g.setColor(Color.black);
        g.fillRect(x, y, boxWidth, boxWidth);
    }
}
