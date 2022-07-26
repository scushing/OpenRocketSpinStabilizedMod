import javax.swing.*;
import spinMod.StabilitySim;
import spinMod.Vectors.Vector;

import java.util.ArrayList;

import static spinMod.StabilitySim.stabilitySim;

public class GraphingFrame {
    public static void main(String [] args){

        RocketGraphing.readTimeAccelerationXYZData("position-graphing/test-data/Circle.txt");
        RocketGraphing.addPositionToSeries();

        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();

    }
}
