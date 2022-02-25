import java.util.List;
import java.util.ArrayList;

/**
 * A simple model of a tortoise.
 * Tortoises age, move, eat plants, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public class Tortoise extends Animal
{
    // Characteristics shared by all tortoises (class variables).
    
    // The age at which a tortoise can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a tortoise can live.
    private static final int MAX_AGE = 90;
    // The likelihood of a tortoise breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a tortoise
    private static final double TORTOISE_FOOD_VALUE = 0.3;
    // Base starting food level for all tortoises
    private static final int BASIC_FOOD_LEVEL = 20;
    // Probability that a tortoise dies from disease.
    private static final double TORTOISE_DEATH_FROM_DISEASE_PROBABILITY = 0.01;
    // List of all tortoise prey.
    private final ArrayList<Class> LIST_OF_PREY = new ArrayList<>() {
        {
            add(Plant.class);
        }
    };
    
    /**
     * Create a tortoise. A tortoise can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the tortoise will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param overlap   Whether or not an actor is allowed to overlap with other actors
     * @param infected  Boolean value determining if the animal is infected or not
     */
    protected Tortoise(boolean randomAge, Field field, Location location, boolean infected)
    {
        super(field, location, infected);
        setOverlap(false);
        setFoodValue(TORTOISE_FOOD_VALUE);
        setDeathByDiseaseProbability(TORTOISE_DEATH_FROM_DISEASE_PROBABILITY);
        setBreedingAge(BREEDING_AGE);
        setBreedingProbability(BREEDING_PROBABILITY);
        setMaxLitter(MAX_LITTER_SIZE);

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
     * This is what the dodo does during the day: it looks for plants and tries to breed
     * In the process it might move, die of hunger, die of infection, get cured, spread an infection, or die of old age.
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    protected void dayAct(List<Actor> newTortoises)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        dieInfection();
        
        if(isAlive()) {
            giveBirth(newTortoises);
            cureInfected();
            spreadVirus();      

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
     * This is what the tortoise does during the night: Sleeps
     * In the process it might, get cured or spread an infection
     * 
     * @param newTortoises A list to return newly born tortoises.
     */
    protected void nightAct(List<Actor> newTortoises)
    {
        if(alive){
            cureInfected();
            spreadVirus();
        } 
    }
}
