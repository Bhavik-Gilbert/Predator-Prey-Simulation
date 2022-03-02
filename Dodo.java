import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple model of a dodo.
 * Dodos age, move, reproduce, eat plants, and die.
 * 
 * @author Bhavik Gilbert(K21004990) and Heman Seegolam(K21003628)
 * @version (28/02/2022)
 */
public class Dodo extends Animal
{
    // Characteristics shared by all Dodos (class variables).

    // The age at which a dodo can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a dodo can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a dodo breeding.
    private static final double BREEDING_PROBABILITY = 0.9;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 12;
    // The base rate which is used to give
    // the number of steps a predator gains when it eats a dodo
    private static final int BASIC_FOOD_LEVEL = 8;
    // The chance of a Dodo attacking a predator in its sleep
    private static final double ATTACK_CHANCE = 0.01;
    // Probability that a dodo dies from disease.
    private static final double DODO_DEATH_FROM_DISEASE_PROBABILITY = 0.005;
    // List of all dodo prey.
    private final ArrayList<Class> LIST_OF_PREY = new ArrayList<>() {
            {
                add(Plant.class);
            }
        };

    /**
     * Create a dodo. A dodo can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the dodo will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param infected  Boolean value determining if the animal is infected or not
     */
    protected Dodo(boolean randomAge, Field field, Location location, boolean infected)
    {
        super(field, location, infected);

        // Sets values in animal class
        setOverlap(false);
        setFoodValue(BASIC_FOOD_LEVEL);
        setDeathByDiseaseProbability(DODO_DEATH_FROM_DISEASE_PROBABILITY);
        setBreedingAge(BREEDING_AGE);
        setBreedingProbability(BREEDING_PROBABILITY);
        setMaxLitter(MAX_LITTER_SIZE);
        setMaxAge(MAX_AGE);

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
        incrementAge();
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
     * 
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

    /**
     * Dodo's attack their known predators
     * Looks at all adjacent locations, and attacks first predator found
     * Dodo moves into the position the predator was
     */
    private Location chargePredator()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> charge = new ArrayList<>();

        adjacent.forEach(where ->{
                Object actor = field.getObjectAt(where);
                HashSet<Class> setOfPredators = MAP_OF_PREDATORS.get(this.getClass());
                if(actor!=null && setOfPredators.contains(actor.getClass())){
                    if(rand.nextDouble() <= ATTACK_CHANCE){
                        Animal prey = (Animal) actor;
                        if(prey.isAlive()) { 
                            prey.setDead();
                            foodLevel += prey.getFoodValue();
                            charge.add(where);
                        }
                    }
                }
            });

        if(!charge.isEmpty()){
            return charge.get(0);
        }

        return null;
    }

}
