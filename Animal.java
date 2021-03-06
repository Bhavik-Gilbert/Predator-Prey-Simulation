import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author Bhavik Gilbert(K21004990) and Heman Seegolam(K21003628)
 * @version (28/02/2022)
 */
public abstract class Animal extends Actor
{
    // The animal's gender.
    private Gender gender;
    // The animal's age.
    protected int age;
    // The animal's food level, which is increased by eating.
    protected double foodLevel;
    // The base rate which is used to give
    // the number of steps a predator gains when it eats this animal.
    private int FOOD_VALUE;
    // The probability that a predator eats its prey.
    private static final double EATING_PROBABILITY = 0.8;
    // A map containing values that can affect how the animal reacts based off the weather.
    private EnumMap<WeatherEffectTypes, Double> weatherEffect = new EnumMap<>(WeatherEffectTypes.class);
    // Probability that an animal dies from disease.
    private double DEATH_FROM_DISEASE_PROBABILITY;
    // Minimum breeding age.
    private int BREEDING_AGE;
    // Probability of successful breeding when a partner is found.
    private double BREEDING_PROBABILITY;
    // Maximum litter per breeding session.
    private int MAX_LITTER_SIZE;
    // Maximum age the animal can reach.
    private int MAX_AGE;
    // Probability that an infected animal spreads the virus.
    private double VIRUS_SPREAD_PROBABILITY = 0.1;
    // The probability that an infected animal gets cured.
    private double CURE_INFECTION_PROBABILITY = 0.1;
    // Map containing a set of all the predators of a given animal.
    protected static HashMap<Class, HashSet<Class>> MAP_OF_PREDATORS =  new HashMap<>();

    // Whether an animal is infected by disease or not.
    protected boolean infected;

    /**
     * Create a new animal at location in field.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param infected Boolean value determining if the animal is infected or not.
     * 
     */
    protected Animal(Field field, Location location, boolean infected)
    {
        super(field, location);
        // infects animal if infected.
        this.infected = infected;
        // assigns random gender.
        randomGender();
        initiatePredators();
    }

    /**
     * Creates a key for the current animal type in the MAP_OF_PREDATORS.
     * Generates empty set and assigns it to the key.
     */
    private void initiatePredators()
    {
        if(!MAP_OF_PREDATORS.containsKey(this.getClass())){
            MAP_OF_PREDATORS.put(this.getClass(), new HashSet<Class>());
        }
    }

    /**
     * Sets the maximum age of the animal.
     * 
     * @param maxAge THe maximum age of the animal.
     */
    protected void setMaxAge(int maxAge)
    {
        MAX_AGE = maxAge;
    }

    /**
     * Sets the minimum age the animal can start breeding.
     * 
     * @param setBreedingAge The minimum age the animal can start breeding.
     */
    protected void setBreedingAge(int breedingAge)
    {
        BREEDING_AGE = breedingAge;
    }

    /**
     * Sets the breeding probability of the animal.
     * 
     * @param breedingProbability The breeding probability of the animal.
     */
    protected void setBreedingProbability(double breedingProbability) 
    {
        BREEDING_PROBABILITY = breedingProbability;
    }

    /**
     * Sets the maximum litter per breeding period for an animal.
     * 
     * @param maxLitter The maximum litter per breeding period for an animal.
     */
    protected void setMaxLitter(int maxLitter) 
    {
        MAX_LITTER_SIZE = maxLitter;
    }

    /**
     * Sets the gender of the animal.
     * 
     * @param setGender The gender that the animal will be set to.
     */
    private void setGender(Gender gender) 
    {
        this.gender = gender;
    }

    /**
     * Sets the base number of steps predator gains for eating this animal.
     * 
     * @param foodValue The base step number.
     */
    protected void setFoodValue(int foodValue)
    {
        FOOD_VALUE = foodValue;
    }

    /**
     * Generates a random gender from the Gender ENUM.
     * Call setGender to give animal this random gender.
     */
    private void randomGender()
    {
        setGender(Gender.values()[rand.nextInt(Gender.values().length)]);
    }            

    /**
     * Gets the gender of the animal.
     * 
     * @return Gender of the animal.
     */
    private Gender getGender()
    {
        return gender;
    }

    /**
     * Retrieves and sets the current weather effect values.
     */
    protected void setWeatherEffects() 
    {
        weatherEffect = WeatherAction.weatherOnAnimals(weather);
    }

    /**
     * An animal can breed if it has reached the breeding age and meets a mate of opposite gender.
     * 
     * @param field The field the object is in.
     * @return Boolean value, true if mate is found and is of age, false otherwise.
     */
    private boolean canBreed(Field field) 
    {
        // Find all adjacent locations.
        List<Location> adjacentLocations = field.adjacentLocations(getLocation()); 
        // List of potential partners.
        List<Animal> partnerList = new ArrayList<>();

        adjacentLocations.forEach(adjacentLocation -> {
                Object actor = field.getObjectAt(adjacentLocation);
                if (actor instanceof Animal) {
                    Animal animal = (Animal) actor;
                    // Check for same species and opposite gender.
                    if (this.getClass().equals(animal.getClass()) && !(this.getGender().equals(animal.getGender()))) {
                        partnerList.add(animal);
                    }
                }
            });

        return age >= BREEDING_AGE && !partnerList.isEmpty();
    }

    /**
     * Increase the age. This could result in the animal's death.
     */
    protected void incrementAge() 
    {
        age++;
        if (age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger() 
    {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Calculates the number of steps gained for eating this animal
     * maxing out at the maximum food value.
     * 
     * @return The number of steps gained for eating this animal.
     */
    protected double getFoodValue()
    {
        double food;

        food = -((4/MAX_AGE)*(age-(MAX_AGE/2)))*2 + FOOD_VALUE;

        if(age<MAX_AGE/5 || age>MAX_AGE*4/5){
            food -= (FOOD_VALUE/5);
        }

        return food;
    }

    /**
     * Look for prey / plants adjacent to the current location.
     * Only the first live prey or plant is eaten.
     * Food level is increased by the prey's mass if animal eats prey.
     * 
     * @param listOfPrey A list of prey that this animal feeds on.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(List<Class> listOfPrey)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> availablePrey = new ArrayList<>();

        adjacent.forEach(where -> {
                Object object = field.getObjectAt(where);
                if (object != null){
                    Actor actor = (Actor) object;
                    // if actor type is in list of prey for predator, prey is alive and probability for eating met.
                    if ((listOfPrey.contains(actor.getClass())) && (rand.nextDouble() <= EATING_PROBABILITY * effectHuntingProbability()) && (actor.isAlive())) { 
                        actor.setDead();
                        foodLevel += actor.getFoodValue();
                        if(actor instanceof Animal){
                            Animal animal = (Animal) actor;
                            this.infected = this.infected || animal.getInfected();

                            // Adds this animal to a set of predators for the prey.
                            HashSet<Class> listOfPredators = MAP_OF_PREDATORS.get(animal.getClass());
                            listOfPredators.add(this.getClass());
                            MAP_OF_PREDATORS.replace(animal.getClass(), listOfPredators);
                        }
                        availablePrey.add(where);
                    }
                }
            });

        if(!availablePrey.isEmpty()){
            return availablePrey.get(0);
        }
        return null;
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @param field The field the object is currently in.
     * @return The number of births (may be zero).
     */
    private int breed(Field field) 
    {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY * effectBreedingProbability()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAnimal A list to return newly born animal.
     */
    protected void giveBirth(List<Actor> newAnimal) 
    {
        // New animals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            Class type = this.getClass();

            if(Human.class.equals(type)){
                Human young = new Human(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(Dodo.class.equals(type)){
                Dodo young = new Dodo(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(Monkey.class.equals(type)){
                Monkey young = new Monkey(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(Pig.class.equals(type)){
                Pig young = new Pig(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(Tortoise.class.equals(type)){
                Tortoise young = new Tortoise(false, field, loc, false);
                newAnimal.add(young);
            }
        }
    }

    /**
     * If infected, checks for adjacent animals, providing the chance for them to be infected.
     */
    protected void spreadVirus()
    {
        if (infected) {
            List<Location> adjacentLocations = field.adjacentLocations(getLocation());

            adjacentLocations.forEach(adjacent -> {
                    Object object = field.getObjectAt(adjacent);
                    if(object instanceof Animal){
                        Animal animal = (Animal) object;
                        if(rand.nextDouble() > VIRUS_SPREAD_PROBABILITY){
                            animal.infect();
                        }
                    }

                });
        }
    }

    /**
     * Infects the animal with a virus, sets infected to true.
     */
    private void infect()
    {
        this.infected = true;
    }

    /**
     * Returns whether or not this animal is infected with a virus.
     * 
     * @return Boolean infected.
     */
    @Override
    public boolean getInfected() 
    {
        return this.infected;
    }   

    /**
     * If infected, runs the chance for the animal to be cured of infection.
     */
    protected void cureInfected() 
    {
        if (infected) {
            this.infected =  rand.nextDouble() > CURE_INFECTION_PROBABILITY;
        }
    }

    /**
     * If infected, implements the effects of disease infection on animals.
     * 
     */
    protected void dieInfection() 
    {
        if (infected) {
            double deathFromDiseaseProbability = DEATH_FROM_DISEASE_PROBABILITY;
            if (rand.nextDouble() <= deathFromDiseaseProbability) {
                setDead();
            }
        }
    }

    /**
     * Sets DEATH_FROM_DISEASE_PROBABILITY to individual animal type death
     * probability.
     * 
     * @param deathProbability Animal death by disease probability.
     */
    protected void setDeathByDiseaseProbability(double deathProbability) 
    {
        DEATH_FROM_DISEASE_PROBABILITY = deathProbability;
    }

    /**
     * Retrieves the breeding probability modifier.
     * Calculated by current weather conditions.
     * 
     * @return Breeding probability modifier.
     */
    private double effectBreedingProbability() 
    {
        return weatherEffect.get(WeatherEffectTypes.BREED);
    }

    /**
     * Retrieves the feeding probability modifier.
     * Calculated by current weather conditions.
     * 
     * @return Feeding probability modifier.
     */
    private double effectHuntingProbability() 
    {
        return weatherEffect.get(WeatherEffectTypes.HUNT);
    }
}
