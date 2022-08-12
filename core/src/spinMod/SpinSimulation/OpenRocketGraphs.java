package spinMod.SpinSimulation;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Range;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.LineXYZRenderer;
import com.orsoncharts.util.Orientation;
import spinMod.Vectors.Vector;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;

public class OpenRocketGraphs extends RocketGraphs{

    @Override
    public void addGraphPanel(Hashtable<String, ArrayList<Vector>> dataAndName, String graphName,
                              String graphSubtitle) {
        if (graphPanels.contains(graphName)){
            graphPanels.replace(graphName, createGraphPanel(dataAndName, graphName, graphSubtitle));
        } else {
            graphPanels.put(graphName, createGraphPanel(dataAndName, graphName, graphSubtitle));
        }
    }

    @Override
    public void addGraphPanel(String [][] dataAndName, String graphName, String graphSubtitle) {
        if (graphPanels.contains(graphName)){
            graphPanels.replace(graphName, createGraphPanel(dataAndName, graphName, graphSubtitle));
        } else {
            graphPanels.put(graphName, createGraphPanel(dataAndName, graphName, graphSubtitle));
        }
    }

    private Chart3DPanel createGraphPanel(Hashtable<String, ArrayList<Vector>> dataAndName, String graphName,
                                          String graphSubtitle){
        return (new Chart3DPanel(createGraph(dataAndName, graphName, graphSubtitle)));
    }

    private Chart3DPanel createGraphPanel(String [][] dataAndName, String graphName, String graphSubtitle){
        return (new Chart3DPanel(createGraph(dataAndName, graphName, graphSubtitle)));
    }

    private Chart3D createGraph(Hashtable<String, ArrayList<Vector>> dataAndName, String graphName,
                                String graphSubtitle){
        Chart3D chart3D = new Chart3D(graphName, graphSubtitle, createXYZPlot(dataAndName));

        //Chart Settings
        chart3D.setLegendOrientation(Orientation.VERTICAL);
        chart3D.getViewPoint().moveUpDown(-(1.9 * Math.PI)/5);
        chart3D.getViewPoint().panLeftRight(-(1.3 * Math.PI)/5);
        chart3D.setChartBoxColor(Color.DARK_GRAY);

        return chart3D;
    }

    private Chart3D createGraph(String [][] dataAndName, String graphName, String graphSubtitle){
        Chart3D chart3D = new Chart3D(graphName, graphSubtitle, createXYZPlot(dataAndName));

        //Charts Settings
        chart3D.setLegendOrientation(Orientation.VERTICAL);
        chart3D.getViewPoint().moveUpDown(-(1.9 * Math.PI)/5);
        chart3D.getViewPoint().panLeftRight(-(1.3 * Math.PI)/5);
        chart3D.setChartBoxColor(Color.DARK_GRAY);

        return chart3D;
    }

    private XYZSeries createSeries(ArrayList<Vector> data, String name){
        XYZSeries xyzSeries = new XYZSeries(name);
        for (Vector datum : data) {
            xyzSeries.add(datum.getI(), datum.getJ(), datum.getK());
        }
        return xyzSeries;
    }

    private XYZSeries createSeries(String data, String name){

        double [] currentPosition = {0,0,0};
        double [] currentVelocity = {0,0,0};
        double [] currentAcceleration;
        double currentTime;
        double startingTime = 0.0;
        double timeDifference;

        XYZSeries xyzSeries = new XYZSeries(name);
        xyzSeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);

        ArrayList<double []> timeAccelerationXYZ = readTimeAccelerationXYZData(data);

        for (double [] set : timeAccelerationXYZ) {
            currentTime = set[0];
            timeDifference = currentTime - startingTime;
            startingTime = currentTime;

            currentAcceleration = Arrays.copyOfRange(set, 1, set.length);

            currentVelocity[0] = newVelocity(currentVelocity[0], currentAcceleration[0], timeDifference);
            currentVelocity[1] = newVelocity(currentVelocity[1], currentAcceleration[1], timeDifference);
            currentVelocity[2] = newVelocity(currentVelocity[2], currentAcceleration[2], timeDifference);

            currentPosition[0] = newPosition(currentPosition[0], currentVelocity[0], timeDifference,
                    currentAcceleration[0]);
            currentPosition[1] = newPosition(currentPosition[1], currentVelocity[1], timeDifference,
                    currentAcceleration[1]);
            currentPosition[2] = newPosition(currentPosition[2], currentVelocity[2], timeDifference,
                    currentAcceleration[2]);

            xyzSeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);
        }

        return xyzSeries;
    }

    private XYZSeriesCollection createSeriesCollection(Hashtable<String, ArrayList<Vector>> dataAndName){

        XYZSeriesCollection xyzSeriesCollection = new XYZSeriesCollection();

        Set<String> setOfKeys = dataAndName.keySet();
        for (String key : setOfKeys){
            xyzSeriesCollection.add(createSeries(dataAndName.get(key), key));
        }

        return xyzSeriesCollection;
    }

    private XYZSeriesCollection createSeriesCollection(String [][] dataAndName){
        XYZSeriesCollection xyzSeriesCollection = new XYZSeriesCollection();

        for (String [] series : dataAndName) {
            xyzSeriesCollection.add(createSeries(series[0], series[1]));
        }

        return xyzSeriesCollection;
    }

    private XYZPlot createXYZPlot(Hashtable<String, ArrayList<Vector>> dataAndName){

        NumberAxis3D xAxis = new NumberAxis3D("x");
        NumberAxis3D yAxis = new NumberAxis3D("y");
        NumberAxis3D zAxis = new NumberAxis3D("z");

        XYZSeriesCollection xyzSeriesCollection = createSeriesCollection(dataAndName);

        double [][] minMaxAxisValues = minMaxAxisValues(xyzSeriesCollection);
        xAxis.setRange(new Range(Math.floor(minMaxAxisValues[0][0]), Math.ceil(minMaxAxisValues[0][1])));
        yAxis.setRange(new Range(Math.floor(minMaxAxisValues[1][0]), Math.ceil(minMaxAxisValues[1][1])));
        zAxis.setRange(new Range(Math.floor(minMaxAxisValues[2][0]), Math.ceil(minMaxAxisValues[2][1])));

        LineXYZRenderer lineXYZRenderer = new LineXYZRenderer();

        XYZPlot xyzPlot = new XYZPlot(xyzSeriesCollection, lineXYZRenderer, xAxis, yAxis, zAxis);

        xAxis.configureAsXAxis(xyzPlot);
        xAxis.configureAsYAxis(xyzPlot);
        xAxis.configureAsZAxis(xyzPlot);

        return xyzPlot;
    }

    private XYZPlot createXYZPlot(String [][] dataAndName){
        NumberAxis3D xAxis = new NumberAxis3D("x");
        NumberAxis3D yAxis = new NumberAxis3D("y");
        NumberAxis3D zAxis = new NumberAxis3D("z");

        XYZSeriesCollection xyzSeriesCollection = createSeriesCollection(dataAndName);

        double [][] minMaxAxisValues = minMaxAxisValues(xyzSeriesCollection);
        xAxis.setRange(new Range(Math.floor(minMaxAxisValues[0][0]), Math.ceil(minMaxAxisValues[0][1])));
        yAxis.setRange(new Range(Math.floor(minMaxAxisValues[1][0]), Math.ceil(minMaxAxisValues[1][1])));
        zAxis.setRange(new Range(Math.floor(minMaxAxisValues[2][0]), Math.ceil(minMaxAxisValues[2][1])));

        XYZPlot xyzPlot = new XYZPlot(xyzSeriesCollection, new LineXYZRenderer(), xAxis, yAxis, zAxis);

        xAxis.configureAsXAxis(xyzPlot);
        xAxis.configureAsYAxis(xyzPlot);
        xAxis.configureAsZAxis(xyzPlot);

        return xyzPlot;
    }

    @Override
    public Chart3DPanel getGraphPanel(String graphName) {
        return graphPanels.get(graphName);
    }

    private double [][] minMaxAxisValues(XYZSeriesCollection xyzSeriesCollection){
        double [][] minMaxAxisValues = new double[][] {
                {-1,1},
                {-1,1},
                {-1,1}
        };

        for(int i = 0; i < xyzSeriesCollection.getSeriesCount(); ++i){
            XYZSeries xyzSeries = xyzSeriesCollection.getSeries(i);
            double xVal, yVal, zVal;
            for (int j = 0; j < xyzSeries.getItemCount(); ++j){
                xVal = xyzSeries.getXValue(j);
                yVal = xyzSeries.getYValue(j);
                zVal = xyzSeries.getZValue(j);

                //Checks for min values
                if(minMaxAxisValues[0][0] > xVal){
                    minMaxAxisValues[0][0] = xVal;
                }
                if(minMaxAxisValues[1][0] > yVal){
                    minMaxAxisValues[1][0] = yVal;
                }
                if(minMaxAxisValues[2][0] > zVal){
                    minMaxAxisValues[2][0] = zVal;
                }

                //Checks for max values
                if(minMaxAxisValues[0][1] < xVal){
                    minMaxAxisValues[0][1] = xVal;
                }
                if(minMaxAxisValues[1][1] < yVal){
                    minMaxAxisValues[1][1] = yVal;
                }
                if(minMaxAxisValues[2][1] < zVal){
                    minMaxAxisValues[2][1] = zVal;
                }
            }
        }
        return minMaxAxisValues;
    }

    private double newPosition(double currentPosition, double currentVelocity, double timeDifference,
                              double currentAcceleration){
        currentPosition = currentPosition + (currentVelocity * timeDifference) +
                ((0.5) * (currentAcceleration * Math.pow(timeDifference, 2)));
        return currentPosition;
    }

    private double newVelocity(double startingVelocity, double currentAcceleration, double timeDifference){
        return (startingVelocity + currentAcceleration * timeDifference);
    }

    private ArrayList<double[]> readTimeAccelerationXYZData(String fileName) {
        ArrayList<double[]> timeAccelerationXYZ = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = bufferedReader.readLine();
            String [] lineArray = new String[4];
            while(line != null) {

                if(line.charAt(0) == 'T'){
                    lineArray[0] = line.replace(" ","").split(":")[1];
                } else if(line.charAt(0) == 'A') {
                    String [] splitLine = removeLabels(line);
                    lineArray[1] = splitLine[0];
                    lineArray[2] = splitLine[1];
                    lineArray[3] = splitLine[2];
                    double [] lineArrayConverted = new double[4];
                    for (int i = 0; i < lineArray.length; ++i) {
                        if(i == lineArray.length - 1){
                            double tempInt = Double.parseDouble(lineArray[i]) - 9.81;
                            lineArrayConverted[i] = tempInt;
                        }
                        double tempInt = Double.parseDouble(lineArray[i]);
                        lineArrayConverted[i] = tempInt;
                    }
                    timeAccelerationXYZ.add(lineArrayConverted);
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return timeAccelerationXYZ;
    }

    private String [] removeLabels(String item){
        item = item.replace("Acceleration:", "");
        item = item.replace("X:", "");
        item = item.replace("Y:", "");
        item = item.replace("Z:", "");
        item = item.replace("m/s^2", "");
        item = item.replace(" ", "");
        return item.split(",");
    }
}
