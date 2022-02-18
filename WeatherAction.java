
/**
 * Defines the actions that occur upon the actors under different weather conditions.
 *
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (a version number or a date)
 */
public class WeatherAction
{
    private Weather weather;

    /**
     * Constructor for objects of class WeatherAction.
     */
    public WeatherAction(Weather weather)
    {
        this.weather = weather;
    }

    /**
     * When sunny, growth of plants is increased by a factor.
     *
     * @param  plantGrowthProbability  the probabilty for plant growth.
     */
    public static void sunOnPlants(double plantGrowthProbability)
    {
        Plant.setBreedingProbability(plantGrowthProbability * 1.5);
    }
    
    /**
     * When raining, growth of plants is increased by a factor.
     *
     * @param  plantGrowthProbability  the probabilty for plant growth.
     */
    public static void rainOnPlants(double plantGrowthProbability)
    {
        Plant.setBreedingProbability(plantGrowthProbability * 1.25);
    }
    
    /**
     * When snowing, growth of plants is decreased by a factor.
     *
     * @param  plantGrowthProbability  the probabilty for plant growth.
     */
    public static void snowOnPlants(double plantGrowthProbability)
    {
        Plant.setBreedingProbability(plantGrowthProbability / 2);
    }
    
    /**
     * When foggy, probability that a predator eats its prey is decreased by a factor.
     *
     * @param  eatingProbability  the probabilty that a predator eats its prey
     */
    public static void fogOnAnimals(double eatingProbability)
    {
        Animal.setEatingProbability(eatingProbability / 2);
    }
}
