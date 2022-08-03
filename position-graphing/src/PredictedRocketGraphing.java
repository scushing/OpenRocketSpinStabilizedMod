import javax.swing.*;

import spinMod.Gust;
import spinMod.StabilitySim;
import spinMod.Vectors.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static spinMod.StabilitySim.stabilitySim;

public class PredictedRocketGraphing {
    public static void main(String [] args) {
        StabilitySim sim = new StabilitySim(0.5, 0.13, 0.023, 200, 1.33, 0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 1, 0.001);
        Queue<Gust> gusts = new LinkedList<>();
        gusts.add(new Gust(new Vector(20, 10, 0), 100, 200));
        ArrayList<Vector> data = stabilitySim(gusts);

        RocketGraphing.addPositionToSeries(data);
        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();
    }

}
