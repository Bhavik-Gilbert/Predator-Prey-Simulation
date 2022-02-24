import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

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
    private static final int BREEDING_AGE = 15;
    // The age to which a dodo can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a dodo breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The base rate which when multiplied by age gives
    // the number of steps a predator gains when it eats a dodo
    private static final double DODO_FOOD_VALUE = 0.4;
    // Base starting food level for all dodos
    private static final int BASIC_FOOD_LEVEL = 20;
    // The chance of a Dodo attacking a predator in its sleep
    private static final double ATTACK_CHANCE = 0.01;
    // Probability that a dodo dies from disease.
    private static final double DODO_DEATH_FROM_DISEASE_PROBABILITY = 0.02;
    // List of all dodo prey.
    private final ArrayList<ActorTypes> LIST_OF_PREY = new ArrayList<>() {
        {
            add(ActorTypes.PLANT);
        }
    };

    /**
     * Create a dodo. A dodo can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the dodo will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param overlap   Whether or not an actor is allowed to overlap with other actors
     * @param infected  Boolean value determining if the animal is infected or not
     */
    protected Dodo(boolean randomAge, Field field, Location location, boolean infected)
    {
        super(field, location, infected);
        setOverlap(false);
        description = ActorTypes.DODO;
        setFoodValue(DODO_FOOD_VALUE);
        setDeathByDiseaseProbability(DODO_DEATH_FROM_DISEASE_PROBABILITY);
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
     * @param newDodos A list to return newly born Dodos.
     */
    protected void dayAct(List<Actor> newDodos)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        dieInfection();
        
        
        if(isAlive()) {            
            giveBirth(newDodos);   
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
     * This is what the dodos do during the night:  Sleeps and Attacks predators
     * In the process it might, die of infection or spread an infection
     * @param newDodos A list to return newly born Dodos.
     */
    protected void nightAct(List<Actor> newDodos)
    {
        
        dieInfection();
        

        if (isAlive()) {
            cureInfected();
            spreadVirus();
            
            // Charges into predator killing it
            Location newLocation = chargePredator();
            if (newLocation != null && (ATTACK_CHANCE >= rand.nextDouble())) {
                setLocation(newLocation);
            }
        }
    }
    
    //NEEDS REFACTORING
    private Location chargePredator(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object actor = field.getObjectAt(where);
            if((actor instanceof Human) || (actor instanceof Pig) || (actor instanceof Monkey)){
                if(rand.nextDouble() <= ATTACK_CHANCE){
                    Animal prey = (Animal) actor;
                    if(prey.isAlive()) { 
                        prey.setDead();
                        foodLevel += prey.getFoodValue();
                        return where;
                    }
                }
            }
        }

        return null;
    }
    
}
