import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author Bhavik Gilbert(K21004990) and Heman Seegolam(K21003628)
 * @version (28/02/2022)
 */
public class SimulatorView extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String DAY_PREFIX = "Day: ";
    private final String TIME_PREFIX = "Time: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, timeLabel, population, infoLabel, visibleLabel, controlLabel, playbackLabel, stepControlLabel, speedControlLabel, weatherLabel;
    private JButton plantButton, humanButton, monkeyButton, pigButton, tortoiseButton, dodoButton, resetClearButton, shutSimulationButton, 
    pauseSimulationButton, playSimulationButton, resetSimulationButton, longSimulationButton, shortSimulationButton, oneStepButton,
    speedUpButton, slowDownButton, randomWeatherButton, sunnyButton, rainyButton, foggyButton, snowyButton;
    private FieldView fieldView;
    
    // A map for storing the current colors for participants in the simulation
    private Map<Class, Color> colors;
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> baseColors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width, Simulator simulator)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        baseColors = new LinkedHashMap<>();

        setTitle("Predator and Prey Simulation");
        
        stepLabel = new JLabel(DAY_PREFIX, JLabel.CENTER);
        timeLabel = new JLabel(TIME_PREFIX, JLabel.CENTER);
        infoLabel = new JLabel("  ", JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        visibleLabel= new JLabel("Visibility Controls", JLabel.CENTER);
        controlLabel = new JLabel("Simulator Controls", JLabel.CENTER);
        playbackLabel = new JLabel("Playback Controls", JLabel.CENTER);
        stepControlLabel = new JLabel("Step Controls", JLabel.CENTER);
        speedControlLabel = new JLabel("Speed Controls", JLabel.CENTER);
        weatherLabel = new JLabel("Weather Controls", JLabel.CENTER);

        plantButton = new JButton("Plant");
        plantButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Plant.class, simulator.getField());
            }
        });

        dodoButton = new JButton("Dodo");
        dodoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Dodo.class, simulator.getField());
            }
        });

        tortoiseButton = new JButton("Tortoise");
        tortoiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Tortoise.class, simulator.getField());
            }
        });

        humanButton = new JButton("Human");
        humanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Human.class, simulator.getField());
            }
        });

        monkeyButton = new JButton("Monkey");
        monkeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Monkey.class, simulator.getField());
            }
        });

        pigButton = new JButton("Pig");
        pigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleColor(Pig.class, simulator.getField());
            }
        });

        resetClearButton = new JButton("Reset Colours");
        resetClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetViewColor(simulator.getField());
            }
        });
        
        shutSimulationButton = new JButton("Shutdown Simulatior");
        shutSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.shutdownSimulation();
            }
        });

        oneStepButton = new JButton("Simulate One Step");
        oneStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.forceSimulateOneStep();
            }
        });

        resetSimulationButton = new JButton("Reset Field");
        resetSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.reset();
            }
        });

        longSimulationButton = new JButton("Long Simulation");
        longSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.runLongSimulation();
            }
        });

        shortSimulationButton = new JButton("Short Simulation");
        shortSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.runShortSimulation();
            }
        });

        pauseSimulationButton = new JButton("Pause Simulation");
        pauseSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setPauseSimulation(true);
            }
        });

        playSimulationButton= new JButton("Play Simulation");
        playSimulationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setPauseSimulation(false);
            }
        });

        speedUpButton = new JButton("Speed Up Simulation");
        speedUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.speedUpTimeDelay();
            }
        });

        slowDownButton = new JButton("Slow Down Simulation");
        slowDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.slowDownTimeDelay();
            }
        });

        randomWeatherButton= new JButton("Random Weather");
        randomWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setCurrentWeather(null);
            }
        });

        sunnyButton = new JButton("Sunny Weather");
        sunnyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setCurrentWeather(Weather.SUNNY);
            }
        });

        rainyButton = new JButton("Rainy Weather");
        rainyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setCurrentWeather(Weather.RAINY);
            }
        });

        foggyButton = new JButton("Foggy Weather");
        foggyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setCurrentWeather(Weather.FOGGY);
            }
        });

        snowyButton = new JButton("Snowy Weather");
        snowyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulator.setCurrentWeather(Weather.SNOWY);
            }
        });
        

        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        
        JPanel infoPane = new JPanel(new BorderLayout());
            infoPane.add(stepLabel, BorderLayout.WEST);
            infoPane.add(timeLabel, BorderLayout.EAST);
            infoPane.add(infoLabel, BorderLayout.CENTER);

        
        JPanel visionButtonPane = new JPanel(new BorderLayout());
            JPanel northVisionButtonPane = new JPanel(new BorderLayout());
            visionButtonPane.add(northVisionButtonPane, BorderLayout.NORTH);

                JPanel northVisionButtonPane1 = new JPanel(new BorderLayout());
                JPanel northVisionButtonPane2 = new JPanel(new BorderLayout());
                JPanel northVisionButtonPane3 = new JPanel(new BorderLayout());
                
                northVisionButtonPane.add(northVisionButtonPane1, BorderLayout.NORTH);
                northVisionButtonPane.add(northVisionButtonPane2, BorderLayout.CENTER);
                northVisionButtonPane.add(northVisionButtonPane3, BorderLayout.SOUTH);

                    northVisionButtonPane1.add(visibleLabel, BorderLayout.NORTH);
                    northVisionButtonPane1.add(plantButton, BorderLayout.CENTER);
                    northVisionButtonPane1.add(dodoButton, BorderLayout.SOUTH);

                    northVisionButtonPane2.add(tortoiseButton, BorderLayout.NORTH);
                    northVisionButtonPane2.add(humanButton, BorderLayout.CENTER);
                    northVisionButtonPane2.add(monkeyButton, BorderLayout.SOUTH);

                    northVisionButtonPane3.add(pigButton, BorderLayout.NORTH);
                    northVisionButtonPane3.add(resetClearButton, BorderLayout.CENTER);

            JPanel weatherButtonPane = new JPanel(new BorderLayout());
            visionButtonPane.add(weatherButtonPane, BorderLayout.SOUTH);

                JPanel weatherButtonPane1 = new JPanel(new BorderLayout());
                JPanel weatherButtonPane2 = new JPanel(new BorderLayout());
                
                weatherButtonPane.add(weatherButtonPane1, BorderLayout.NORTH);
                weatherButtonPane.add(weatherButtonPane2, BorderLayout.CENTER);

                    weatherButtonPane1.add(weatherLabel, BorderLayout.NORTH);
                    weatherButtonPane1.add(randomWeatherButton, BorderLayout.CENTER);
                    weatherButtonPane1.add(sunnyButton, BorderLayout.SOUTH);

                    weatherButtonPane2.add(rainyButton, BorderLayout.NORTH);
                    weatherButtonPane2.add(foggyButton, BorderLayout.CENTER);
                    weatherButtonPane2.add(snowyButton, BorderLayout.SOUTH);
                    
        JPanel controlButtonPane = new JPanel(new BorderLayout());
            JPanel northControlButtonPane = new JPanel(new BorderLayout());
            controlButtonPane.add(northControlButtonPane, BorderLayout.NORTH);

                JPanel northControlButtonPane1 = new JPanel(new BorderLayout());
                JPanel northControlButtonPane2 = new JPanel(new BorderLayout());
                JPanel northControlButtonPane3 = new JPanel(new BorderLayout());
                northControlButtonPane.add(northControlButtonPane1, BorderLayout.NORTH);
                northControlButtonPane.add(northControlButtonPane2, BorderLayout.CENTER);
                northControlButtonPane.add(northControlButtonPane3, BorderLayout.SOUTH);

                    northControlButtonPane1.add(playbackLabel, BorderLayout.NORTH);
                    northControlButtonPane1.add(playSimulationButton, BorderLayout.CENTER);
                    northControlButtonPane1.add(pauseSimulationButton, BorderLayout.SOUTH);

                    northControlButtonPane2.add(speedControlLabel, BorderLayout.NORTH);
                    northControlButtonPane2.add(speedUpButton, BorderLayout.CENTER);
                    northControlButtonPane2.add(slowDownButton, BorderLayout.SOUTH);
            
            JPanel centerControlButtonPane = new JPanel(new BorderLayout());
            controlButtonPane.add(centerControlButtonPane, BorderLayout.CENTER);
                JPanel nCenterControlButtonPane = new JPanel(new BorderLayout());
                centerControlButtonPane.add(nCenterControlButtonPane, BorderLayout.NORTH);

                    JPanel nCenterControlButtonPane1 = new JPanel(new BorderLayout());
                    JPanel nCenterControlButtonPane2 = new JPanel(new BorderLayout());
                    JPanel nCenterControlButtonPane3 = new JPanel(new BorderLayout());
                    nCenterControlButtonPane.add(nCenterControlButtonPane1, BorderLayout.NORTH);
                    nCenterControlButtonPane.add(nCenterControlButtonPane2, BorderLayout.CENTER);
                    nCenterControlButtonPane.add(nCenterControlButtonPane3, BorderLayout.SOUTH);

                    nCenterControlButtonPane1.add(stepControlLabel, BorderLayout.NORTH);

                    nCenterControlButtonPane2.add(oneStepButton, BorderLayout.NORTH);
                    nCenterControlButtonPane2.add(shortSimulationButton, BorderLayout.CENTER);
                    nCenterControlButtonPane2.add(longSimulationButton, BorderLayout.SOUTH);        

        JPanel southControlButtonPane = new JPanel(new BorderLayout());
        controlButtonPane.add(southControlButtonPane, BorderLayout.SOUTH);

            JPanel southControlButtonPane1 = new JPanel(new BorderLayout());
            southControlButtonPane.add(southControlButtonPane1, BorderLayout.NORTH);

            southControlButtonPane1.add(controlLabel, BorderLayout.NORTH);
            southControlButtonPane1.add(resetSimulationButton, BorderLayout.CENTER);
            southControlButtonPane1.add(shutSimulationButton, BorderLayout.SOUTH);
                
            

        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        contents.add(visionButtonPane, BorderLayout.EAST);
        contents.add(controlButtonPane, BorderLayout.WEST);
        pack();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of actor.
     * 
     * @param actorClass The actor's Class object.
     * @param color      The color to be used for the given class.
     */
    public void setColor(Class actorClass, Color color)
    {
        colors.put(actorClass, color);
        baseColors.put(actorClass, color);
    }

    /**
     * Toggles the colour of the actor between clear and coloured
     * 
     * @param actorClass The actor's Class object
     */
    private void toggleColor(Class actorClass, Field field)
    {
        if(EMPTY_COLOR.equals(colors.get(actorClass))){
            colors.replace(actorClass, baseColors.get(actorClass));
        }
        else{
            colors.replace(actorClass, EMPTY_COLOR);
        }
        
        updatePanel(field, false);
    }

    private void resetViewColor(Field field){
        baseColors.forEach((key,entry) -> colors.replace(key,entry)); 
        updatePanel(field, false);
    }

    /**
     * Display a short information label at the top of the window.
     */
    public void setInfoText(String text)
    {
        infoLabel.setText(text);
    }

    /**
     * @param actorClass An actor class
     * @return The color to be used for a given class of actor.
     */
    private Color getColor(Class actorClass)
    {
        Color col = colors.get(actorClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * 
     * @param step Which iteration step it is.
     * @param totalDays The number of steps the simulator is running for
     * @param field The field whose status is to be displayed.
     * @param info Other information of the board such as weather
     */
    public void showStatus(int step, int totalSteps, Field field, Weather weather, int virusCount)
    {
        if(!isVisible()) {
            setVisible(true);
        }
        
        // Displays day number
        stepLabel.setText(DAY_PREFIX + (step+1)/2 + "/" + totalSteps/2);
        // Computes and displays time of day
        String time = "";
        if(step%2 == 0){
            time = "Night";
        }
        else if(step%2 == 1){
            time = "Day";
        }
        timeLabel.setText(TIME_PREFIX + time);

        // Displays additional info such as weather and virus numbers
        setInfoText("Weather:" + weather + "   Infected :" + virusCount);
        stats.reset();
        
        updatePanel(field, true);
    }

    /**
     * Clears and repaints the field on the viewer
     * 
     * @param field The field the viewer is currently representing
     * @param newStep Defines whether a new step has occured or not
     */
    private void updatePanel(Field field, boolean newStep)
    {
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Actor actor = (Actor) field.getObjectAt(row, col);
                if (actor != null) {
                    if(newStep){
                       stats.incrementCount(actor.getClass()); 
                    }
                    fieldView.drawMark(col, row, getColor(actor.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * 
     * @param field The field the viewer is displaying
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        @Override
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
