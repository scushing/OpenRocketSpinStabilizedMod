package spinMod.SpinSimulation;

import com.orsoncharts.*;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.data.Dataset3DChangeEvent;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.LineXYZRenderer;
import spinMod.Rocket;
import spinMod.Vectors.Vector;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;


public class RocketGraphing{

    private static final ArrayList<double[]> timeAccelerationXYZ = new ArrayList<>();

    private XYZSeries xyzSeries;
    private XYZSeriesCollection xyzSeriesCollection;
    private NumberAxis3D xAxis, yAxis, zAxis;
    private XYZPlot xyzPlot;
    private Chart3D chart3D;
    private Chart3DPanel chart3DPanel;

    private double[] maxPositionValues;
    private double[] minPositionValues;

    public RocketGraphing(String graphName, String graphSubtitle){
        //Series Init
        //xyzSeries = new XYZSeries("default");
        xyzSeriesCollection = new XYZSeriesCollection();
        //xyzSeriesCollection.add(xyzSeries);

        //Axis Init
        xAxis = new NumberAxis3D("x");
        yAxis = new NumberAxis3D("y");
        zAxis = new NumberAxis3D("z");

        //Plot and Chart Init
        xyzPlot = new XYZPlot(xyzSeriesCollection, new LineXYZRenderer(), xAxis, yAxis, zAxis);
        chart3D = new Chart3D(graphName, graphSubtitle, xyzPlot);
        chart3DPanel = new Chart3DPanel(chart3D);

        //Axes Defined
        xAxis.configureAsXAxis(xyzPlot);
        yAxis.configureAsYAxis(xyzPlot);
        zAxis.configureAsZAxis(xyzPlot);

        //Min & Max Init
        maxPositionValues = new double[] {1,1,1};
        minPositionValues = new double [] {-1,-1,-1};
    }

    public XYZSeries getXyzSeries() {
        return xyzSeries;
    }

    public void setXyzSeries(XYZSeries xyzSeries) {
        this.xyzSeries = xyzSeries;
    }

    public XYZSeriesCollection getXyzSeriesCollection() {
        return xyzSeriesCollection;
    }

    public void setXyzSeriesCollection(XYZSeriesCollection xyzSeriesCollection) {
        this.xyzSeriesCollection = xyzSeriesCollection;
    }

    public NumberAxis3D getxAxis() {
        return xAxis;
    }

    public void setxAxis(NumberAxis3D xAxis) {
        this.xAxis = xAxis;
    }

    public NumberAxis3D getyAxis() {
        return yAxis;
    }

    public void setyAxis(NumberAxis3D yAxis) {
        this.yAxis = yAxis;
    }

    public NumberAxis3D getzAxis() {
        return zAxis;
    }

    public void setzAxis(NumberAxis3D zAxis) {
        this.zAxis = zAxis;
    }

    public XYZPlot getXyzPlot() {
        return xyzPlot;
    }

    public void setXyzPlot(XYZPlot xyzPlot) {
        this.xyzPlot = xyzPlot;
    }

    public Chart3D getChart3D() {
        return chart3D;
    }

    public void setChart3D(Chart3D chart3D) {
        this.chart3D = chart3D;
    }

    public Chart3DPanel getChart3DPanel() {
        return chart3DPanel;
    }

    public void setChart3DPanel(Chart3DPanel chart3DPanel) {
        this.chart3DPanel = chart3DPanel;
    }

    public double[] getMaxPositionValues() {
        return maxPositionValues;
    }

    public void setMaxPositionValues(double[] maxPositionValues) {
        this.maxPositionValues = maxPositionValues;
    }

    public double[] getMinPositionValues() {
        return minPositionValues;
    }

    public void setMinPositionValues(double[] minPositionValues) {
        this.minPositionValues = minPositionValues;
    }

    public void updateGraphRange(){
        xAxis.setRange(new Range(Math.floor(minPositionValues[0]), Math.ceil(maxPositionValues[0])));
        yAxis.setRange(new Range(Math.floor(minPositionValues[1]), Math.ceil(maxPositionValues[1])));
        zAxis.setRange(new Range(Math.floor(minPositionValues[2]), Math.ceil(maxPositionValues[2])));
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

    public void readTimeAccelerationXYZData(String fileName) {
        timeAccelerationXYZ.clear();

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
    }

    public double newPosition(double currentPosition, double currentVelocity, double timeDifference,
                              double currentAcceleration){
        currentPosition = currentPosition + (currentVelocity * timeDifference) +
                ((0.5) * (currentAcceleration * Math.pow(timeDifference, 2)));
        return currentPosition;
    }

    public double newVelocity(double startingVelocity, double currentAcceleration, double timeDifference){
        return (startingVelocity + currentAcceleration * timeDifference);
    }

    private void addPositionToSeries() {
        double [] currentPosition = {0,0,0};
        double [] currentVelocity = {0,0,0};
        double [] currentAcceleration;
        double currentTime;
        double startingTime = 0.0;
        double timeDifference;
        xyzSeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);

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

            if(minPositionValues[0] > currentPosition[0]){
                minPositionValues[0] = currentPosition[0];
            }
            if(minPositionValues[1] > currentPosition[1]){
                minPositionValues[1] = currentPosition[1];
            }
            if(minPositionValues[2] > currentPosition[2]){
                minPositionValues[2] = currentPosition[2];
            }

            if(maxPositionValues[0] < currentPosition[0]){
                maxPositionValues[0] = currentPosition[0];
            }
            if(maxPositionValues[1] < currentPosition[1]){
                maxPositionValues[1] = currentPosition[1];
            }
            if(maxPositionValues[2] < currentPosition[2]){
                maxPositionValues[2] = currentPosition[2];
            }

            xyzSeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);
        }


    }

    private void addPositionToSeries(ArrayList<Vector> positionVectors){
        for (int i = positionVectors.size() - 1; i >= 0; --i) {
            if (minPositionValues[0] > positionVectors.get(i).getI()) {
                minPositionValues[0] = positionVectors.get(i).getI();
            }
            if (minPositionValues[1] > positionVectors.get(i).getJ()) {
                minPositionValues[1] = positionVectors.get(i).getJ();
            }
            if (minPositionValues[2] > positionVectors.get(i).getK()) {
                minPositionValues[2] = positionVectors.get(i).getK();
            }

            if (maxPositionValues[0] < positionVectors.get(i).getI()) {
                maxPositionValues[0] = positionVectors.get(i).getI();
            }
            if (maxPositionValues[1] < positionVectors.get(i).getJ()) {
                maxPositionValues[1] = positionVectors.get(i).getJ();
            }
            if (maxPositionValues[2] < positionVectors.get(i).getK()) {
                maxPositionValues[2] = positionVectors.get(i).getK();
            }

            xyzSeries.add(positionVectors.get(i).getI(), positionVectors.get(i).getJ(), positionVectors.get(i).getK());
        }
    }

    public void updatePositionSeries(){
        xyzSeries = new XYZSeries("default");
        addPositionToSeries();
        xyzSeriesCollection.add(xyzSeries);
        updateGraphRange();
    }

    public void updatePositionSeries(ArrayList<Vector> positionVectors, String SeriesName){
        xyzSeries = new XYZSeries(SeriesName);
        addPositionToSeries(positionVectors);
        xyzSeriesCollection.add(xyzSeries);
        updateGraphRange();
    }
}
