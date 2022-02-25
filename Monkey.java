import java.util.List;
import java.util.ArrayList;

/**
 * A simple model of a monkey.
 * Monkeys age, move, eat dodos, and die.
 * 
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public class Monkey extends Animal
{
    // Characteristics shared by all monkeys (class variables).
    
    // The age at which a monkey can start to breed.
    private static final int BREEDING_AGE = 6;
    // The age to which a monkey can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a monkey breeding.
    private static final double BREEDING_PROBABILITY = 0.6;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a monkey
    private static final double MONKEY_FOOD_VALUE = 0.5;
    // Base starting food level for all monkeys
    private static final int BASIC_FOOD_LEVEL = 20;
    // Probability that a monkey dies from disease.
    private static final double MONKEY_DEATH_FROM_DISEASE_PROBABILITY = 0.05;
    // List of all monkey prey.
    private final ArrayList<Class> LIST_OF_PREY = new ArrayList<>() {
        {
            add(Dodo.class);
        }
    };

    /**
     * Create a monkey. A monkey can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the monkey will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param overlap   Whether or not an actor is allowed to overlap with other actors
     * @param infected  Boolean value determining if the animal is infected or not
     */
    protected Monkey(boolean randomAge, Field field, Location location, boolean infected)
    {
        super(field, location, infected);
        setOverlap(false);
        setFoodValue(MONKEY_FOOD_VALUE);
        setDeathByDiseaseProbability(MONKEY_DEATH_FROM_DISEASE_PROBABILITY);
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
     * This is what the monkey does during the day: it hunts for dodos and tries to breed
     * In the process it might move, die of hunger, die of infection, get cured, spread an infection, or die of old age.
     *
     * @param newMonkeys A list to return newly born monkeys.
     */
    protected void dayAct(List<Actor> newMonkeys)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        dieInfection();

        if(isAlive()) {    
            giveBirth(newMonkeys); 
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
     * This is what the monkey does during the night: Sleeps
     * In the process it might, die of infection
     * 
     * @param newMonkeys A list to return newly born monkeys.
     */
    protected void nightAct(List<Actor> newMonkeys)
    {
        dieInfection();
    }
}
