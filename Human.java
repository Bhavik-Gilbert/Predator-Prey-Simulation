 //ate rabbits and racoons

import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a human.
 * Human age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Human extends Animal
{
    // Characteristics shared by all human (class variables).
    
    // The age at which a human can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a human can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a human breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 6;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a human can go before it has to eat again.
    private static final int PIG_FOOD_VALUE = 10;
    private static final int DODO_FOOD_VALUE = 10;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The human's age.
    private int age;
    // The human's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a human. A human can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the human will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Human(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PIG_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PIG_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the human does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHuman A list to return newly born human.
     */
    public void act(List<Actor> newHuman)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHuman);            
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
     * Increase the age. This could result in the human's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this human more hungry. This could result in the human's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Pig) {
                Pig pig = (Pig) animal;
                if(pig.isAlive()) { 
                    pig.setDead();
                    foodLevel += PIG_FOOD_VALUE;
                    return where;
                }
            }
            if (animal instanceof Dodo) {
                Dodo dodo = (Dodo) animal;
                if (dodo.isAlive()) {
                    dodo.setDead();
                    foodLevel = DODO_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this human is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHuman A list to return newly born human.
     */
    private void giveBirth(List<Actor> newHuman)
    {
        // New human are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Human young = new Human(false, field, loc);
            newHuman.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A human can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
