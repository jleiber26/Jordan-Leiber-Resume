
/**
 * Runs several rounds, and print the output to an Excel file
 * 
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ExperimentController
{
    // instance variables 
    private PrintWriter output;
    private Scanner input;
    private String data;
    private File file;
    private Clock testClock = new Clock();

    /**
     * Constructor for objects of class ExperimentController
     */
    public ExperimentController()
    {
        // initialise instance variables
        file = new File("input.txt");
        //Create a scanner to read the input data
        try{
            input = new Scanner(file);

        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        //Create a PrintWriter to create an Output csv
        try{
            output = new PrintWriter(new File("output.csv"));
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    /**
     * Runs tests, and outputs the information onto the output file
     *
     */
    public void runExperiment(){
        ArrayList<Integer> numberWalkers = new ArrayList<Integer>();
        ArrayList<Integer> numberMiners = new ArrayList<Integer>();
        ArrayList<Integer> numberBuilders = new ArrayList<Integer>();
        ArrayList<Integer> numberYellers = new ArrayList<Integer>();

        //While the input file still has more lines, add the different data types to the different arrays
        while(input.hasNext()){
            numberWalkers.add(Integer.parseInt(input.next()));
            numberMiners.add(Integer.parseInt(input.next()));
            numberBuilders.add(Integer.parseInt(input.next()));
            numberYellers.add(Integer.parseInt(input.next()));
        }

        int i = 0;
        int j = 0;

        //Print headers
        //Run all experiments 3 times
        while(i < 3){
            output.write("Trial" + i + ",Walkers," + "Miners," + "Builders," + "Yellers," + "Gold Collected," + "Gold Lost," + "Gold Missed," + "Dead Dwarves," + "Score, " + "Ticks," + "Productivity,");

            //Print out the desired information at the end of each run
            while(j < numberWalkers.size()){
                Clock clock = new Clock();
                clock.runMap4Test(numberWalkers.get(j), numberMiners.get(j), numberBuilders.get(j), numberYellers.get(j));
                output.write("\n" + "Experiment " + j + "," + numberWalkers.get(j) * 15 + "," + numberMiners.get(j) * 15+ "," + numberBuilders.get(j) * 3+ "," + numberYellers.get(j) * 5+ "," + clock.getGoldCollectedRound() + "," + clock.getGoldLostRound() + "," + clock.getGoldMissed() + "," + clock.getDeadDwarves() + "," + clock.getFinalScore() + "," + clock.getTicks() + "," + clock.getMostProductive());

                j++;
            }
            i++;
            j = 0;
            output.write("\n");
        }

        output.close();
    }

}
