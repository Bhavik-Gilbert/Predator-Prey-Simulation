import java.util.*;

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
    // The probability that a predator eats its prey.
    protected static final double EATING_PROBABILITY = 0.8;
    // A map containing values that can effect how the animal reacts based off of the weather
    protected EnumMap<WeatherEffectTypes, Double> weatherEffect = new EnumMap<>(WeatherEffectTypes.class);
    // Probability that an animal dies from disease.
    private double DEATH_FROM_DISEASE_PROBABILITY;
    private int BREEDING_AGE;
    private double BREEDING_PROBABILITY;
    private int MAX_LITTER_SIZE;

    // Whether an animal is infected by disease or not.
    protected boolean infected;

    
    /**
     * Create a new animal at location in field.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param infected Boolean value determining if the animal is infected or not
     * 
     */
    protected Animal(Field field, Location location,boolean infected)
    {
        super(field, location);
        this.infected = infected;

        //assigns random gender
        randomGender();
    }
    
    /**
     * Sets the minimum age the animal can start breeding
     * 
     * @param setBreedingAge The minimum age the animal can start breeding
     */
    protected void setBreedingAge(int breedingAge)
    {
        BREEDING_AGE = breedingAge;
    }

    /**
     * Sets the breeding probability of the animal
     * 
     * @param breedingProbability The breeding probability of the animal
     */
    protected void setBreedingProbability(double breedingProbability) {
        BREEDING_PROBABILITY = breedingProbability;
    }


    /**
     * Sets the maximum litter per breeding period for an animal
     * 
     * @param maxLitter The maximum litter per breeding period for an animal
     */
    protected void setMaxLitter(int maxLitter) {
        MAX_LITTER_SIZE = maxLitter;
    }

    /**
     * Sets the gender of the animal
     * 
     * @param setGender The gender that the animal will be set to
     */
    private void setGender(Gender gender) {
        this.gender = gender;
    }
    
    /**
     * Sets the base number of steps predator gains for eating this animal
     * 
     * @param foodValue The base step number
     */
    protected void setFoodValue(double foodValue){
        FOOD_VALUE = foodValue;
    }
    /**
     * Generates a random gender from the Gender ENUM
     * Call setGender to give animal this random gender
     */
    protected void randomGender()
    {
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
     * Retrieves and sets the current weather effect values
     */
    protected void setWeatherEffects() {
        weatherEffect = WeatherAction.weatherOnAnimals(weather);
    }

    /**
     * An animal can breed if it has reached the breeding age and meets a mate of opposite gender.
     * @param field The field the object is in
     * 
     * @return Boolean value, true if mate is found and is of age, false otherwise
     */
    protected boolean canBreed(Field field) {
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

    /**
     * Calculates the number of steps gained for eating this animal
     * @return The number of steps gained for eating this animal
     */
    protected double getFoodValue(){
        return age * FOOD_VALUE;
    }
    
    /**
     * Look for prey / plants adjacent to the current location.
     * Only the first live prey or plant is eaten.
     * Food level is increased by the prey's mass if animal eats prey
     * 
     * @param listOfPrey A list of prey that this animal feeds on
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(List<ActorTypes> listOfPrey)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            if (object != null){
                Actor actor = (Actor) object;
                // if actor type is in list of prey for predator, prey is alive and probability for eating met
                if ((listOfPrey.contains(actor.getDescription())) && (rand.nextDouble() <= EATING_PROBABILITY * effectHuntingProbability()) && (actor.isAlive())) { 
                    actor.setDead();
                    foodLevel += actor.getFoodValue();
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Implements the effects of disease infection on animals
     * 
     */
    protected void infection()
    {
        double deathFromDiseaseProbability = DEATH_FROM_DISEASE_PROBABILITY;
        if (rand.nextDouble() <= deathFromDiseaseProbability){
            setDead();
        }
    }

    /**
     * Sets DEATH_FROM_DISEASE_PROBABILITY to individual animal type death probability
     * 
     * @param deathProbability Animal death by disease probability
     */
    protected void setDeathByDiseaseProbability(double deathProbability)
    {
        DEATH_FROM_DISEASE_PROBABILITY = deathProbability;
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @param field The field the object is currently in
     * @return The number of births (may be zero).
     */
    protected int breed(Field field) 
    {
        int births = 0;
        if (canBreed(field) && rand.nextDouble() <= BREEDING_PROBABILITY * effectBreedingProbability()) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Check whether or not this human is to give birth at this step.
     * New births will be made into free adjacent locations.
     * 
     * @param newAnimal A list to return newly born animal.
     */
    protected void giveBirth(List<Actor> newAnimal) {
        // New human are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(field);
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            ActorTypes type = this.getDescription();

            if(ActorTypes.HUMAN.equals(type)){
                Human young = new Human(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(ActorTypes.DODO.equals(type)){
                Dodo young = new Dodo(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(ActorTypes.MONKEY.equals(type)){
                Monkey young = new Monkey(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(ActorTypes.PIG.equals(type)){
                Pig young = new Pig(false, field, loc, false);
                newAnimal.add(young);
            }
            else if(ActorTypes.TORTOISE.equals(type)){
                Tortoise young = new Tortoise(false, field, loc, false);
                newAnimal.add(young);
            }
        }
    }

    /**
     * Retrieves the breeding probability modifier
     * Calculated by current weather conditions
     * 
     * @return Breeding probability modifier
     */
    private double effectBreedingProbability() {
        return weatherEffect.get(WeatherEffectTypes.BREED);
    }

    /**
     * Retrieves the feeding probability modifier
     * Calculated by current weather conditions
     * 
     * @return Feeding probability modifier
     */
    private double effectHuntingProbability() {
        return weatherEffect.get(WeatherEffectTypes.HUNT);
    }
}
