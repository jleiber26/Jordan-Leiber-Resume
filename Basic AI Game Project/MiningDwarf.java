
/**
 * Extension of the SmartDwarf type. Is responsible for breaking rock and gold
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;

public class MiningDwarf extends SmartDwarf{
    // instance variables 
    private String state = "walking";
    private boolean leaveMap = false;
    private final int MINING_DWARF_PRICE = 35;
    private int armorStrength = 5;

    /**
     * Constructor for objects of class MiningDwarf
     */
    public MiningDwarf(Location[][] playField)
    {
        // initialise instance variables
        super(playField);
    }

    /**
     * Returns a String representation of this Dwarf
     *
     * @return the String 
     */
    public String getType(){
        return "Miner";
    }

    /**
     * Returns the cost of this Dwarf
     * 
     * @return the cost
     */ 
    public int getValue(){
        return MINING_DWARF_PRICE;
    }

    /**
     * Checks to see that the current location is rock or gold. If it is, hit it
     *
     */
    public void mine(){
        if(curLocation.getTerrain() instanceof Rock){
            Rock tempTerrain = (Rock)curLocation.getTerrain();
            tempTerrain.run();
        }else if(curLocation.getTerrain() instanceof Gold){
            Gold tempTerrain = (Gold)curLocation.getTerrain();
            tempTerrain.run();
        }
    }

    /**
     * Moves the Dwarf with the River for 3 ticks
     * 
     */
    public void flow(){
        //If where you are is a River flow in the direction of the river
        if(curLocation.getTerrain() instanceof River){
            River tempRiver = (River)curLocation.getTerrain();
            if(tempRiver.getCurrentDirection().equals("up")){
                yLoc--;
            }else if(tempRiver.getCurrentDirection().equals("down")){
                yLoc++;
            }else if(tempRiver.getCurrentDirection().equals("left")){
                xLoc--;
            }else if(tempRiver.getCurrentDirection().equals("right")){
                xLoc++;
            }
        }else{
            timeToFlow = 0;
        }
    }

    /**
     * Take the Dwarf out of the River
     */
    public void leaveRiver(){
        River tempRiver = (River)curLocation.getTerrain();
        //Choose the direction you will get out in
        if(tempRiver.getCurrentDirection().equals("up")){
            xLoc--;

        }else if(tempRiver.getCurrentDirection().equals("down")){
            xLoc++;

        }else if(tempRiver.getCurrentDirection().equals("left")){
            yLoc--;

        }else if(tempRiver.getCurrentDirection().equals("right")){
            yLoc++;

        }
    }

    /**
     * Get the Dwarf's state
     * 
     * @return the Dwarf's state
     */
    public String getState(){
        return state;
    }

    /**
     * Sees if the Miner is done their work
     * 
     * @return true if the dwarf should leave the map
     */
    public boolean doneWorking(){
        return leaveMap;
    }

    /**
     * Updates the Dwarf's state and acts
     * 
     */
    public void run(){

        //If you stand still for 10 ticks, lose your memory
        if(standingStill > 10){
            visitedLocations.clear();
            standingStill = 0;
        }

        //If you're on gold or rock start mining
        if(curLocation.getTerrain() instanceof Rock || curLocation.getTerrain() instanceof Gold){
            state = "mining";

        }

        //If you're walking
        if(state.equals("walking")){
            //See if you're in a river
            if(curLocation.getTerrain() instanceof River){
                state = "inRiver";
                timeToFlow = 3;
            }else{
                move();
            }
        }
        //If you're mining
        else if(state.equals("mining")){
            //If the rock is broken go back to walking, otherwise mine
            if(curLocation.getTerrain() instanceof Rock){
                Rock tempRock = (Rock)curLocation.getTerrain();

                if(tempRock.getState().equals("broken")){
                    state = "walking";
                }else{
                    mine();
                }
                //If you're on gold hit it if it's not broken 
            }else if(curLocation.getTerrain() instanceof Gold){
                Gold tempGold = (Gold)curLocation.getTerrain();
                if(tempGold.getState().equals("brokenGoldNotTaken")){
                    state = "walking";
                    move();
                }else{
                    mine();
                }
            }
            else{
                state = "walking";
            }

        }
        //If you're in a river flow for 3 ticks
        else if(state.equals("inRiver")){
            if(curLocation.getTerrain() instanceof River){
                River tempRiver = (River)curLocation.getTerrain();

                if(timeToFlow > 0){
                    flow();
                    timeToFlow--;

                    if(xLoc <= 0 && tempRiver.getCurrentDirection().equals("left")
                    || xLoc >= 29 && tempRiver.getCurrentDirection().equals("right")
                    || yLoc >= 29 && tempRiver.getCurrentDirection().equals("down")
                    || yLoc <= 0 && tempRiver.getCurrentDirection().equals("up")){
                        kill();
                    }

                }else{
                    leaveRiver();
                    state = "walking";
                }
            }else{
                state = "walking";
            }

        }
        //If you're in lava or a pit you lose armor
        if(curLocation.getTerrain() instanceof Lava || curLocation.getTerrain() instanceof Pit){
            armorStrength--;
            //If you lost your armor, you leave the map
            if(armorStrength <= 0)
                leaveMap = true;
        }
    }

    /**
     * Put the Dwarf into the GUI
     * 
     * @param g the Graphics object
     * @param x the x location of the box
     * @param y the y location of the box
     * @param size the circle's diameter
     */  
    public void paint(Graphics g, int x, int y, int size){
        g.setColor(Color.orange);

        if(leaveMap == true){
            x = 999999;
        }
        g.fillOval(x, y, size, size);

        g.setColor(Color.white);

        String tempString = Integer.toString(getMyId());
        g.drawString(tempString, x, y + size / 2);

    }
}

