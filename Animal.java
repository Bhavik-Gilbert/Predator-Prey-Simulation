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
        this.gender = gender;
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

    /**
     * A monkey can breed if it has reached the breeding age.
     */
    protected boolean canBreed(Field field, int BREEDING_AGE, int age) {
        boolean partner = false;
        List<Location> free = field.adjacentLocations(getLocation()); 
        while (free.size() > 0) {
            free.remove(0);

            Object actor = field.getObjectAt(getLocation());
            if (actor instanceof Animal) {
                Animal animal = (Animal) actor;
                //System.out.println(this.getClass().equals(animal.getClass()) && !(this.getGender().equals(animal.getGender())));
                //System.out.println(this.getClass().equals(animal.getClass()));
                //System.out.println(!(this.getGender().equals(animal.getGender())));
                //System.out.println(this.getGender() + " : " + animal.getGender());
                //System.out.println("");
                if (this.getClass().equals(animal.getClass()) && !(this.getGender().equals(animal.getGender()))) {
                    partner = true;
                    break;
                }
            }
        }

        if (!partner) {
            return partner;
        }
        
        System.out.println("birth");
        return age >= BREEDING_AGE;
    }
}
