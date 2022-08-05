import javax.swing.*;
import spinMod.StabilitySim;
import spinMod.Vectors.Vector;

import java.util.ArrayList;

import static spinMod.StabilitySim.stabilitySim;

public class PredictedRocketGraphing {
    public static void main(String [] args) {
        StabilitySim sim = new StabilitySim(0.5, 0.13, 0.023, 200, 1.33, 0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 8, 1000);
        int[] startAlt = new int[1];
        startAlt[0] = 100;
        int[] endAlt = new int[1];
        endAlt[0] = 200;
        Vector[] v = new Vector[1];
        v[0] = new Vector(1, 0, 0);
        ArrayList<Vector> data = stabilitySim(startAlt, endAlt, v);

        RocketGraphing.addPositionToSeries(data);
        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();
    }

}
