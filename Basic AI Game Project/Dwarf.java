
/**
 * The most basic Dwarf type. Walks around completely randomly, and looks for gold. If gold is found, it is brought back Home.
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class Dwarf{
    public static int id = 0;

    // instance variables 
    protected int xLoc;
    protected int yLoc;
    protected int prevX;
    protected int prevY;
    protected int timeToFlow = 0;
    protected Location[][] playField;

    protected boolean isDead = false;

    protected final int GOLD_CAPACITY;
    protected int goldStored = 0;
    protected int productivity = 0;
    protected int standingStill = 0;

    private final int DWARF_PRICE = 25;

    protected Stack<Location> visitedLocations = new Stack<Location>();
    protected Stack<Location> pathToGold = new Stack<Location>();

    public ArrayList<Location> knownDanger = new ArrayList<Location>();

    protected String state;
    protected Location curLocation = null;
    int myId;

    /**
     * Constructor for objects of class Dwarf
     */
    public Dwarf(Location[][] playField)
    {
        // initialise instance variables
        xLoc = 0;
        yLoc = 0;
        prevX = xLoc;
        prevY = yLoc;
        state = "searching";
        myId = id;
        id++;

        this.playField = playField;
        GOLD_CAPACITY = 3;
    }

    /**
     * Returns a String representation of the type of dwarf
     * 
     * @return the type
     */ 
    public String getType(){
        return "Dwarf";
    }

    /**
     * Returns the cost of this Dwarf
     * 
     * @return the cost
     */ 
    public int getValue(){
        return DWARF_PRICE;
    }

    /**
     * Increases the standingStill timer
     * 
     */ 
    public void standingStill(){
        standingStill++;
    }

    /**
     * Resets the standing still timer to 0
     * 
     */ 
    public void resetStandingStill(){
        standingStill = 0;
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

        //Make sure the Dwarf stays on the map
        while(xLoc < 0)
            xLoc++;
        while(yLoc < 0)
            yLoc++;
        while(xLoc > 29)
            xLoc--;
        while(yLoc > 29)
            yLoc--;

    }

    /**
     * Adds a location to the Dwarf's memory
     * 
     * @param addedLoc the location being add
     */ 
    public void addLocation(Location addedLoc){
        visitedLocations.add(addedLoc);
    }

    /**
     * Adds a location to the Dwarf's memory of a path to gold
     * 
     * @param addedLoc the location being add
     */ 
    public void addToGoldPath(Location input){
        pathToGold.add(input);
    }

    /**
     * Returns the path to a gold location
     * 
     * @return the path
     */ 
    public Stack getPathToGold(){
        return pathToGold;
    }

    /**
     * Leaves your ID on a location so others know you've been there
     * 
     */ 
    public void leaveRune(){
        curLocation.addRune(myId);
    }

    /**
     * Moves the Dwarf right by one space
     *
     */
    public void moveX(){
        xLoc++;
    }

    /**
     * Moves the Dwarf up by one space
     *
     */
    public void moveY(){
        yLoc++;
    }

    /**
     * Get the Dwarf's X location
     * 
     * @return the Dwarf's x location
     */
    public int getXLoc(){
        return xLoc;
    }

    /**
     * Get the Dwarf's Y location
     * 
     * @return the Dwarf's Y location
     */
    public int getYLoc(){
        return yLoc;
    }

    /**
     * Get the Dwarf's X location
     * 
     * @return the Dwarf's x location
     */
    public int getPrevXLoc(){
        return prevX;
    }

    /**
     * Get the Dwarf's Y location
     * 
     * @return the Dwarf's Y location
     */
    public int getPrevYLoc(){
        return prevY;
    }

    /**
     * Sets the Dwarf's X Location
     * 
     * @param the new x Locatoin
     */ 
    public void setX(int x){
        xLoc = x; 
    }

    /**
     * Sets the Dwarf's Y Location
     * 
     * @param the new y Locatoin
     */ 
    public void setY(int y){
        yLoc = y;
    }

    /**
     * Get the Dwarf's location
     * 
     * @return the Dwarf's location
     */
    public Location getCurLocation(){
        return curLocation;
    }

    /**
     * Sets the Dwarf's location
     * 
     */
    public void setLocation(Location inLoc){
        curLocation = inLoc;
    }

    /**
     * Adds gold to the Dwarf's stored gold
     * 
     */ 
    public void addGold(){
        goldStored++;
        productivity++;
    }

    /**
     * Return the amount of gold a Dwarf has
     * 
     * @return amount of gold
     */ 
    public int getGoldStored(){
        return goldStored;
    }

    /**
     * Returns a Dwarf's productivity
     * 
     * @return the productivity
     */ 
    public int getProductivity(){
        return productivity;
    }

    /**
     * Get the Dwarf's ID
     * 
     * @return the Dwarf's ID
     */
    public int getMyId(){
        return myId;
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
     * Moves the Dwarf home by going up and left
     * 
     */
    public void tryToFindHome(){
        //If you can move up go, otherwise go left as far as possible
        if(yLoc > 0)
            yLoc--;
        else if(xLoc > 0)
            xLoc--;

        leaveRune();
    }

    /**
     * Moves the Dwarf with the River for 3 ticks
     * 
     */
    public void flow(){
        //If where you are is a River
        if(curLocation.getTerrain() instanceof River){
            River tempRiver = (River)curLocation.getTerrain();
            //If the river flows up go up
            if(tempRiver.getCurrentDirection().equals("up")){
                yLoc--;
                //If the river flows down go down
            }else if(tempRiver.getCurrentDirection().equals("down")){
                yLoc++;
                //if the river goes left go left
            }else if(tempRiver.getCurrentDirection().equals("left")){
                xLoc--;
                //If the river goes right go right
            }else if(tempRiver.getCurrentDirection().equals("right")){
                xLoc++;
            }
        }else{
            timeToFlow = 0;
        }
    }

    /**
     * Add a location to the list of locations to avoid
     * 
     * @param input the location being added
     */
    public void addDanger(Location input){
        knownDanger.add(input);
    }

    /**
     * Take the Dwarf out of the River
     */
    public void leaveRiver(){
        River tempRiver = (River)curLocation.getTerrain();
        //Choose the direction you want to step out in
        if(tempRiver.getCurrentDirection().equals("up")){
            if(!(playField[xLoc - 1][yLoc].getTerrain() instanceof Rock))
                xLoc--;
            else 
                flow();
        }else if(tempRiver.getCurrentDirection().equals("down")){
            if(!(playField[xLoc + 1][yLoc].getTerrain() instanceof Rock))
                xLoc++;
            else 
                flow();
        }else if(tempRiver.getCurrentDirection().equals("left")){
            if(!(playField[xLoc][yLoc - 1].getTerrain() instanceof Rock))
                yLoc--;
            else 
                flow();
        }else if(tempRiver.getCurrentDirection().equals("right")){
            if(!(playField[xLoc][yLoc + 1].getTerrain() instanceof Rock))
                yLoc++;
            else 
                flow();
        }
    }

    /**
     * Follows the path back to gold
     * 
     */
    public void tracePath(){
        //If there is a path to follow, set your next x and y locations to the most recent location
        if(pathToGold.size() > 0){
            Location tempLoc = pathToGold.pop();
            xLoc = tempLoc.getX();
            yLoc = tempLoc.getY(); 

        }else{
            state = "searching";
        }

    }

    /**
     * Kill the Dwarf
     * 
     */
    public void kill(){
        isDead = true;
    }

    /**
     * Sees if the Dwarf is dead
     * 
     * @return true if the dwarf is dead
     */
    public boolean isDead(){
        return isDead;
    }

    /**
     * Get the amount of time a dwarf has been standing still
     * 
     * @return the time
     */
    public int getTimeStill(){
        return standingStill;
    }

    /**
     * Updates the Dwarf's state and acts
     * 
     */
    public void run(){
        prevX = xLoc;
        prevY = yLoc;

        //If you have been standing still for 10 ticks, reset your memory
        if(standingStill > 10){
            visitedLocations.clear();
            standingStill = 0;
        }

        //If you're searching
        if(state.equals("searching")){
            //If you have a path to gold, follow it
            if(pathToGold.size() > 0){
                state = "goBackToGold";

            }else{
                //If you're in a tunnel or home you can move
                if(curLocation.getTerrain() instanceof Tunnel || curLocation.getTerrain() instanceof Home){
                    move();
                    //If you're in lava or a pit you die
                }else if(curLocation.getTerrain() instanceof Lava || curLocation.getTerrain() instanceof Pit){
                    isDead = true;
                    //If you're in gold
                }else if(curLocation.getTerrain() instanceof Gold){
                    Gold tempGold = (Gold)curLocation.getTerrain();
                    //If the gold is broken, start taking it
                    if(tempGold.getState().equals("brokenGoldNotTaken")){
                        state = "grabGold";
                        //Otherwise move
                    }else{
                        move();
                    }
                    //If youre in a rive flow
                }else if(curLocation.getTerrain() instanceof River){
                    state = "inRiver";
                    timeToFlow = 3;
                    //if where you are has a path to gold follow it
                }else if(curLocation.getRuneTree().size() != 0){
                    Integer idCheck = (Integer)curLocation.getRuneTree().last();

                    //Find the dwarf who left the rune and take their path
                    for(int i = 0; i < playField.length; i++){
                        for(int j = 0; j < playField[i].length; j++){
                            ArrayList<Dwarf> tempList = playField[i][j].getPresentDwarfs();

                            for(int k = 0; k < tempList.size(); k++){
                                if(tempList.get(i).getMyId() == idCheck){
                                    pathToGold = tempList.get(i).getPathToGold();
                                }
                            }

                        }

                    }

                    state = "goBackToGold";
                }
            }
            //If you are grabbing gold
        }else if(state.equals("grabGold")){
            if(curLocation.getTerrain() instanceof Gold){
                Gold tempGold = (Gold)curLocation.getTerrain();
                tempGold.run();
                if(tempGold.getState().equals("brokenGoldNotTaken")){
                    //If you have more space keep grabbing otherwise go home
                    if(goldStored < GOLD_CAPACITY){

                        tempGold.takeGold();
                        addGold();
                    }else{
                        state = "findHome";
                        tryToFindHome();
                    }

                    //If you have gold and the location is empty take it home
                }else if(tempGold.getState().equals("done") && goldStored > 0){
                    state = "findHome";
                    tryToFindHome();

                }else{
                    state = "searching";

                }
            }else{
                state = "searching";
            }
            //If you're in a river, flow with it
        }else if(state.equals("inRiver")){
            if(curLocation.getTerrain() instanceof River){
                River tempRiver = (River)curLocation.getTerrain();

                if(timeToFlow > 0){
                    flow();
                    timeToFlow--;
                    //If you go off screen, you die
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
            //If you're walking home
        }else if(state.equals("findHome")){
            tryToFindHome();
            //If you get home put the gold down
            if(playField[xLoc][yLoc].getTerrain() instanceof Home){
                state = "dropGold";

            }
            //If you're dropping gold
        }else if(state.equals("dropGold")){

            if(curLocation.getTerrain() instanceof Home){
                Home tempHome = (Home)curLocation.getTerrain();

                //Put it in the bin until you don't have more, then go home
                if(goldStored > 0){
                    tempHome.addGold();
                    goldStored--;
                }else{
                    //THIS IS WHERE REVERSABLE MEM GOES
                    state = "goBackToGold";
                }

            }
            //If you're going back to the gold
        }else if(state.equals("goBackToGold")){
            tracePath();
            if(visitedLocations.size() > 0)
                visitedLocations.clear();
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
        g.setColor(Color.green);
        if(isDead == true){
            x = 999999;
        }
        g.fillOval(x, y, size, size);

        g.setColor(Color.white);

        String tempString = Integer.toString(getMyId());
        g.drawString(tempString, x, y + size / 2);
    }
}
