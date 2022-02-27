import java.util.*;

/**
 * A simple model of a plant.
 * Plants age, move, breed, and die.
 * 
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public class Plant extends Actor
{
    // Characteristics shared by all plants (class variables).

    // The age at which a plant can start to breed.
    private static final int BREEDING_AGE = 0;
    // The age to which a plant can live.
    private static final int MAX_AGE = 2;
    // The likelihood of a plant breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a plany
    private static final int PLANT_FOOD_VALUE = 7;
    // A map containing values that can effect how the plant reacts based off of the weather
    private EnumMap<WeatherEffectTypes, Double> weatherEffect = new EnumMap<>(WeatherEffectTypes.class);
    
    // Individual characteristics (instance fields).
    
    // The plant's age.
    private int age;

    /**
     * Create a new plant. A plant may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the plant will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param overlap   Whether or not an actor is allowed to overlap with other actors
     */
    public Plant(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setOverlap(true);
        age = 0;

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * This is what the plant does during the day. 
     * Sometimes it will breed or die of old age.
     * @param newPlants A list to return newly born plants.
     */
    public void dayAct(List<Actor> newPlants)
    {
        incrementAge(MAX_AGE);
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
     * Increase the age. This could result in the plants's death.
     */
    protected void incrementAge(int MAX_AGE) {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Retrieves and sets the current weather effect values
     */
    protected void setWeatherEffects(){
        weatherEffect = WeatherAction.weatherOnPlants(weather);
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
                Plant young = new Plant(false, field, loc);
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
        int births = 0;

        if(canBreed() && rand.nextDouble() <= (BREEDING_PROBABILITY * effectBreedingProbability())) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Retrieves the breeding probability modifier
     * Calculated by time of day and current weather conditions
     * 
     * @return Breeding probability modifier
     */
    private double effectBreedingProbability(){
        double dayEffect;

        if (getDay()) {
            dayEffect = 1;
        } else {
            dayEffect = 0.01;
        }

        return dayEffect * weatherEffect.get(WeatherEffectTypes.BREED);
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

    /**
     * Calculates the number of steps gained for eating this plant
     * 
     * @return The number of steps gained for eating this plant
     */
    public double getFoodValue() {
        return age * PLANT_FOOD_VALUE;
    }
}
