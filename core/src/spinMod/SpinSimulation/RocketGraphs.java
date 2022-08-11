package spinMod.SpinSimulation;

import com.google.common.collect.HashBasedTable;
import com.orsoncharts.*;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Range;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.data.Dataset3DChangeEvent;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.LineXYZRenderer;
//import javafx.scene.chart.Chart;
import spinMod.Rocket;
import spinMod.Vectors.Vector;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public abstract class RocketGraphs {

    public Hashtable <String, Chart3DPanel> graphPanels;


    /**
     * Creates a RocketGraphs object. This object stores multiple
     * graphs that can be accessed using a String key.
     */
    public RocketGraphs(){
        graphPanels = new Hashtable<>();
    }

    /**
     * Creates and adds a graphPanel to the graphPanel hashtable or replaces
     * one if it already exists.
     *
     * @param dataAndName An ArrayList of location Vectors with String keys that
     *                    indicate the name of the data set.
     * @param graphName A String that indicates the name of the graph inside
     *                  the graph panel being created.
     * @param graphSubtitle A String that indicates the subtitle of the graph
     *                      inside the graph panel being created.
     */
    public abstract void addGraphPanel(Hashtable<String, ArrayList<Vector>> dataAndName, String graphName,
                                       String graphSubtitle);

    /**
     * Creates and adds a graphPanel to the graphPanel hashtable or replaces
     * one if it already exists.
     *
     * @param dataAndName A 2D String array containing the file location of
     *                    a data set and its corresponding name
     * @param graphName A String that indicates the name of the graph inside
     *                  the graph panel being created.
     * @param graphSubtitle A String that indicates the subtitle of the graph
     *                      inside the graph panel being created.
     */
    public abstract void addGraphPanel(String [][] dataAndName, String graphName, String graphSubtitle);

    /**
     * Gets a graph using the specified key given as an argument
     *
     * @param graphName A String key that corresponds to a graph in the
     *                  graphPanels HashTable.
     */
    public abstract Chart3DPanel getGraphPanel(String graphName);

}
