
/**
 * Creates a 2d array of locations to create the playfield. Dwarves move around on these locations. The map stores, and is responsible\
 * for running the game
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Map extends Canvas
{
    // instance variables 

    public Location[][] playField;
    private ArrayList<Dwarf> dwarvesInGame = new ArrayList<Dwarf> ();
    private ArrayList<Integer> order = new ArrayList<Integer> ();
    private int[] possibleLocations;

    public JFrame frame;
    public int gridSize;
    int mapSize;

    //Variables to keep track of where all gold is on the map to determine when the game ends
    public int totalGoldOnMap = 0;
    public int goldCollected = 0;
    public int goldLost = 0;
    public int ticks = 0;
    public int deadDwarves = 0;

    private final int VALUE_OF_GOLD = 2500;

    public int money = 10000;

    public boolean gameOver;

    private PrintWriter stateOut;
    /**
     * Constructor for objects of class Map
     */
    public Map()
    {
        // initialise instance variables
        //Create a PrintWriter to create an Output csv
        try{
            stateOut = new PrintWriter(new File("outputStates.csv"));
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }

        Random random = new Random();
        for(int i = 0; i < 4; i++){
            int add = random.nextInt(4);
            while(order.contains(add))
                add = random.nextInt(4);

            order.add(add);
        }

        frame = new JFrame("Dwarf Map");

        gridSize = 20;
        mapSize = 800;

        this.setSize(mapSize, mapSize);

        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        gameOver = false;
    }

    /**
     * Gets the money on the map
     * 
     * @return the money
     */ 
    public int getMoney(){
        return money;
    }

    /**
     * Decreases the money on the map by a given value
     * 
     * @param amount the amount the money is decreased by
     * 
     */
    public void decreaseMoney(int amount){
        money -= amount;
    }

    /**
     * Returns the list of Dwarves in the game
     * 
     * @return the list
     * 
     */
    public ArrayList<Dwarf> getDwarvesInGame(){
        return dwarvesInGame;
    }

    /**
     * Tells how much gold is stored in the home location
     * 
     * @return the amount of gold
     */
    public int getGoldCollected(){
        return goldCollected;
    }

    /**
     * Tells how much gold was collected but lost
     * 
     * @return the amount of gold
     */
    public int getGoldLost(){
        return goldLost;
    }

    /**
     * Tells how many Dwarves have died
     * 
     * @return the number of dwarves
     */
    public int getDeadDwarves(){
        return deadDwarves;
    }

    /**
     * Tells how much gold was missed
     * 
     * @return the amount of gold
     */
    public int getGoldMissed(){
        return totalGoldOnMap - (goldCollected + goldLost);
    }

    /**
     * Sets up the 2d array with the correct terrain types, and number of dwarves
     * 
     * @param numberWalkers the number of SmartDwarves in the game
     * @param numberMiners the number of MiningDwarves in the game
     * @param numberBridgeBuilders the number of BridgeBuilders in the game
     * @param numberYellers the number of YellerDwarves in the game
     * @param userInput tells whether or not to use a user input
     */
    public void setMap4(int numberWalkers, int numberMiners, int numberBridgeBuilders, int numberYellers, boolean userInput){
        try{
            //Set the parameters of map 
            playField = new Location[30][30];  

            Scanner scanner = new Scanner(new FileReader("testMap.prn"));
            //Read from the Excel file and create the map accordingly
            for(int i = 0; i < playField.length; i++){
                for(int j = 0; j < playField[i].length; j++){
                    String next = scanner.next();
                    if(next.equals("H")){
                        playField[j][i] = new Location(new Home(), j, i);
                    }else if(next.equals("Ro")){
                        playField[j][i] = new Location(new Rock(), j, i);
                    }else if(next.equals("G")){
                        playField[j][i] = new Location(new Gold(), j, i);
                    }else if(next.equals("P")){
                        playField[j][i] = new Location(new Pit(), j, i);
                    }else if(next.equals("L")){
                        playField[j][i] = new Location(new Lava(), j, i);
                    }else if(next.equals("Rl")){
                        playField[j][i] = new Location(new River("left"), j, i);
                    }else if(next.equals("Rd")){
                        playField[j][i] = new Location(new River("down"), j, i);
                    }else if(next.equals("Rr")){
                        playField[j][i] = new Location(new River("right"), j, i);
                    }else if(next.equals("Ru")){
                        playField[j][i] = new Location(new River("up"), j, i);
                    }else{
                        playField[j][i] = new Location(new Rock(), j, i);
                    }

                }
            }

            Random random = new Random();

            //Calculate the total gold on the map before the game starts
            for(int i = 0; i < playField.length; i++){
                for(int j = 0; j < playField[i].length; j++){
                    if(playField[i][j].getTerrain() instanceof Gold){
                        Gold tempGold = (Gold)playField[i][j].getTerrain();

                        totalGoldOnMap += tempGold.getGoldContained();
                    }
                }
            }

            //Has one test Dwarf to run around and have fun in the sun
            //This will be expanded to be an array or list or something fun
            for(int i = 0; i < numberYellers * 5; i++){
                dwarvesInGame.add(new YellerDwarf(playField));
            }
            for(int i = 0; i < numberWalkers * 15; i++){
                dwarvesInGame.add(new SmartDwarf(playField));
            }
            for(int i = 0; i < numberMiners * 15; i++){
                dwarvesInGame.add(new MiningDwarf(playField));
            }
            for(int i = 0; i < numberBridgeBuilders * 3; i++){
                dwarvesInGame.add(new BridgeBuilder(playField));
            }

            for(int i = 0; i < dwarvesInGame.size(); i++){
                money -= dwarvesInGame.get(i).getValue();
            }

            //Print onto the Excel
            for(int i = 0; i < dwarvesInGame.size(); i++){
                stateOut.write("Dwarf " + (int)dwarvesInGame.get(i).getMyId() + ",");
            }
            stateOut.write("\n");

            if(userInput == false)
                money = 0;

            //Start all Dwarves to start in the top left
            for(int i = 0; i < dwarvesInGame.size(); i++){
                Location tempLoc = playField[0][0];
                dwarvesInGame.get(i).setLocation(tempLoc);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Return a Location at a particular location
     *
     * @return the Location at a particular spot on the map
     */
    public Location getLocation(int x, int y){
        return playField[x][y];
    }

    /**
     * Updates the map locations and dwarves by one tick
     * 
     */
    public void testRun(){
        ticks++;

        //Update the map in order

        for(int i = 0; i < playField.length; i++){
            for(int j = 0; j < playField[i].length; j++){

                //Go through each Dwarf in the list
                for(int k = 0; k < dwarvesInGame.size(); k++){

                    //If you are standing on a point that the map doesn't know yet, put yourself there
                    if(dwarvesInGame.get(k).getCurLocation().equals(playField[i][j]) && !playField[i][j].contains(dwarvesInGame.get(k))){
                        playField[i][j].addDwarf(dwarvesInGame.get(k));

                    }

                }
                //If a location is rock, and it is broken, it turns into tunnel
                if(playField[i][j].getTerrain() instanceof Rock){
                    Rock tempRock = (Rock)playField[i][j].getTerrain();

                    if(tempRock.getState().equals("broken")){
                        Tunnel newTunnel = new Tunnel();
                        playField[i][j].setTerrainType(newTunnel);
                    }

                }

                //If a location is gold and is done, it turns into tunnel
                if(playField[i][j].getTerrain() instanceof Gold){
                    Gold tempGold = (Gold)playField[i][j].getTerrain();

                    if(tempGold.getState().equals("done")){
                        Tunnel newTunnel = new Tunnel();
                        playField[i][j].setTerrainType(newTunnel);
                    }

                }

                //If the location has had gold added, reflect it in the score
                if(playField[i][j].getTerrain() instanceof Home){
                    Home tempHome = (Home)playField[i][j].getTerrain();
                    int curGold = goldCollected;
                    goldCollected = tempHome.getGoldContained();                    

                    money += ((goldCollected - curGold) * VALUE_OF_GOLD);
                }
                //If the Dwarf has moved away, remove them from your list
                for(int k = 0; k < dwarvesInGame.size(); k++){
                    if(!dwarvesInGame.get(k).getCurLocation().equals(playField[i][j]) && playField[i][j].contains(dwarvesInGame.get(k))){
                        playField[i][j].remove(dwarvesInGame.get(k));
                    }
                }
            }

        }

        for(int i = 0; i < dwarvesInGame.size(); i++){
            dwarvesInGame.get(i).run();

            stateOut.write(dwarvesInGame.get(i).getState() + ",");

            //See if you've stood still
            if(dwarvesInGame.get(i).getXLoc() == dwarvesInGame.get(i).getPrevXLoc() && dwarvesInGame.get(i).getYLoc() == dwarvesInGame.get(i).getPrevYLoc()){
                dwarvesInGame.get(i).standingStill();

            }else{
                dwarvesInGame.get(i).resetStandingStill();

            }

            //If the dwarf is dead, delete all traces of their existance
            if(dwarvesInGame.get(i).isDead()){
                deadDwarves++;
                //If you've died, your Gold is LOST
                goldLost += dwarvesInGame.get(i).getGoldStored();

                //Pay the family 10 times your worth
                money -= dwarvesInGame.get(i).getValue() * 10;

                for(int j = 0; j < playField.length; j++){
                    for(int k = 0; k < playField[j].length; k++){
                        if(playField[j][k].contains(dwarvesInGame.get(i))){
                            playField[j][k].remove(dwarvesInGame.get(i));
                        }

                    }
                }

                dwarvesInGame.remove(i);
            }

            //If a miner has lost their armor, take them off the map
            if(i < dwarvesInGame.size() && dwarvesInGame.get(i) instanceof MiningDwarf){
                MiningDwarf tempDwarf = (MiningDwarf)dwarvesInGame.get(i);
                if(tempDwarf.doneWorking()){
                    for(int j = 0; j < playField.length; j++){

                        for(int k = 0; k < playField[j].length; k++){
                            if(playField[j][k].contains(dwarvesInGame.get(i))){
                                playField[j][k].remove(dwarvesInGame.get(i));
                            }

                        }
                    }

                    dwarvesInGame.remove(i);
                }

            }
        }
        stateOut.write("\n");
        //If the new location you want to move to is a Rock, and you can't move to a Rock, stay where you are.
        for(int i = 0; i < dwarvesInGame.size(); i++){
            if(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Rock
            && dwarvesInGame.get(i) instanceof Dwarf && !(dwarvesInGame.get(i) instanceof MiningDwarf)){
                dwarvesInGame.get(i).setX(dwarvesInGame.get(i).getPrevXLoc());
                dwarvesInGame.get(i).setY(dwarvesInGame.get(i).getPrevYLoc());

            }
        }

        //Upadate their location on the map
        for(int i = 0; i < dwarvesInGame.size(); i++){

            if(dwarvesInGame.get(i).isDead() == false){
                if(dwarvesInGame.get(i) instanceof MiningDwarf){
                    dwarvesInGame.get(i).setLocation(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()]);
                }else if(dwarvesInGame.get(i) instanceof Dwarf && 
                (playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Tunnel
                    || playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Lava
                    || playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof River
                    || playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Pit
                    || playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Gold
                    || playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Home)){
                    if(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()].getTerrain() instanceof Rock){
                        dwarvesInGame.get(i).setLocation(playField[dwarvesInGame.get(i).getPrevXLoc()][dwarvesInGame.get(i).getPrevYLoc()]);

                    }else{
                        dwarvesInGame.get(i).setLocation(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()]);
                    }
                }

            }
            //Add this location to the dwarf's visited locations
            dwarvesInGame.get(i).addLocation(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()]);

            //If you're heading home, this is your path back to where the gold was
            if(dwarvesInGame.get(i).getState().equals("findHome")){
                dwarvesInGame.get(i).addToGoldPath(playField[dwarvesInGame.get(i).getXLoc()][dwarvesInGame.get(i).getYLoc()]);

            }
        }

    }

    /**
     * Show all items through the GUI
     * 
     * @param g the Graphics object
     */  
    public void paint(Graphics g) {
        int brickSize = mapSize / 40;

        int paintX = brickSize / 8;
        int paintY = brickSize / 8;

        for(int x = 0; x < playField[x].length; x++){
            for(int y = 0; y < playField.length; y++){

                playField[x][y].getTerrain().paint(g, brickSize, paintX, paintY);
                for(int k = 0; k < playField[x][y].getPresentDwarfs().size(); k++){
                    playField[x][y].getPresentDwarfs().get(k).paint(g, paintX, paintY, 20);
                }
                paintY += brickSize + brickSize / 8;
            }
            paintX += brickSize + brickSize / 8;
            paintY = 0;
        }

    }

    /**
     * Sees if all gold has been collected or lost
     * 
     * @return true if all gold is gone
     */  
    public boolean noMoreGold(){

        if(goldLost + goldCollected == totalGoldOnMap)
            return true;

        return false;
    }

    /**
     * Sees if there are any miners left imn the game
     * 
     * @return true if all miners are gone
     */  
    public boolean noMoreMiners(){
        for(int i = 0; i < dwarvesInGame.size(); i++){
            if(dwarvesInGame.get(i) instanceof MiningDwarf)
                return false;
        }

        return true;
    }

    /**
     * Sees if the game is over
     * 
     * @return true if the game is done
     */  
    public boolean gameIsOver(){
        if(noMoreMiners() || noMoreGold() || ticks > 1500){
            return true;

        }

        return false;
    }

}
