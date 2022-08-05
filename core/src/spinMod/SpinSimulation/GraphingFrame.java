package spinMod.SpinSimulation;

public class GraphingFrame {
    public static void main(String [] args){

        RocketGraphing.readTimeAccelerationXYZData("position-graphing/test-data/Circle.txt");
        RocketGraphing.addPositionToSeries();

        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();

    }
}
