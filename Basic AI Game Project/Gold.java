
/**
 * Gold is a Terrain type that holds a given amount of value. Can be hit in order to get Gold out.
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class Gold extends Terrain{
    // instance variables    
    private int goldContained;
    private int goldRemoved = 0;
    private int difficultyToMine;
    private int hitCount = 0;

    private String state;

    /**
     * Constructor for objects of class Rock
     */
    public Gold(){
        // initialise instance variables
        Random random = new Random();
        //Each rock can be turned into a tunnel in a different amount of hits
        goldContained = random.nextInt(6);

        while(goldContained <= 0){
            goldContained = random.nextInt(6);
        }

        difficultyToMine = random.nextInt(3);
        while(difficultyToMine <= 0){
            difficultyToMine = random.nextInt(3);
        }

        //Possible states: standing/brokenGoldNotTaken/brokenNothingLeft
        state = "standing";

    }

    /**
     * Breaks off a piece of gold and adds it to the collactable pile
     */    
    public void hitGold(){
        hitCount++;
        //The strength of the Gold determines how often it breaks off a piece
        if(hitCount % difficultyToMine == 0){
            goldContained--;
            goldRemoved++;
        }
    }

    /**
     * Picks up a piece of collectable gold
     */    
    public void takeGold(){
        goldRemoved--;
    }

    /**
     * Returns the state of the gold
     * 
     * @return the gold's state
     */    
    public String getState(){
        return state;
    }

    public int getGoldContained(){
        return goldContained;
    }

    /**
     * Changes the Gold's state
     */    
    public void run(){
        //If there is gold that can be mined, hit it
        if(state.equals("standing")){
            if(goldContained > 0){
                hitGold();
            }else{
                state = "brokenGoldNotTaken";
            }
            //Collect the gold
        }else if(state.equals("brokenGoldNotTaken")){
            if(goldRemoved <= 0){
                state = "done";
            }

        }

    }

    /**
     * Put the Gold terrain into the GUI
     * 
     * @param g the Graphics object
     * @param boxWidth the width of the location box
     * @param x the x location of the box
     * @param y the y location of the box
     */   
    public void paint(Graphics g, int boxWidth, int x, int y){
        Color color = new Color(208, 213, 39);

        if(state.equals("standing"))
            g.setColor(Color.yellow);
        else if(state.equals("brokenGoldNotTaken")){
            g.setColor(color);
        }

        g.fillRect(x, y, boxWidth, boxWidth);

        g.setColor(Color.black);
        String tempString = Integer.toString(goldContained);
        g.drawString(tempString, x, y + boxWidth / 2);

    }
}
