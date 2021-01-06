
/**
 * Holds the information of one spot of the Map (the Dwarves on the spot, and a terrain type)
 * 
 * @author Jordan Leiber
 * E-mail: leiberj@lafayette.edu
 * Course: CS 150 - Section 02
 */

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Stack;

public class Location {
    // instance variables 
    private Terrain terrain;

    //Somehow get this to have a tree of all people who have been here
    private ArrayList<Dwarf> presentDwarfs = new ArrayList<Dwarf> ();
    private TreeSet<Integer> runes = new TreeSet<Integer>();

    //Set based off of array locations
    private int xLoc;  
    private int yLoc;

    /**
     * Constructor for objects of class Location
     */
    public Location(Terrain terrain, int xLoc, int yLoc){
        // initialise instance variables
        this.terrain = terrain;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }

    /**
     * Depending on what the type of terrain is, there is a different result from Dwarves being on this point
     *
     */
    public Terrain getTerrain(){
        return terrain;
    }

    /**
     * Returns the present dwarfs on the spot
     * 
     * @return the ArrayList of Dwarfs
     */
    public ArrayList<Dwarf> getPresentDwarfs(){
        return presentDwarfs;
    }

    /**
     * Sets the present dwarfs to a new list
     * 
     * @param newDwarves the list of Dwarves to replace the old one
     */
    public void setPresentDwarfs(ArrayList<Dwarf> newDwarves){
        presentDwarfs = newDwarves;
    }

    /**
     * Sets the Terrain type
     * 
     * @param terrain the wanted terrain
     */
    public void setTerrainType(Terrain terrain){
        this.terrain = terrain;
    }

    /**
     * Adds the Id of a Dwarf to the Location's tree
     * 
     * @param dwarfId The Dwarf's ID
     */
    public void addRune(int dwarfId){
        runes.add(dwarfId);
    }

    /**
     * Returns the TreeSet of runes that have been left on the location
     * 
     * @return The TreeSet
     */
    public TreeSet getRuneTree(){
        return runes;
    }

    /**
     * Adds a dwarf to the location
     * 
     * @param inDwarf the Dwarf being added
     */
    public void addDwarf(Dwarf inDwarf){
        presentDwarfs.add(inDwarf);
    }

    /**
     * Finds if a Dwarf is contained in a location
     * 
     * @param inDwarf the Dwarf being checked
     * 
     * @return true if the Dwarf is in the location
     */
    public boolean contains(Dwarf inDwarf){
        //Go through each of the Location's Dwarves
        for(int i = 0; i < presentDwarfs.size(); i++){
            //If the current Dwarf is the one you're searching for
            if(presentDwarfs.get(i).equals(inDwarf)){
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a dwarf from the location
     * 
     * @param inDwarf the dwarf to be removed
     */
    public void remove(Dwarf inDwarf){
        //Go through each of the Location's Dwarves
        for(int i = 0; i < presentDwarfs.size(); i++){

            //If the current Dwarf is the one you're looking for
            if(presentDwarfs.get(i).getMyId() == inDwarf.getMyId()){
                presentDwarfs.remove(i);
            }
        }
    }

    /**
     * Get the x location of this Location
     * 
     * @return the x coordinate
     */  
    public int getX(){
        return xLoc;
    }

    /**
     * Get the y location of this Location
     * 
     * @return the y coordinate
     */  
    public int getY(){
        return yLoc;
    }

    /**
     * Update the Dwarves present at this location, as well as the terrain, and list of Dwarves who have been on this location
     *
     */
    public void run(){
        //Go through each Dwarf in the Location
        for(int i = 0; i < presentDwarfs.size(); i++){
            //If you are a Lava Location remove the Dwarf
            if(terrain instanceof Lava){
                Lava lava = (Lava)terrain;
                lava.run(presentDwarfs.get(i));
                presentDwarfs.remove(i);
            }
        }
    }
}
