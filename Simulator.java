import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing dodos, humans, monkeys, tortoises and pigs.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Bhavik Gilbert and Heman Seegolam
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 99;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 66;
    // The probability that a human will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.1;
    // The probability that a monkey will be created in any given grid position.
    private static final double MONKEY_CREATION_PROBABILITY = 0.1;
    // The probability that a pig will be created in any given grid position.
    private static final double PIG_CREATION_PROBABILITY = 0.05;
    // The probability that a tortoise will be created in any given grid position.
    private static final double TORTOISE_CREATION_PROBABILITY = 0.05;
    // The probability that a dodo will be created in any given grid position.
    private static final double DODO_CREATION_PROBABILITY = 0.1;    
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.8;
    // The probability that it is sunny.
    private static final double SUNNY_PROBABILITY = 0.55;
    // The probability that it is raining.
    private static final double RAINY_PROBABILITY = 0.3;
    // The probability that it is foggy.
    private static final double FOGGY_PROBABILITY = 0.1;
    // The probability that it is snowing.
    private static final double SNOWY_PROBABILITY = 0.05;
    // The probability that a disease particle will be created in any given grid position.
    private static final double DISEASE_CREATION_PROBABILITY = 0.05;

    // List of actors in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    //runs main simulation
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        actors = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Pig.class, new Color(255,102,102));
        view.setColor(Human.class, new Color(153,102,0));
        view.setColor(Dodo.class, new Color(255,204,51));
        view.setColor(Monkey.class, new Color(51,0,0));
        view.setColor(Tortoise.class, new Color(0,153,0));
        view.setColor(Plant.class, Color.GREEN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (50 days).
     */
    public void runLongSimulation()
    {
        simulate(100);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each actor
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn actors.
        List<Actor> newActors = new ArrayList<>();
        // Changes weather every step
        Weather weather = randomWeather();
        // Let all actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            Actor actor = it.next();
            actor.act(newActors, step%2, weather);
            if(!actor.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born actors to the main lists.
        actors.addAll(newActors);

        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        actors.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with actors.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        int infectedCount = 0;

        //Gathers running probability for use in creation
        double[] totalProbabilities = {DODO_CREATION_PROBABILITY, HUMAN_CREATION_PROBABILITY, PIG_CREATION_PROBABILITY, MONKEY_CREATION_PROBABILITY, TORTOISE_CREATION_PROBABILITY};
        totalProbabilities =  getTotalProbability(totalProbabilities);
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                // Populate with animals and plants as per their probabilities    
                Location location = new Location(row, col);
                boolean infected = false;

                if (rand.nextDouble() <= totalProbabilities[0]){
                    Dodo dodo = new Dodo(true, field, location, infected);
                    actors.add(dodo);
                }
                else if(rand.nextDouble() <= totalProbabilities[1]){
                    Human human = new Human(true, field, location, infected);
                    actors.add(human);
                }
                else if(rand.nextDouble() <= totalProbabilities[2]){
                    Pig pig = new Pig(true, field, location, infected);
                    actors.add(pig);
                }
                else if (rand.nextDouble() <= totalProbabilities[3]){
                    Monkey monkey = new Monkey(true, field, location, infected);
                    actors.add(monkey);
                }
                else if (rand.nextDouble() <= totalProbabilities[4]){
                    Tortoise tortoise = new Tortoise( true, field, location, infected);
                    actors.add(tortoise);
                }

                if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY){
                    Plant plant = new Plant(true, field, location);
                    actors.add(plant);
                }
                if (rand.nextDouble() <= DISEASE_CREATION_PROBABILITY){
                    infected = true;
                    infectedCount += 1;
                }
            }
        }

        System.out.println(infectedCount);
        
        // Gives warning if spawn probability is above 1
        if(totalProbabilities[totalProbabilities.length-1] > 1){
            System.out.println("Your total spawn probability is above 1, there may be some unexpected errors in simulation as a result");
        }
    }

    /**
     * Generates running total probability list based on input
     * 
     * @param total Probability list
     * @return Running total probability list
    */
    private double[] getTotalProbability(double[] total){
        for(int i=0; i < total.length-1; i++){
            total[i+1]+=total[i];
        }

        return total;
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    /**
     * Generates a random weather from the Weather ENUM
     */
    private Weather randomWeather() {
        Random rand = Randomizer.getRandom();

        double[] totalProbabilities = {SNOWY_PROBABILITY, FOGGY_PROBABILITY, RAINY_PROBABILITY, SUNNY_PROBABILITY};
        totalProbabilities =  getTotalProbability(totalProbabilities);

        if (rand.nextDouble() <= totalProbabilities[0]){
            return Weather.SNOWY;
        }
        else if (rand.nextDouble() <= totalProbabilities[1]){
            return Weather.FOGGY;
        }
        else if (rand.nextDouble() <= totalProbabilities[2]){
            return Weather.RAINY;
        }
        else if (rand.nextDouble() <= totalProbabilities[3]){
            return Weather.SUNNY;
        }

        if (totalProbabilities[totalProbabilities.length-1] != 1) {
            System.out.println("Your total weather probability is not equal to 1, there may be some unexpected errors in simulation as a result");
        }

        return Weather.SUNNY;
    }
}
