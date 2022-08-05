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
        StabilitySim sim = new StabilitySim(0.5, 0.13, 0.023, 0, 1.33, 0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 1, 0.001);
        Queue<Gust> gusts = new LinkedList<>();
        gusts.add(new Gust(new Vector(5, 2, 0), 0, 50));
        gusts.add(new Gust(new Vector(-3, 7, 0), 100, 150));
        gusts.add(new Gust(new Vector(6, -10, 0), 200, 220));
        ArrayList<Vector> data = stabilitySim(gusts);

        RocketGraphing.addPositionToSeries(data);
        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();
    }

}
