import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a tortoise.
 * Tortoises age, move, eat plants, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Tortoise extends Animal
{
    // Characteristics shared by all tortoises (class variables).
    
    // The age at which a tortoise can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a tortoise can live.
    private static final int MAX_AGE = 90;
    // The likelihood of a tortoise breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 10;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a tortoise
    private static final double TORTOISE_FOOD_VALUE = 0.3;
    // Base starting food level for all tortoises
    private static final int BASIC_FOOD_LEVEL = 20;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    /**
     * Create a tortoise. A tortoise can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the tortoise will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    protected Tortoise(boolean randomAge, Field field, Location location, boolean overlap)
    {
        super(field, location, overlap);
        setFoodValue(TORTOISE_FOOD_VALUE);
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
     * This is what the tortoise does during the day: it hunts for
     * plants. In the process, it might breed, die of hunger,
     * or die of old age.
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    public void dayAct(List<Actor> newTortoises)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        if(isAlive()) {
            giveBirth(newTortoises);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
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
     * This is what the tortoise does during the night: Sleep
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    public void nightAct(List<Actor> newTortoises)
    {
    }
    
    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object actor = field.getObjectAt(where);
            if(actor instanceof Plant) {
                if (rand.nextDouble() <= EATING_PROBABILITY){
                    Plant plant = (Plant) actor;
                    if(plant.isAlive()) { 
                        plant.setDead();
                        foodLevel += plant.getFoodValue();
                        return where;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this tortoise is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    private void giveBirth(List<Actor> newTortoises)
    {
        // New tortoises are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tortoise young = new Tortoise(false, field, loc, false);
            newTortoises.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @param field The field the tortoise is currently in
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
