package spinMod.SpinSimulation;
import spinMod.Rocket;
import spinMod.SpinSimulation.RocketGraphing.*;
import spinMod.Vectors.Vector;

import javax.swing.*;

import java.util.ArrayList;

import static spinMod.SpinSimulation.RocketGraphing.*;

public class RocketGraphFrame extends JFrame {

    public RocketGraphFrame(ArrayList<Vector> data){

        RocketGraphing rocketGraphing = new RocketGraphing();
        rocketGraphing.updatePositionSeries(data);
        add(rocketGraphing.getChart3DPanel());
        setSize(600,600);
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }
}
