
/**
 * The Clock class steps the simulation at a time. Clock ensures that events happen when they are supposed to. 
 *
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Clock {
    // instance variables
    protected static int ticks = 0;
    private int seed;
    protected int TICKS_IN_DAY = 720;

    Map map = new Map();

    private boolean useGraphics = false;

    private int goldCollectedRound;
    private int goldLostRound;
    private int goldLeftEndOfRound;
    private int deadDwarves;
    private int finalScore;
    private int gameTicks;
    private Dwarf mostProductive;

    public static void main(String[] args){
        Clock testClock = new Clock();

        testClock.runMap4();
    }

    /**
     * Constructor for objects of class Clock
     */
    public Clock(){
        // initialise instance variables
        seed = 9868;

    }

    /**
     * Moves the clock ahead by one tick
     * 
     */
    public void clockTick(){
        ticks++;
    }

    /**
     * Runs map 4 for a user to input their own values
     * 
     */    
    public void runMap4(){
        Scanner scanner = new Scanner(System.in);
        int budget = 10000;

        int numberSmartDwarves, numberMiners, numberBridgeBuilders, numberYellers;
        System.out.println("Welcome to Dwarf Miner Tycoon! You have a budget of: " + budget);

        System.out.println("Would you like to use graphics? (Y/N)");
        String answer = scanner.next();
        //Choose whether or not to use the GUI
        if(answer.equalsIgnoreCase("Y")){
            useGraphics = true;
        }else{
            useGraphics = false;
        }

        System.out.println("There are 4 Dwarf types to choose from: ");
        System.out.println("1: The Walker: The Walker wanders around the field of play in search of broken up Gold. " + "\nIn the event that the Walker finds broken Gold, it will return it back to the home base.");
        System.out.println("The cost of a team of 15 Walkers is 250");

        System.out.println("2: The Miner: The Miner marches through the field of play turning Rock into traversable path. \nThe Miner also breaks up Gold block so it can be collected by others.");
        System.out.println("The cost of a team of 15 Miners is 350");

        System.out.println("3: The Bridge Builder: The Bridge Builder can turn danger into safety. When stumbling upon a River or Pit, the Bridge Builder will make it safe to cross.");
        System.out.println("Unfortionately...Lava is still...hot, and cannot be built over. Any Dwarf in Lava WILL burn");

        System.out.println("The cost of a team of 3 Brige Buiders is 450");

        System.out.println("4: The Yeller: The Yeller is the only line of protection against danger. With enhanced sight in the darkness of the mine, the Yeller can see all danger in the map when standing next to it.");
        System.out.println("Once a danger location is found, the Yeller alerts all other miners in a 9 square area of that danger.");
        System.out.println("Unless they wish to be free of this indentured servitude, the Dwarves will avoid these locations.");

        System.out.println("The cost of a team of 4 Yellers is 400");

        System.out.println("Money remaining: " + map.getMoney());
        System.out.println("How many teams of Walkers at 250 each? You can buy up to: " + (budget / 250));
        numberSmartDwarves = scanner.nextInt();
        budget -= numberSmartDwarves * 250;

        //If the user tries to spend too much, tell them to try again...and pray they don't do it again
        if(budget < 0){
            budget += numberSmartDwarves * 250;
            System.out.println("Please only spend money that you have. Try again. You can get up to: " + (budget / 250));
            System.out.println("Money remaining: " + map.getMoney());
            System.out.println("How many teams of Walkers at 250 each?");
            numberSmartDwarves = scanner.nextInt();
        }
        map.decreaseMoney(numberSmartDwarves * 250);

        System.out.println("Money remaining: " + map.getMoney());
        System.out.println("How many teams of Miners at 350 each? You can buy up to: " + (budget / 350));
        numberMiners = scanner.nextInt();
        budget -= numberMiners * 350;
        if(budget < 0){
            budget += numberMiners * 350;
            System.out.println("Please only spend money that you have. Try again" );
            System.out.println("Money remaining: " + map.getMoney());
            System.out.println("How many teams of Miners at 350 each? You can buy up to: " + (budget / 350));
            numberMiners = scanner.nextInt();
        }
        map.decreaseMoney(numberMiners * 350);

        System.out.println("Money remaining: " + map.getMoney());   
        System.out.println("How many teams of Yellers at 400 each? You can buy up to: " + (budget / 400));
        numberYellers = scanner.nextInt();
        budget -= numberYellers * 400;
        if(budget < 0){
            budget += numberYellers * 400;
            System.out.println("Please only spend money that you have. Try again");
            System.out.println("Money remaining: " + map.getMoney());
            System.out.println("How many teams of Yellers at 400 each? You can buy up to: " + (budget / 400));
            numberYellers = scanner.nextInt();
        }
        map.decreaseMoney(numberYellers * 400);

        System.out.println("Money remaining: " + map.getMoney());
        System.out.println("How many teams of Bridge Builders at 450 each? You can buy up to: " + (budget / 450));
        numberBridgeBuilders= scanner.nextInt();
        budget -= numberBridgeBuilders * 450;
        if(budget < 0){
            budget += numberBridgeBuilders * 450;
            System.out.println("Please only spend money that you have. Try again");
            System.out.println("Money remaining: " + map.getMoney());
            System.out.println("How many teams of Bridge Builders at 450 each? You can buy up to: " + (budget / 450));
            numberBridgeBuilders = scanner.nextInt();
        }
        map.decreaseMoney(numberBridgeBuilders * 450);

        map.setMap4(numberSmartDwarves, numberMiners, numberBridgeBuilders, numberYellers, true);
        try {
            //If the user wants to use graphics
            if(useGraphics){
                //Run until the game ends
                while(!map.gameIsOver()){
                    map.repaint();
                    map.testRun();
                    ticks++;
                    TimeUnit.SECONDS.sleep(1);

                    map.repaint();
                    System.out.println("Ticks: " + ticks);
                    System.out.println("Score: " + map.getMoney());
                    int max = 0;
                    //Update the most productive Dwarf
                    if(map.getDwarvesInGame().size() > 0){
                        for(int i = 0; i < map.getDwarvesInGame().size(); i++){
                            Dwarf tempDwarf = map.getDwarvesInGame().get(i);
                            if(tempDwarf.getProductivity() > max){
                                max = tempDwarf.getProductivity();
                                mostProductive = tempDwarf;
                            }
                        }

                    }else{
                        mostProductive = null;
                    }
                    //If the most productive Dwarf is null make it a Miner
                    if(mostProductive == null)
                        mostProductive = new MiningDwarf(new Location[30][30]);
                    System.out.println("Most Productive Dwarf: " + mostProductive.getMyId() + " Type: " + mostProductive.getType());

                }

                //If you don't want to use graphics
            }else{
                //Run the game until it ends
                while(!(map.gameIsOver())){
                    map.testRun();
                    ticks++;
                }

                //Print the final results of the game
                System.out.println("Score: " + map.getMoney());
                System.out.println("Dwarves remaining: " + map.getDeadDwarves());

                int max = 0;
                if(map.getDwarvesInGame().size() > 0){
                    for(int i = 0; i < map.getDwarvesInGame().size(); i++){
                        Dwarf tempDwarf = map.getDwarvesInGame().get(i);
                        if(tempDwarf.getProductivity() > max){
                            max = tempDwarf.getProductivity();
                            mostProductive = tempDwarf;
                        }
                    }

                }else{
                    mostProductive = null;
                }

                if(mostProductive == null)
                    mostProductive = new MiningDwarf(new Location[30][30]);
                System.out.println("Most Productive Dwarf: " + mostProductive.getMyId() + " Type: " + mostProductive.getType());

            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }   
        System.out.println("Ticks: " + ticks);
        ticks = 0;
    }

    /**
     * Runs map 4 for testing purposes
     * 
     */    
    public void runMap4Test( int numberSmartDwarves, int numberMiners, int numberBridgeBuilders, int numberYellers){
        Scanner scanner = new Scanner(System.in);

        useGraphics = false;
        map.setMap4(numberSmartDwarves, numberMiners, numberBridgeBuilders, numberYellers, false);
        //Run the game until it is over
        while(!(map.gameIsOver())){
            map.testRun();
            ticks++;
        }

        //Set all values that will be put into the Excel file
        goldCollectedRound = map.getGoldCollected();
        goldLostRound = map.getGoldLost();
        goldLeftEndOfRound = map.getGoldMissed();
        deadDwarves = map.getDeadDwarves();
        finalScore = map.getMoney();
        int max = 0;
        if(map.getDwarvesInGame().size() > 0){
            for(int i = 0; i < map.getDwarvesInGame().size(); i++){
                Dwarf tempDwarf = map.getDwarvesInGame().get(i);
                if(tempDwarf.getProductivity() > max){
                    max = tempDwarf.getProductivity();
                    mostProductive = tempDwarf;
                }
            }

        }else{
            mostProductive = null;
        }
        if(mostProductive == null)
            mostProductive = new MiningDwarf(new Location[30][30]);

        gameTicks = ticks;  

        ticks = 0;
    }

    /**
     * Returns the number of Dwarves who died in a round
     * 
     * @return the number of dead Dwarves
     * 
     */ 
    public int getDeadDwarves(){
        return deadDwarves;
    }

    /**
     * Returns the amount of gold collected in a round
     * 
     * @return the amount of gold
     * 
     */ 
    public int getGoldCollectedRound(){
        return goldCollectedRound;
    }

    /**
     * Returns the amount of gold lost in a round
     * 
     * @return the amount of gold
     * 
     */ 
    public int getGoldLostRound(){
        return goldLostRound;
    }

    /**
     * Returns the amount of gold not collected or lost in a round
     * 
     * @return the amount of gold
     * 
     */ 
    public int getGoldMissed(){
        return goldLeftEndOfRound;
    }

    /**
     * Returns the final score of a round
     * 
     * @return the score
     * 
     */ 
    public int getFinalScore(){
        return finalScore;
    }

    /**
     * Returns the amount of time a round ran
     * 
     * @return the number of ticks
     * 
     */ 
    public int getTicks(){
        return gameTicks;
    }

    /**
     * Returns the type of Dwarf the most productive Dwarf was
     * 
     * @return the type
     */ 
    public String getMostProductive(){
        if(mostProductive != null)
            return mostProductive.getType();

        return "null";
    }

}
