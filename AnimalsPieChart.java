import java.awt.*;
import javax.swing.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class PieChart here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AnimalsPieChart extends JFrame
{
    // GUI variables for pie chart
    ObservableList<PieChart.Data> pieChartData;
    JFXPanel dataPanel = new JFXPanel();
    private ArrayList<String> animalsList = new ArrayList<>();
    
    /**
     * Constructor for objects of class PieChart
     */
    public AnimalsPieChart()
    {
        createChartData();
    
        JFrame frame = new JFrame();
        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(() -> createChart(fxPanel));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(fxPanel, BorderLayout.CENTER);
                
        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    /**
     * Creates the pie chart with the correct details.
     * @param the panel that the pie chart shall be displayed on.
     */
    private void createChartData() 
    {
        HashMap<Class, Counter> counters = new HashMap<>(); 
        counters = FieldStats.getCounter();
        for (Class key : counters.keySet()){
            System.out.println(key.toString());
            System.out.println(counters.get(key).getCount());
            addData(key.toString().substring(6), counters.get(key).getCount());
        }
    }
    
    /**
     * Creates the pie chart with the correct details.
     * @param the panel that the pie chart shall be displayed on.
     */
    private void createChart(JFXPanel dataPanel) 
    {
        this.pieChartData = FXCollections.observableArrayList();
        String test1 = "Test1";
        int test2 = 300;
        addData(test1, test2);
        
        addData("Test2", 5000);
        addData("Test3", 2000);
        
        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Animals");
        final Label caption = new Label("");
        
        Group root = new Group();

        root.getChildren().add(pieChart);
        
        Scene scene = new Scene(root);
        dataPanel.setScene(scene); 
    }
    
    /**
     * Adds data, i.e. a name and an associated number value to the pie chart.
     * @param the name and number value.
     */
    public void addData(String name, int value)
    {
        System.out.println(name);
        System.out.println(value);
        pieChartData.add(new javafx.scene.chart.PieChart.Data(name, value));
    }
}
