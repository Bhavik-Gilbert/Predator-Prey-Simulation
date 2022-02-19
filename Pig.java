import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of a pig.
 * Pigs age, move, eat dodos, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Pig extends Animal
{
    // Characteristics shared by all pigs (class variables).
    
    // The age at which a pig can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a pig can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a pig breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 9;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a pig
    private static final double PIG_FOOD_VALUE = 0.6;
    // Base starting food level for all pigs
    private static final int BASIC_FOOD_LEVEL = 20;
    // Probability that a pig dies from disease.
    protected static final double DEATH_FROM_DISEASE_PROBABILITY = 0.02;
    // List of all pig prey.
    protected static List<String> LIST_OF_PREY;

    /**
     * Create a pig. A pig can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the pig will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    protected Pig(String description, boolean randomAge, Field field, Location location, boolean overlap, boolean infected)
    {
        super(description, field, location, overlap, infected);
        LIST_OF_PREY = new ArrayList<>();
        LIST_OF_PREY.add("Dodo");
        setFoodValue(PIG_FOOD_VALUE);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(BASIC_FOOD_LEVEL);
        }
        else {
            age = 0;
            foodLevel = BASIC_FOOD_LEVEL;
        }
    }
    
    /**
     * This is what the pig does during the day: it hunts for dodos. 
     * In the process, it might breed, die of hunger, or die of old age.
     * 
     * @param newPigs A list to return newly born pigs.
     */
    public void dayAct(List<Actor> newPigs)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        super.infection();
        if(isAlive()) {
            giveBirth(newPigs);            
            // Move towards a source of food if found.
            Location newLocation = super.findFood(LIST_OF_PREY);
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * This is what the pig does during the 
     * It sleeps
     * 
     * @param newPigs A list to return newly born pigs.
     */
    public void nightAct(List<Actor> newPigs)
    {
        super.infection();
    }
    
    /**
     * Check whether or not this pig is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newPigs A list to return newly born pigs.
     */
    private void giveBirth(List<Actor> newPigs)
    {
        // New pigs are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Pig young = new Pig("Pig", false, field, loc, false, false);
            newPigs.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births, 
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed(Field field)
    {
        int births = 0;
        if(canBreed(field, BREEDING_AGE, age) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

}
