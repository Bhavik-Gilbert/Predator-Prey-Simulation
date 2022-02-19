import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of a human.
 * Human age, move, eat dodos and pigs, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Human extends Animal
{
    // Characteristics shared by all human (class variables).
    
    // The age at which a human can start to breed.
    private static final int BREEDING_AGE = 18;
    // The age to which a human can live.
    private static final int MAX_AGE = 90;
    // The likelihood of a human breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a human
    private static final double HUMAN_FOOD_VALUE = 0.3;
    // Base starting food level for all humans
    private static final int BASIC_FOOD_LEVEL = 20;
    // Probability that a human dies from disease.
    private static final double HUMAN_DEATH_FROM_DISEASE_PROBABILITY = 0.05;
    // List of all human prey.
    private final ArrayList<ActorTypes> LIST_OF_PREY = new ArrayList<>(){
        {
            add(ActorTypes.DODO);
            add(ActorTypes.PIG);
        }
    };

    /**
     * Create a human. A human can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the human will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param infected  Boolean value determining if the animal is infected or not
     */
    protected Human(boolean randomAge, Field field, Location location, boolean infected)
    {
        super(field, location, infected);
        setOverlap(false);
        setFoodValue(HUMAN_FOOD_VALUE);
        description = ActorTypes.HUMAN;
        setDeathByDiseaseProbability(HUMAN_DEATH_FROM_DISEASE_PROBABILITY);
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
     * This is what the human does during the day: it hunts for dodos and pigs.
     *  In the process, die of hunger, or die of old age.
     * 
     * @param newHuman A list to return newly born humans.
     */
    protected void dayAct(List<Actor> newHuman)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        super.infection();
        if(isAlive()) {           
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
     * This is what the human does during the night: Gives birth and Sleeps
     * 
     * @param newHuman A list to return newly born humans.
     */
    protected void nightAct(List<Actor> newHuman)
    {
        super.infection();
        if(isAlive()) {
            giveBirth(newHuman);
        }
    }
}
