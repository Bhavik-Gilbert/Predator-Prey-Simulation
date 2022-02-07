 

import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a racoon.
 * Racoons age, move, eat plants, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Racoon extends Animal
{
    // Characteristics shared by all racoons (class variables).
    
    // The age at which a racoon can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a racoon can live.
    private static final int MAX_AGE = 25;
    // The likelihood of a racoon breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single plant. In effect, this is the
    // number of steps a racoon can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 10000;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The racoon's age.
    private int age;
    // The racoon's food level, which is increased by eating plants.
    private int foodLevel;

    /**
     * Create a racoon. A racoon can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the racoon will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Racoon(boolean randomAge, Field field, Location location)
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
     * This is what the racoon does most of the time: it hunts for
     * plants. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newRacoons A list to return newly born racoons.
     */
    public void act(List<Animal> newRacoons)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newRacoons);            
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
     * Increase the age. This could result in the racoon's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this racoon more hungry. This could result in the racoon's death.
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
            Object animal = field.getObjectAt(where);
            if(animal instanceof Plant) {
                Plant plant = (Plant) animal;
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
     * Check whether or not this racoon is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRacoons A list to return newly born racoons.
     */
    private void giveBirth(List<Animal> newRacoons)
    {
        // New racoons are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Racoon young = new Racoon(false, field, loc);
            newRacoons.add(young);
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
     * A racoon can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
