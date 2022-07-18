package spinMod;

import net.sf.openrocket.util.ArrayList;

public class StabilitySim {

    static double mass;
    static double cgArm;
    static double radius;
    static double baseSpin;
    static double maxTime;
    static int stepMax;

    public StabilitySim(double mass, double cgArm, double radius, double baseSpin, double maxTime, int stepMax) {
        this.mass = mass;
        this.cgArm = cgArm;
        this.radius = radius;
        this.baseSpin = baseSpin;
        this.maxTime = maxTime;
        this.stepMax = stepMax;
    }


    public static ArrayList<VelocityVector> stabilitySim() {
        ArrayList<VelocityVector> velocityVectors = new ArrayList<>(stepMax);
        Rocket rocket = new Rocket(mass, cgArm, radius, baseSpin, maxTime);

        double inc = maxTime/stepMax;
        double time = 0;

        while (time < maxTime) {
            rocket.update();
            time += inc;
        }

        return velocityVectors;
    }


    private ArrayList<Gust> windVelocities(int[] startAlt, int[] endAlt, VelocityVector[] v) {
        ArrayList<Gust> list = new ArrayList<>(stepMax);
        for (int i = 0; i < stepMax; i++) {
            list.add(new Gust(v[i], startAlt[i], endAlt[i]));
        }
        return list;
    }


    private static double rpmToRad(double rpm) {
        return (rpm/60) * 2 * Math.PI;
    }
}
