package spinMod.SpinSimulation;

import com.orsoncharts.*;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.LineXYZRenderer;
import spinMod.Vectors.Vector;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;


public class RocketGraphing {

    private static final ArrayList<double[]> timeAccelerationXYZ = new ArrayList<>();

    public static XYZSeries mySeries = new XYZSeries("v");

    public static XYZSeriesCollection myData = new XYZSeriesCollection();

    public static NumberAxis3D xAxis = new NumberAxis3D("x");
    public static NumberAxis3D yAxis = new NumberAxis3D("y");
    public static NumberAxis3D zAxis = new NumberAxis3D("z");

    public static double[] maxPositionValues = {1,1,1};
    public static double[] minPositionValues = {-1,-1,-1};

    public static XYZPlot xyzPlot;

    public static Chart3D myChart;

    public static Chart3DPanel myChartPanel;

    public static void setUpGraph(){
        xAxis.setRange(new Range(Math.floor(minPositionValues[0]), Math.ceil(maxPositionValues[0])));
        yAxis.setRange(new Range(Math.floor(minPositionValues[1]), Math.ceil(maxPositionValues[1])));
        zAxis.setRange(new Range(Math.floor(minPositionValues[2]), Math.ceil(maxPositionValues[2])));

        xyzPlot = new XYZPlot(myData, new LineXYZRenderer(), xAxis, yAxis, zAxis);

        myChart = new Chart3D("Position Test Data", "", xyzPlot);

        myChartPanel = new Chart3DPanel(myChart);


        xAxis.configureAsXAxis(xyzPlot);
        yAxis.configureAsYAxis(xyzPlot);
        zAxis.configureAsZAxis(xyzPlot);
    }

    public static void setUpFrame(){

        myData.add(mySeries);
        JFrame frame = new JFrame();
        frame.setSize(600,600);
        frame.add(myChartPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println(xAxis.getRange());
        System.out.println(yAxis.getRange());
        System.out.println(zAxis.getRange());
    }

    public static String [] removeLabels(String item){
        item = item.replace("Acceleration:", "");
        item = item.replace("X:", "");
        item = item.replace("Y:", "");
        item = item.replace("Z:", "");
        item = item.replace("m/s^2", "");
        item = item.replace(" ", "");
        return item.split(",");

    }



    public static void readTimeAccelerationXYZData(String fileName) {
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

    public static double newPosition(double currentPosition, double currentVelocity, double timeDifference,
                              double currentAcceleration){
        currentPosition = currentPosition + (currentVelocity * timeDifference) +
                ((0.5) * (currentAcceleration * Math.pow(timeDifference, 2)));
        return currentPosition;
    }

    public static double newVelocity(double startingVelocity, double currentAcceleration, double timeDifference){
        return (startingVelocity + currentAcceleration * timeDifference);
    }

    public static void addPositionToSeries() {
        double [] currentPosition = {0,0,0};
        double [] currentVelocity = {0,0,0};
        double [] currentAcceleration;
        double currentTime;
        double startingTime = 0.0;
        double timeDifference;
        mySeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);

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

            mySeries.add(currentPosition[0], currentPosition[1], currentPosition[2]);
        }


    }

    public static void addPositionToSeries(ArrayList<Vector> positionVectors){
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

            mySeries.add(positionVectors.get(i).getI(), positionVectors.get(i).getJ(), positionVectors.get(i).getK());
        }
    }
}
