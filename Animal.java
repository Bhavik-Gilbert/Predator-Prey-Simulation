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
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
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
                // Check for same species and oppsite gender
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
}
