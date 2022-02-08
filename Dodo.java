import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a dodo.
 * Dodos age, move, reproduce, eat plants, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Dodo extends Animal
{
    // Characteristics shared by all Dodos (class variables).
    
    // The age at which a dodo can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a dodo can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a dodo breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single dodo. In effect, this is the
    // number of steps a dodo can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 10000;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The dodo's age.
    private int age;
    // The dodo's food level, which is increased by eating foxes.
    private int foodLevel;

    /**
     * Create a dodo. A dodo can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the dodo will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Dodo(boolean randomAge, Field field, Location location)
    {
        super(field, location);
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
     * Determines whether night or day.
     * 
     * @param newDodos  A list to return newly born human.
     * @param timeOfDay Integer value determining day or night
     */
    public void act(List<Actor> newDodos, int timeOfDay)
    {
        switch(timeOfDay){
            case 0:
                dayAct(newDodos);
            case 1:
                nightAct(newDodos);
        }
    }
    
    /**
     * This is what the dodo does most of the time: it looks for plants
     * In the process, it might breed, die of hunger, or die of old age.
     * 
     * @param newDodos A list to return newly born Dodos.
     */
    public void dayAct(List<Actor> newDodos)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newDodos);            
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
     * This is what the dodos do during the night
     * @param newDodos A list to return newly born Dodos.
     */
    public void nightAct(List<Actor> newDodos)
    {
    }

    /**
     * Increase the age. This could result in the dodo's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this dodo more hungry. This could result in the dodo's death.
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
     * Check whether or not this dodo is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDodos A list to return newly born Dodos.
     */
    private void giveBirth(List<Actor> newDodos)
    {
        // New Dodos are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Dodo young = new Dodo(false, field, loc);
            newDodos.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @param field The field the dodo is currently in
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
