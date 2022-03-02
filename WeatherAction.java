import java.util.EnumMap;

/**
 * Defines the actions that occur upon the actors under different weather
 * conditions.
 *
 * @author Bhavik Gilbert(K21004990) and Heman Seegolam(K21003628)
 * @version (28/02/2022)
 */
public class WeatherAction
{  
    // Map of weather conditions with numerical values for different weather effects on plants breeding.
    private static final EnumMap<Weather, Double> plantBreedEffect = new EnumMap<>(Weather.class){{
                put(Weather.SUNNY, 1.5);
                put(Weather.RAINY, 1.25);
                put(Weather.SNOWY, 0.5);
            }};

    // Map of weather conditions with numerical values for different weather effects on animals breeding.
    private static final EnumMap<Weather, Double> animalBreedEffect = new EnumMap<>(Weather.class) {
            {
                put(Weather.SUNNY, 1.5);
                put(Weather.RAINY, 0.75);
                put(Weather.SNOWY, 0.5);
                put(Weather.FOGGY, 0.75);
            }
        };

    // Map of weather conditions with numerical values for different weather effects on animals eating.
    private static final EnumMap<Weather, Double> animalHuntEffect = new EnumMap<>(Weather.class) {
            {
                put(Weather.RAINY, 0.5);
                put(Weather.SNOWY, 0.25);
                put(Weather.FOGGY, 0.5);
            }
        };

    /**
     * No weather objects are constructed
     * All values are statically used
     */
    public WeatherAction()
    {
    }

    /**
     * Returns a Map of modifiers for plants based on the actions they take.
     * If weather condition has no modifier, it is set to 1.
     * 
     * @param weather The current weather.
     * @return The Map of actions and their current modifiers.
     */
    public static EnumMap<WeatherEffectTypes, Double> weatherOnPlants(Weather weather)
    {
        EnumMap<WeatherEffectTypes, Double> weatherEffect = new EnumMap<>(WeatherEffectTypes.class);

        if (plantBreedEffect.get(weather) != null) {
            weatherEffect.put(WeatherEffectTypes.BREED, plantBreedEffect.get(weather));
        }
        else {
            weatherEffect.put(WeatherEffectTypes.BREED, 1.0);
        }

        return weatherEffect;
    }

    /**
     * Returns a Map of modifiers for animals based on the actions they take.
     * If weather condition has no modifier, it is set to 1.
     * 
     * @param weather The current weather.
     * @return The Map of actions and their current modifiers.
     */
    public static EnumMap<WeatherEffectTypes, Double> weatherOnAnimals(Weather weather)
    {
        EnumMap<WeatherEffectTypes, Double> weatherEffect = new EnumMap<>(WeatherEffectTypes.class);

        if(animalBreedEffect.get(weather) != null){
            weatherEffect.put(WeatherEffectTypes.BREED, animalBreedEffect.get(weather));
        }
        else{
            weatherEffect.put(WeatherEffectTypes.BREED, 1.0);
        }

        if (animalHuntEffect.get(weather) != null) {
            weatherEffect.put(WeatherEffectTypes.HUNT, animalHuntEffect.get(weather));
        } 
        else {
            weatherEffect.put(WeatherEffectTypes.HUNT, 1.0);
        }

        return weatherEffect;
    }
}
