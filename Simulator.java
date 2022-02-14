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
    private static final int DEFAULT_WIDTH = 150;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 100;
    // The probability that a human will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.05;
    // The probability that a monkey will be created in any given grid position.
    private static final double MONKEY_CREATION_PROBABILITY = 0.05;
    // The probability that a pig will be created in any given grid position.
    private static final double PIG_CREATION_PROBABILITY = 0.05;
    // The probability that a tortoise will be created in any given grid position.
    private static final double TORTOISE_CREATION_PROBABILITY = 0.02;
    // The probability that a dodo will be created in any given grid position.
    private static final double DODO_CREATION_PROBABILITY = 0.02;    
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.05;

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
     * (4000 steps).
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
        // Let all actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            Actor actor = it.next();
            actor.act(newActors, step%2, randomWeather());
            if(!actor.isAlive()) {
                it.remove();
            }
        }
        
        //TEMPORARY - CORRECT PLANT OBJECT COUNTER
        //System.out.println(Plant.getPlantCount());

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
        int total;

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                total = 0;
                if (rand.nextDouble() <= DODO_CREATION_PROBABILITY + total) {
                    total += DODO_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Dodo dodo = new Dodo(true, field, location, false);
                    actors.add(dodo);
                }
                else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY + total) {
                    total += HUMAN_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Human human = new Human(true, field, location, false);
                    actors.add(human);
                }
                else if(rand.nextDouble() <= PIG_CREATION_PROBABILITY + total) {
                    total += PIG_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Pig pig = new Pig(true, field, location, false);
                    actors.add(pig);
                }
                else if (rand.nextDouble() <= MONKEY_CREATION_PROBABILITY + total) {
                    total += MONKEY_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Monkey monkey = new Monkey(true, field, location, false);
                    actors.add(monkey);
                }
                else if (rand.nextDouble() <= TORTOISE_CREATION_PROBABILITY + total) {
                    total += TORTOISE_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Tortoise tortoise = new Tortoise(true, field, location, false);
                    actors.add(tortoise);
                }
                else if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY + total) {
                    total += PLANT_CREATION_PROBABILITY;

                    Location location = new Location(row, col);
                    Plant plant = new Plant(true, field, location, true);
                    actors.add(plant);
                }
                // else leave the location empty.
            }
        }
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
    protected Weather randomWeather() {
        Random rand = Randomizer.getRandom();
        return Weather.values()[rand.nextInt(Weather.values().length)];
    }
}
