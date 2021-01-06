
/**
 * Extension of the SmartDwarf type. Can recognize and tell others about dangerous locations
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class YellerDwarf extends SmartDwarf{
    // instance variables 

    private final int YELLER_DWARF_PRICE = 100;

    /**
     * Constructor for objects of class YellerDwarf
     */
    public YellerDwarf(Location[][] playField)
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
        return "Yeller";
    }

    /**
     * Returns the cost of this Dwarf
     * 
     * @return the cost
     */     
    public int getValue(){
        return YELLER_DWARF_PRICE;
    }

    /**
     * If you are next to a danger location, tell all dwarves in a 9 block square about it
     *
     * @param  location the location you warn people about
     */
    public void yellDanger(Location danger){
        ArrayList<Location> warningArea = new ArrayList<Location>();

        int xWarn = xLoc - 1;
        int yWarn = yLoc - 1;
        int index = 0;

        //Go through all blocks around you
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(xWarn >= 0 && yWarn >= 0 && xWarn < 30 && yWarn < 30)
                    warningArea.add(playField[xWarn][yWarn]);

                yWarn++;
                index++;
            }
            xWarn++;
            yWarn = yLoc - 1;
        }

        if(warningArea.size() > 0){

            for(int i = 0; i < warningArea.size(); i++){
                for(int j = 0; j < warningArea.get(i).getPresentDwarfs().size(); j++){
                    warningArea.get(i).getPresentDwarfs().get(j).addDanger(danger);
                }

            }
        }
    }

    /**
     * Updates the Dwarf's state and acts
     * 
     */
    public void run(){
        if(standingStill > 10){
            visitedLocations.clear();
            standingStill = 0;
        }

        //Check all surrounding blocks to see if you have anything to yell about
        for(int i = xLoc - 1; i < xLoc + 2; i++){
            for(int j = yLoc - 1; j < yLoc + 2; j++){
                if(i >= 0 && j >= 0 && i < 30 && j < 30){
                    if(playField[j][i].getTerrain() instanceof Lava || playField[j][i].getTerrain() instanceof Pit || playField[j][j].getTerrain() instanceof River){
                        yellDanger(playField[j][i]);
                    }
                }
            }
        }

        if(state.equals("searching")){
            if(curLocation.getTerrain() instanceof Tunnel || curLocation.getTerrain() instanceof Home){
                move();
            }else if(curLocation.getTerrain() instanceof Lava || curLocation.getTerrain() instanceof Pit){
                isDead = true;
            }else if(curLocation.getTerrain() instanceof Gold){
                Gold tempGold = (Gold)curLocation.getTerrain();
                if(tempGold.getState().equals("brokenGoldNotTaken") && curLocation.getPresentDwarfs().size() <= 1){
                    state = "grabGold";
                }else{
                    move();
                }
            }else if(curLocation.getTerrain() instanceof River){
                state = "inRiver";
                timeToFlow = 3;
            }

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

        }else if(state.equals("inRiver")){
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
                    state = "searching";
                }
            }else{
                state = "searching";
            }

        }else if(state.equals("findHome")){
            tryToFindHome();
            if(playField[xLoc][yLoc].getTerrain() instanceof Home){
                state = "dropGold";

            }
        }else if(state.equals("dropGold")){

            if(curLocation.getTerrain() instanceof Home){
                Home tempHome = (Home)curLocation.getTerrain();

                if(goldStored > 0){
                    tempHome.addGold();
                    goldStored--;
                }else{
                    visitedLocations.clear();
                    state = "searching";
                }

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
        Color color = new Color(127, 14, 183);
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

