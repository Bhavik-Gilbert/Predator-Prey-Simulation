import java.util.List;
import java.util.Random;

/**
 * Defines what the different characteristics of elements in the simulation must have
 *
 * Determines it's state of life and location on the field
 * Creates existence of action method for subclasses 
 *
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public abstract class Actor
{
    // Whether the actor is alive or not.
    protected boolean alive;
    // The actor's field.
    protected Field field;
    // The actor's position in the field.
    protected Location location;
    // Whether the actor can overlap with other actors in a particular field
    protected boolean overlap;
    // The current time of day, true day, false night
    private boolean day;
    // The current weather conditions in the field
    protected Weather weather;
    // A shared random number generator to control breeding and disease infection effects.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Actor
     * 
     * @param field The field the actor is currently in
     * @param location The actors location on the field
     */
    public Actor(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Determines whether night or day.
     * Replaces actor onto field if alive and space available
     * Sets current weather and determines effects
     * 
     * @param newActors A list to return newly born actors.
     * @param timeOfDay Integer value determining day or night
     */
    public void act(List<Actor> newActors, int timeOfDay, Weather weather) {
        replaceActor();
        this.weather = weather;
        setWeatherEffects();

        switch (timeOfDay) {
            case 0:
                day = true;
                dayAct(newActors);
            case 1:
                day = false;
                nightAct(newActors);
        }
    }

    /**
     * Used to place alive actors,
     * back onto the board at their location if their space is available
     */
    private void replaceActor(){

        if ((alive) && (field.getObjectAt(location) == null)) {
            field.place(this, location);
        }
    }
    
    /**
     * Returns the current time of day 
     * @return Current time of day, true if day, false if night
     */
    protected boolean getDay(){
        return day;
    }

    /**
     * Returns if the actor is infected or not
     * @return actor always not infected
     */
    public boolean getInfected(){
        return false;
    }
    
    /**
     * Plays out the actions taken by the actor during the day.
     * 
     * @param newActors A list to return newly born actors.
     */
    protected abstract void dayAct(List<Actor> newActors);

    /**
     * Plays out the actions taken by the actor during the night.
     * 
     * @param newActors A list to return newly born actors.
     */
    protected abstract void nightAct(List<Actor> newActors);
    
    /**
     * Gets food value of animal or plants
     * 
     */
    protected abstract double getFoodValue();

    /**
     * Sets current weather effects values
     * 
     */
    protected abstract void setWeatherEffects();

    protected void setOverlap(boolean overlapping){
        this.overlap = overlapping;
    }

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
    
    /**
     * Return Whether the actor can overlap with other actors.
     * @return Whether the actor can overlap.
     */
    public boolean getOverlap()
    {
        return overlap;
    }
}
