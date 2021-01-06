
/**
 * Extension of the SmartDwarf type. Can turn pits or rivers into tunnel
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class BridgeBuilder extends SmartDwarf{
    // instance variables 
    String state = "walking";

    private final int BRIDGE_DWARF_PRICE = 150;

    /**
     * Constructor for objects of class BridgeBuilder
     */
    public BridgeBuilder(Location[][] playField){
        // initialise instance variables
        super(playField);
    }

    /**
     * Returns a String representation of this Dwarf
     *
     * @return the String 
     */
    public String getType(){
        return "Builder";
    }

    /**
     * Returns the cost of this Dwarf
     * 
     * @return the cost
     */     
    public int getValue(){
        return BRIDGE_DWARF_PRICE;
    }

    /**
     * Follows the path back to gold
     * 
     */
    public void tracePath(){
        if(pathToGold.size() > 0){
            Location tempLoc = pathToGold.pop();
            xLoc = tempLoc.getX();
            yLoc = tempLoc.getY(); 

        }else{
            state = "searching";
        }

    }

    /**
     * Updates the Dwarf's state and acts
     * 
     */
    public void run(){
        //If you stand still foro 10 ticks, lose your memory
        if(standingStill > 10){
            visitedLocations.clear();
            standingStill = 0;
        }

        //If you're on a river or pit, build over it
        if(curLocation.getTerrain() instanceof River || curLocation.getTerrain() instanceof Pit){
            state = "building";
        }

        //Go to the gold that others have found
        if(curLocation.getRuneTree().size() != 0){
            Integer idCheck = (Integer)curLocation.getRuneTree().last();

            for(int i = 0; i < playField.length; i++){
                for(int j = 0; j < playField[i].length; j++){
                    ArrayList<Dwarf> tempList = playField[i][j].getPresentDwarfs();

                    for(int k = 0; k < tempList.size(); k++){
                        if(tempList.get(k).getMyId() == idCheck){
                            pathToGold = tempList.get(k).getPathToGold();
                        }
                    }

                }

            }

            state = "goBackToGold";
        }

        //Move to either gold or a new block
        if(state.equals("walking")){
            if(pathToGold.size() > 0)
                state = "goBackToGold";
            else
                move();
        }
        //Make river or pit into tunnel
        else if(state.equals("building")){
            if(curLocation.getTerrain() instanceof River || curLocation.getTerrain() instanceof Pit){
                curLocation.setTerrainType(new Tunnel());
            }else{
                state = "walking";
            }
            //Pick up gold and take it home if you can't hold anymore
        }else if(state.equals("grabGold")){
            if(curLocation.getTerrain() instanceof Gold){
                Gold tempGold = (Gold)curLocation.getTerrain();
                tempGold.run();
                if(tempGold.getState().equals("brokenGoldNotTaken")){
                    if(goldStored <= GOLD_CAPACITY){
                        tempGold.takeGold();
                        addGold();
                    }else{
                        state = "findHome";
                        tryToFindHome();
                    }
                }else{
                    state = "searching";
                }
            }else{
                state = "searching";
            }

        }else if(state.equals("goBackToGold")){

            tracePath();
            if(visitedLocations.size() > 0)
                visitedLocations.clear();
        }

        if(curLocation.getTerrain() instanceof Lava){
            isDead = true;
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
        Color color = new Color(196, 202, 206);
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
