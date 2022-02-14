 import java.util.List;
import java.util.Random;

/**
 * A simple model of a plant.
 * Plants age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Plant extends Actor
{
    // Characteristics shared by all plants (class variables).

    // The age at which a plant can start to breed.
    private static final int BREEDING_AGE = 0;
    // The age to which a plant can live.
    private static final int MAX_AGE = 2;
    // The likelihood of a plant breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // TEMP
    // The number of alive plants at a given time
    private static int objectCount = 0;
    
    // Individual characteristics (instance fields).
    
    // The plant's age.
    private int age;

    /**
     * Create a new plant. A plant may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the plant will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(boolean randomAge, Field field, Location location, boolean overlap)
    {
        super(field, location, overlap);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }

        incrementPlantCount();
    }
    
    /**
     * TEMP
     *  Increments the number of plants alive by 1
     */
    private void incrementPlantCount(){
        objectCount++;
    }

    /**
     * TEMP
     * Decrements the number of plants alive by 1
     */
    private void decrementPlantCount() {
        objectCount--;
    }

    /**
     * Returns the number of plants currently alive
     */
    public static int getPlantCount() {
        return objectCount;
    }
    
    /**
     * This is what the plant does during the day. 
     * Sometimes it will breed or die of old age.
     * @param newPlants A list to return newly born plants.
     */
    public void dayAct(List<Actor> newPlants)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newPlants);            
        }
    }
    
    /**
     * This is what the plant does during the night
     * It may breed at a decreased rate
     * 
     * @param newPlants A list to return newly born plants.
     */
    public void nightAct(List<Actor> newPlants)
    {
        if (isAlive()) {
            giveBirth(newPlants);
        }
    }

    /**
     * Increase the age.
     * This could result in the plant's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    @Override
    public void setDead() {
        alive = false;
        decrementPlantCount();
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newPlants A list to return newly born plants.
     */
    private void giveBirth(List<Actor> newPlants)
    {
        // New plants are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            // Avoids overcrowding of the same type 
            if(!(field.getObjectAt(free.get(0)) instanceof Plant)){
                Location loc = free.remove(0);
                Plant young = new Plant(false, field, loc, true);
                newPlants.add(young);
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int effect;
        if(day){
            effect = 1;
        }
        else{
            effect = 10;
        }

        int births = 0;
        if(canBreed() && rand.nextDouble() <= (BREEDING_PROBABILITY / effect)) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A plant can breed if it has reached the breeding age.
     * 
     * @return true if the plant can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
