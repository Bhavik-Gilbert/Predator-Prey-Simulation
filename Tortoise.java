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
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 10;
    // The food value of a single plant. In effect, this is the
    // number of steps a tortoise can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 10000;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The tortoise's age.
    private int age;
    // The tortoise's food level, which is increased by eating plants.
    private int foodLevel;

    /**
     * Create a tortoise. A tortoise can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the tortoise will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Tortoise(boolean randomAge, Field field, Location location, boolean overlap)
    {
        super(field, location, overlap);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
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
        incrementAge();
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
     * This is what the tortoise does during the night
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    public void nightAct(List<Actor> newTortoises)
    {
    }

    /**
     * Increase the age. This could result in the tortoise's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this tortoise more hungry. This could result in the tortoise's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
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
                Plant plant = (Plant) actor;
                if(plant.isAlive()) { 
                    plant.setDead();
                    foodLevel += PLANT_FOOD_VALUE;
                    return where;
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
