import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
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
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    private void setGender(Gender gender)
    {
        gender = gender;
    }
    
    protected void randomGender()
    {
        Random rand = Randomizer.getRandom();
        setGender(Gender.values()[rand.nextInt(Gender.values().length)]);
    }            
    
    protected Gender getGender()
    {
        return gender;
    }
}
