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
    protected static final double DEATH_FROM_DISEASE_PROBABILITY = 0.02;
    // List of all dodo prey.
    protected static List<String> LIST_OF_PREY;

    /**
     * Create a dodo. A dodo can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the dodo will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    protected Dodo(String description, boolean randomAge, Field field, Location location, boolean overlap, boolean infected)
    {
        super(description, field, location, overlap, infected);
        LIST_OF_PREY = new ArrayList<>();
        LIST_OF_PREY.add("Plant");
        setFoodValue(DODO_FOOD_VALUE);
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
     * This is what the dodo does during the day: it looks for plants
     * In the process, it might breed, die of hunger, or die of old age.
     * 
     * @param newDodos A list to return newly born Dodos.
     */
    public void dayAct(List<Actor> newDodos)
    {
        incrementAge(MAX_AGE);
        incrementHunger();
        super.infection();
        if(isAlive()) {
            giveBirth(newDodos);            
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
     * This is what the dodos do during the night: 
     * It may attack predators that would kill it during the day
     * Then it sleeps
     * @param newDodos A list to return newly born Dodos.
     */
    public void nightAct(List<Actor> newDodos)
    {
        super.infection();
        if (isAlive()) {
            // Charges into predator killing it
            Location newLocation = chargePredator();
            if (newLocation != null && (ATTACK_CHANCE >= rand.nextDouble())) {
                setLocation(newLocation);
            }
        }
    }
    
    private Location chargePredator(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object actor = field.getObjectAt(where);
            if((actor instanceof Human) || (actor instanceof Pig) || (actor instanceof Monkey)) {
                if (rand.nextDouble() <= EATING_PROBABILITY){
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
    
    /**
     * Check whether or not this dodo is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDodos A list to return newly born Dodos.
     */
    private void giveBirth(List<Actor> newDodos)
    {
        // New Dodos are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Dodo young = new Dodo("Dodo", false, field, loc, false, false);
            newDodos.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @param field The field the dodo is currently in
     * @return The number of births (may be zero).
     */
    private int breed(Field field)
    {
        int births = 0;
        if(canBreed(field, BREEDING_AGE, age) && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
}
