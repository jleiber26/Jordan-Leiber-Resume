
/**
 * Extension of the Dwarf type. Implements a different moving mechanism so that the Smart Dwarf won't walk somwhere it has already been.
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class SmartDwarf extends Dwarf{
    // instance variables 
    protected final int GOLD_CAPACITY;

    private final int SMART_DWARF_PRICE = 25;

    ArrayList<Location> knownDanger = new ArrayList<Location>();

    /**
     * Constructor for objects of class SmartDwarf
     */
    public SmartDwarf(Location[][] playField)    {
        // initialise instance variables
        super(playField);
        GOLD_CAPACITY = 4;
    }

    /**
     * Returns a String representation of this Dwarf
     *
     * @return the String 
     */
    public String getType(){
        return "Walker";
    }

    /**
     * Returns the cost of this Dwarf
     * 
     * @return the cost
     */ 
    public int getValue(){
        return SMART_DWARF_PRICE;
    }

    /**
     * Tells if the Dwarf has been in a given location
     *
     * @param  x  the x coordinate of the location
     * @param y the y coordinate of the location
     * 
     * @return    true if the Dwarf has been there
     */
    public boolean hasBeenHere(int x, int y){
        //Go through each of the locations you've visited
        for(int i = 0; i < visitedLocations.size(); i++){
            //If you've been there return true
            if(visitedLocations.get(i).getX() == x && visitedLocations.get(i).getY() == y){
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a location to the list of danger locations
     * 
     * @param input the location being added
     */ 
    public void addDanger(Location input){
        if(!knownDanger.contains(input))
            knownDanger.add(input);
    }

    /**
     * Tells if a location is known to be dangerous
     *
     * @param  x  the x coordinate of the location
     * @param y the y coordinate of the location
     * 
     * @return    true if the location is dangerous
     */
    public boolean isDanger(int x, int y){
        for(int i = 0; i < knownDanger.size(); i++){
            if(knownDanger.get(i).getX() == x && knownDanger.get(i).getY() == y){
                return true;
            }
        }

        return false;
    }

    /**
     * Moves the Dwarf in a random direction by one space
     *
     */
    public void move(){
        Random random = new Random();
        int direction = random.nextInt(4);
        prevX = xLoc;
        prevY = yLoc;

        //Up, down, left, or right
        if(direction == 0){
            yLoc--;
        }else if(direction == 1){
            xLoc--;
        }else if(direction == 2){
            yLoc++;
        }else if(direction == 3){
            xLoc++;
        }

        //Make sure you stay in the map
        while(xLoc < 0)
            xLoc++;
        while(yLoc < 0)
            yLoc++;
        while(xLoc > 29)
            xLoc--;
        while(yLoc > 29)
            yLoc--;

        //If you have already been where you are pick a different position
        //Choose until you find a direction you have not taken
        if(hasBeenHere(xLoc, yLoc)){
            xLoc = prevX;
            yLoc = prevY;
            if(!hasBeenHere(xLoc + 1, yLoc) && xLoc < 29)
                xLoc++;
            else if(!hasBeenHere(xLoc, yLoc + 1) && yLoc < 29)
                yLoc++;
            else if(!hasBeenHere(xLoc - 1, yLoc) && xLoc > 0)
                xLoc--;
            else if(!hasBeenHere(xLoc, yLoc - 1) && yLoc > 0)
                yLoc--;
            else if(!hasBeenHere(xLoc - 1, yLoc - 1) && yLoc > 0 && xLoc > 0){
                xLoc--;
                yLoc--;
            }
            else if(!hasBeenHere(xLoc - 1, yLoc + 1) && yLoc < 29 && xLoc > 0){
                xLoc--;
                yLoc++;
            }
            else if(!hasBeenHere(xLoc + 1, yLoc + 1) && yLoc < 29 && xLoc < 29){
                xLoc++;
                yLoc++;
            }
            else if(!hasBeenHere(xLoc + 1, yLoc - 1) && yLoc > 0 && xLoc < 29){
                xLoc++;
                yLoc--;
            }else{
                Location tempLoc = visitedLocations.pop();
                xLoc = tempLoc.getX();
                yLoc = tempLoc.getY();
            }
        }

        //See if where you are is dangerous
        //If it is pick a surrounding location that is known to be safe
        if(isDanger(xLoc, yLoc)){
            xLoc = prevX;
            yLoc = prevY;
            if(!isDanger(xLoc + 1, yLoc) && xLoc < 29)
                xLoc++;
            else if(!isDanger(xLoc, yLoc + 1) && yLoc < 29)
                yLoc++;
            else if(!isDanger(xLoc - 1, yLoc) && xLoc > 0)
                xLoc--;
            else if(!isDanger(xLoc, yLoc - 1) && yLoc > 0)
                yLoc--;
            else if(!isDanger(xLoc - 1, yLoc - 1) && yLoc > 0 && xLoc > 0){
                xLoc--;
                yLoc--;
            }
            else if(!isDanger(xLoc - 1, yLoc + 1) && yLoc < 29 && xLoc > 0){
                xLoc--;
                yLoc++;
            }
            else if(!isDanger(xLoc + 1, yLoc + 1) && yLoc < 29 && xLoc < 29){
                xLoc++;
                yLoc++;
            }
            else if(!isDanger(xLoc + 1, yLoc - 1) && yLoc > 0 && xLoc < 29){
                xLoc++;
                yLoc--;
            }else{
                Location tempLoc = visitedLocations.pop();
                xLoc = tempLoc.getX();
                yLoc = tempLoc.getY();
            }
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
        Color color = new Color(0, 124, 0);
        g.setColor(color);
        if(isDead == true){
            x = 999999;
        }
        g.fillOval(x, y, size, size);

        g.setColor(Color.white);

        String tempString = Integer.toString(getMyId());
        g.drawString(tempString, x, y + size / 2);
    }
}
