import java.util.List;

/**
 * Defines what the different characteristics of elements in the simulation must have
 *
 *  Determines it's state of life and location on the field
 * Creates existence of action method for subclasses 
 *
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (a version number or a date)
 */
public abstract class Actor
{
    // Whether the actor is alive or not.
    protected boolean alive;
    // The actor's field.
    protected Field field;
    // The actor's position in the field.
    protected Location location;

    /**
     * Constructor for objects of class Actor
     */
    public Actor(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly born actors.
     * @param timeOfDay Integer value determining day or night
     */
    abstract public void act(List<Actor> newActors, int timeOfDay);

    /**
     * Check whether the actor is alive or not.
     * @return true if the actor is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the actor is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the actor's location.
     * @return The actor's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the actor at the new location in the given field.
     * @param newLocation The actor's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the actor's field.
     * @return The actor's field.
     */
    protected Field getField()
    {
        return field;
    }
}
