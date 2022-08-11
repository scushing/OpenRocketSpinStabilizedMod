package spinMod;

import net.sf.openrocket.util.ArrayList;
import spinMod.Vectors.OrientationVector;
import spinMod.Vectors.Vector;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class StabilitySim {

    static double mass;
    static double cgArm;
    static double radius;
    static double baseSpin;
    static double airDensity;
    static double topDragCoefficient;
    static double sideDragCoefficient;
    static double sideArea;
    static double topArea;
    static double dragCPArm;
    static double windCPArm;

    static double thrust;
    static double burnTime;

    static double incrementSize;


//    public static void main(String args[]) {
//        StabilitySim sim = new StabilitySim(1, 0.13, 0.023, 0, 1.33, 0.2,
//                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 1, 0.001);
//        Queue<Gust> gusts = new LinkedList<>();
//        gusts.add(new Gust(new Vector(1, 1, 0), 100, 200));
//        java.util.ArrayList<Vector> data = stabilitySim(gusts);
//    }


    public StabilitySim(double mass, double cgArm, double radius, double baseSpin, double airDensity, double topDragCoefficient,
                        double sideDragCoefficient, double sideArea, double topArea, double dragCPArm, double windCPArm,
                        double thrust, double burnTime, double incrementSize) {
        this.mass = mass;
        this.cgArm = cgArm;
        this.radius = radius;
        this.topDragCoefficient = topDragCoefficient;
        this.sideDragCoefficient = sideDragCoefficient;
        this.sideArea = sideArea;
        this.topArea = topArea;
        this.dragCPArm = dragCPArm;
        this.windCPArm = windCPArm;
        this.thrust = thrust;
        this.burnTime = burnTime;

        this.baseSpin = baseSpin;
        this.airDensity = airDensity;

        this.incrementSize = incrementSize;
    }


    public ArrayList<Vector> stabilitySim(Queue<Gust> risingWinds) {
        ArrayList<Vector> vectors = new ArrayList<>();
        Rocket rocket = new Rocket(mass, cgArm, radius, rpmToRad(baseSpin), airDensity, topDragCoefficient, sideDragCoefficient, sideArea,
                topArea, dragCPArm, windCPArm);

        double time = 0;
        int step = 0;
        Gust currentGust = risingWinds.peek();
        Stack<Gust> fallingWinds = new Stack<>();
        int countMax = risingWinds.size();

        double currentAlt = 0;
        while (currentAlt >= 0) {
            currentGust = updateCurrent(risingWinds, fallingWinds, currentGust, currentAlt, countMax, 0);
            Vector wind = currentGust.getWind();
            if (time > burnTime) {
                thrust = 0;
            }
            rocket.update(wind, incrementSize, thrust);
            time += incrementSize;
            vectors.add(step ,(new Vector(rocket.getPosition().getI(), rocket.getPosition().getJ(),rocket.getPosition().getK())));
            step++;
            currentAlt = rocket.getPosition().getK();
        }

        return vectors;
    }


    private Gust updateCurrent(Queue<Gust> risingWinds, Stack<Gust> fallingWinds, Gust currentGust, double currentAlt, int countMax, int counter) {
        if (counter >= countMax) {
            return new Gust(new Vector(0.00001, 0, 0), 0, 0);
        }
        if (currentGust.getStartAlt() <= currentAlt && currentGust.getEndAlt() >= currentAlt) {
            return currentGust;
        } else if (currentGust.getEndAlt() < currentAlt && risingWinds.iterator().hasNext()) {
            counter++;
            currentGust = risingWinds.peek();
            fallingWinds.push(risingWinds.poll());
            return updateCurrent(risingWinds, fallingWinds, currentGust, currentAlt, countMax, counter);
        } else if (currentGust.getStartAlt() > currentAlt && fallingWinds.iterator().hasNext()) {
            counter++;
            currentGust = fallingWinds.peek();
            risingWinds.offer(fallingWinds.pop());
            return updateCurrent(risingWinds, fallingWinds, currentGust, currentAlt, countMax, counter);
        } else {
            return new Gust(new Vector(0.00001, 0, 0), 0, 0);
        }
    }


    private double rpmToRad(double rpm) {
        return (rpm/60) * 2 * Math.PI;
    }
}
