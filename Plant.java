import java.util.List;

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
    private static final int MAX_AGE = 3;
    // The likelihood of a plant breeding.
    private static final double ORIGINAL_BREEDING_PROBABILITY = 0.4;
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a tortoise
    private static final double PLANT_FOOD_VALUE = 5;
    
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
    public Plant(String description, boolean randomAge, Field field, Location location, boolean overlap)
    {
        super(description, field, location, overlap);
        BREEDING_PROBABILITY = ORIGINAL_BREEDING_PROBABILITY;
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
                Plant young = new Plant("Plant", false, field, loc, true);
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
        if(getDay()){
            effect = 1;
        }
        else{
            effect = 100;
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

    public int getAge(){
        return age;
    }

    public double getFoodValue() {
        return age * PLANT_FOOD_VALUE;
    }
    
    public static void setBreedingProbability(double breedingProbability){
        BREEDING_PROBABILITY = breedingProbability;
    }
    
    public static double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Implement the effects of the weather.
     * @param weather The weather.
     */
    public static void implementWeather(Weather weather)
    {
        switch (weather) {
            case SUNNY:
                if (BREEDING_PROBABILITY == ORIGINAL_BREEDING_PROBABILITY){
                    System.out.println("plant breeding probability not yet changed " + BREEDING_PROBABILITY);
                    WeatherAction.sunOnPlants(BREEDING_PROBABILITY);
                    System.out.println("plant breeding probability changed to " + BREEDING_PROBABILITY);
                }
                break;
            case RAINY:
                if (BREEDING_PROBABILITY == ORIGINAL_BREEDING_PROBABILITY){
                    System.out.println("plant breeding probability not yet changed " + BREEDING_PROBABILITY);
                    WeatherAction.rainOnPlants(BREEDING_PROBABILITY);
                    System.out.println("plant breeding probability changed to " + BREEDING_PROBABILITY);
                }
                break;
            case SNOWY:
                if (BREEDING_PROBABILITY == ORIGINAL_BREEDING_PROBABILITY){
                    System.out.println("plant breeding probability not yet changed " + BREEDING_PROBABILITY);
                    WeatherAction.snowOnPlants(BREEDING_PROBABILITY);
                    System.out.println("plant breeding probability changed to " + BREEDING_PROBABILITY);
                }
                break;
        }
    }
    
    /**
     * Resets the breeding probability to the original value.
     * 
     */
    public static void resetBreedingProbability()
    {
        BREEDING_PROBABILITY = ORIGINAL_BREEDING_PROBABILITY;
        System.out.println("plantGrowthProbability reset to  " + BREEDING_PROBABILITY);
    }
}
