import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert & Heman Seegolam
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Actor
{
    // The animal's gender.
    protected Gender gender;
    // The animal's age.
    protected int age;
    // The animal's food level, which is increased by eating.
    protected double foodLevel;

    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats this animal
    protected double FOOD_VALUE;
    
    // the probability that a predator eats its prey.
    protected static final double ORIGINAL_EATING_PROBABILITY = 0.8;
    protected static double EATING_PROBABILITY;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    protected Animal(Field field, Location location, boolean overlap)
    {
        super(field, location, overlap);
        EATING_PROBABILITY = ORIGINAL_EATING_PROBABILITY;
        randomGender();
    }
    
    /**
     * Sets the gender of the animal
     * @param setGender The gender that the animal will be set to
     */
    private void setGender(Gender gender)
    {
        this.gender = gender;
    }
    
    protected void setFoodValue(double food_value){
        FOOD_VALUE = food_value;
    }
    /**
     * Generates a random gender from the Gender ENUM
     * Call setGender to give animal this random gender
     */
    protected void randomGender()
    {
        Random rand = Randomizer.getRandom();
        setGender(Gender.values()[rand.nextInt(Gender.values().length)]);
    }            
    
    /**
     * Gets the gender of the animal
     * @return Gender of the animal
     */
    protected Gender getGender()
    {
        return gender;
    }

    /**
     * An animal can breed if it has reached the breeding age and meets a mate of opposite gender.
     * @param field The field the object is in
     * @param BREEDING_AGE The minimum age that animal can start breeding
     * @param age The current age of the animal
     * @return Boolean value, true if mate is found and is of age, false otherwise
     */
    protected boolean canBreed(Field field, int BREEDING_AGE, int age) {
        boolean partner = false;
        // Find all adjacent locations
        List<Location> adjacentLocations = field.adjacentLocations(getLocation()); 
        while (adjacentLocations.size() > 0) {
            // Retrieves object at location
            Object actor = field.getObjectAt(adjacentLocations.get(0));
            if (actor instanceof Animal) {
                Animal animal = (Animal) actor;
                // Check for same species and opposite gender
                if (this.getClass().equals(animal.getClass()) && !(this.getGender().equals(animal.getGender()))) {
                    partner = true;
                    break;
                }
            }
            // Removes checked object from list
            adjacentLocations.remove(0);
        }

        return age >= BREEDING_AGE && partner;
    }

    /**
     * Increase the age. This could result in the animal's death.
     */
    protected void incrementAge(int MAX_AGE) {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    protected double getFoodValue(){
        return age * FOOD_VALUE;
    }
    
    public static void setEatingProbability(double eatingProbability){
        EATING_PROBABILITY = eatingProbability;
    }
    
    /**
     * Implement the effects of the weather.
     * @param weather The weather.
     */
    public static void implementWeather(Weather weather)
    {
        switch (weather) {
            case FOGGY:
                if (EATING_PROBABILITY == ORIGINAL_EATING_PROBABILITY){
                    System.out.println("animal eating probability not yet changed " + EATING_PROBABILITY);
                    WeatherAction.fogOnAnimals(EATING_PROBABILITY);
                    System.out.println("animal eating probability changed to " + EATING_PROBABILITY);
                }
        }
    }
    
    /**
     * Resets the eating probability to the original value.
     * 
     */
    public static void resetEatingProbability()
    {
        EATING_PROBABILITY = ORIGINAL_EATING_PROBABILITY;
        System.out.println("eatingProbability reset to  " + EATING_PROBABILITY);
    }
}
