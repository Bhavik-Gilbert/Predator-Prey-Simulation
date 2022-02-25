import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author Bhavik Gilbert amd Heman Seegolam
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
    private JLabel stepLabel, timeLabel, population, infoLabel, visibleLabel, controlLabel;
    private JButton plantButton, humanButton, monkeyButton, pigButton, tortoiseButton, dodoButton, resetClearButton, shutSimulationButton, 
    pauseSimulationButton, playSimulationButton, resetSimulationButton, longSimulationButton, shortSimulationButton;
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

        plantButton = new JButton("Plant");
        plantButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Plant.class, simulator.getField());
            }
        });

        dodoButton = new JButton("Dodo");
        dodoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Dodo.class, simulator.getField());
            }
        });

        tortoiseButton = new JButton("Tortoise");
        tortoiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Tortoise.class, simulator.getField());
            }
        });

        humanButton = new JButton("Human");
        humanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Human.class, simulator.getField());
            }
        });

        monkeyButton = new JButton("Monkey");
        monkeyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Monkey.class, simulator.getField());
            }
        });

        pigButton = new JButton("Pig");
        pigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColor(Pig.class, simulator.getField());
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

        

        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        
        JPanel infoPane = new JPanel(new BorderLayout());
            infoPane.add(stepLabel, BorderLayout.WEST);
            infoPane.add(timeLabel, BorderLayout.EAST);
            infoPane.add(infoLabel, BorderLayout.CENTER);

        
        JPanel buttonPane = new JPanel(new BorderLayout());
            JPanel northButtonPane = new JPanel(new BorderLayout());
            buttonPane.add(northButtonPane, BorderLayout.NORTH);

                JPanel northButtonPane1 = new JPanel(new BorderLayout());
                JPanel northButtonPane2 = new JPanel(new BorderLayout());
                JPanel northButtonPane3 = new JPanel(new BorderLayout());
                
                northButtonPane.add(northButtonPane1, BorderLayout.NORTH);
                northButtonPane.add(northButtonPane2, BorderLayout.CENTER);
                northButtonPane.add(northButtonPane3, BorderLayout.SOUTH);

                    northButtonPane1.add(visibleLabel, BorderLayout.NORTH);
                    northButtonPane1.add(plantButton, BorderLayout.CENTER);
                    northButtonPane1.add(dodoButton, BorderLayout.SOUTH);

                    northButtonPane2.add(tortoiseButton, BorderLayout.NORTH);
                    northButtonPane2.add(humanButton, BorderLayout.CENTER);
                    northButtonPane2.add(monkeyButton, BorderLayout.SOUTH);

                    northButtonPane3.add(pigButton, BorderLayout.NORTH);
                    northButtonPane3.add(resetClearButton, BorderLayout.CENTER);
            
            JPanel southButtonPane = new JPanel(new BorderLayout());
            buttonPane.add(southButtonPane, BorderLayout.SOUTH);

                JPanel southButtonPane1 = new JPanel(new BorderLayout());
                JPanel southButtonPane2 = new JPanel(new BorderLayout());
                JPanel southButtonPane3 = new JPanel(new BorderLayout());
                southButtonPane.add(southButtonPane1, BorderLayout.NORTH);
                southButtonPane.add(southButtonPane2, BorderLayout.CENTER);
                southButtonPane.add(southButtonPane3, BorderLayout.SOUTH);

                southButtonPane1.add(controlLabel, BorderLayout.NORTH);
                southButtonPane1.add(resetSimulationButton, BorderLayout.SOUTH);

                southButtonPane2.add(playSimulationButton, BorderLayout.NORTH);
                southButtonPane2.add(pauseSimulationButton, BorderLayout.CENTER);

                southButtonPane3.add(shortSimulationButton, BorderLayout.NORTH);
                southButtonPane3.add(longSimulationButton, BorderLayout.CENTER);
                southButtonPane3.add(shutSimulationButton, BorderLayout.SOUTH);
                
            

        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        contents.add(buttonPane, BorderLayout.EAST);
        pack();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of actor.
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
    private void changeColor(Class actorClass, Field field)
    {
        if(EMPTY_COLOR.equals(colors.get(actorClass))){
            colors.replace(actorClass, baseColors.get(actorClass));
        }
        else{
            colors.replace(actorClass, EMPTY_COLOR);
        }
        
        updatePanel(field);
    }

    private void resetViewColor(Field field){
        baseColors.forEach((key,entry) -> colors.replace(key,entry)); 
        updatePanel(field);
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
     * @param step Which iteration step it is.
     * @param totalDays The number of steps the simulator is running for
     * @param field The field whose status is to be displayed.
     * @param info Other information of the board such as weather
     */
    public void showStatus(int step, int totalSteps, Field field, String info)
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

        // Displays additional info such as weather
        setInfoText(info);
        stats.reset();
        
        fieldView.preparePaint();
        updatePanel(field);
    }

    private void updatePanel(Field field){
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object actor = field.getObjectAt(row, col);
                if (actor != null) {
                    stats.incrementCount(actor.getClass());
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

    private abstract class PieChartSample extends Application {

        public void start(Stage stage, Map Actors) {
            Scene scene = new Scene(new Group());
            stage.setTitle("Population");
            stage.setWidth(500);
            stage.setHeight(500);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    //actors.forEach((k,v)->{new PieChart.Data(k, v);}));
                    new PieChart.Data("Grapefruit", 13),
                    new PieChart.Data("Oranges", 25),
                    new PieChart.Data("Plums", 10),
                    new PieChart.Data("Pears", 22),
                    new PieChart.Data("Apples", 30));
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("Population");

            ((Group) scene.getRoot()).getChildren().add(chart);
            stage.setScene(scene);
            stage.show();
        }
    }
}
