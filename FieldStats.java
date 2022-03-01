import java.util.HashMap;
import java.util.ArrayList;

/**
 * This class collects and provides some statistical data on the state
 * of a field. It is flexible: it will create and maintain a counter
 * for any class of object that is found within the field.
 * 
 * @author Bhavik Gilbert and Heman Seegolam
 * @version (28/02/2022)
 */
public class FieldStats
{
    // Counters for each type of entity in the simulation.
    private static HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;
    // What actors to ignore in viability of running
    private ArrayList<Class> ignoreViable;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<>();
        countsValid = true;
        ignoreViable = new ArrayList<>();
        ignoreViable.add(Plant.class);
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**UNUSED, MADE FOR GRAPH
     * Get details of what is in the field.
     * @return A list describing what is in the field.
     */
    public HashMap<String,Integer> getPopulationNumerics(Field field) {
        HashMap<String, Integer> animalDesc = new HashMap<>();
        if (!countsValid) {
            generateCounts(field);
        }
        for (Class key : counters.keySet()) {
            Counter info = counters.get(key);
            animalDesc.put(info.getName(), info.getCount());
        }
        return animalDesc;
    }
    
    /**
     * Return the counters hash map.
     * @return The counters hash map.
     */
    public static HashMap<Class, Counter> getCounter()
    {
        return counters;
    }
    
    /**
     * Invalidate the current set of statistics; reset all 
     * counts to zero.
     */
    public void reset()
    {
        countsValid = false;
        for(Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * @param animalClass The class of animal to increment.
     */
    public void incrementCount(Class animalClass)
    {
        Counter count = counters.get(animalClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(animalClass.getName());
            counters.put(animalClass, count);
        }
        count.increment();
    }

    /**
     * Indicate that an animal count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        // How many counts are non-zero.
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }

        for(Class key : counters.keySet()) {
            if(!(ignoreViable.contains(key))){
                Counter info = counters.get(key);
                if(info.getCount() > 0){
                    nonZero++;
                }
            }
        }
        
        return nonZero > 1;
    }
    
    /**
     * Generate counts of the number of actors
     * These are not kept up to date a actors
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field)
    {
        reset();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}
