import java.util.Random;

/**
 * Provide control over the randomization of the simulation. By using the shared, fixed-seed randomizer, 
 * repeated runs will perform exactly the same (which helps with testing). 
 * Set 'useShared' to false to get different random behaviour every time.
 * 
 * @author Bhavik Gilbert(K21004990) and Heman Seegolam(K21003628)
 * @version (28/02/2022)
 */
public class Randomizer
{
    // The default seed for control of randomization.
    private static final int SEED = 1111;
    // A shared Random object, if required.
    private static final Random rand = new Random(SEED);
    // Determine whether a shared random generator is to be provided.
    private static final boolean useShared = false;

    /**
     * Constructor for objects of class Randomizer
     * All values are shared between randomizers
     */
    public Randomizer()
    {
    }

    /**
     * Provide a random generator.
     * 
     * @return A random object.
     */
    public static Random getRandom()
    {
        if(useShared) {
            return rand;
        }
        else {
            return new Random();
        }
    }

    /**
     * Reset the randomization.
     * This will have no effect if randomization is not through a shared Random generator.
     */
    public static void reset()
    {
        if(useShared) {
            rand.setSeed(SEED);
        }
    }
}
