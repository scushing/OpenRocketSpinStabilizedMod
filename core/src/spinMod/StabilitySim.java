package spinMod;

import net.sf.openrocket.util.ArrayList;

public class StabilitySim {

    double mass;
    double cgArm;
    double radius;
    double baseSpin;
    double maxTime;

    public StabilitySim(double mass, double cgArm, double radius, double baseSpin, double maxTime) {
        this.mass = mass;
        this.cgArm = cgArm;
        this.radius = radius;
        this.baseSpin = baseSpin;
        this.maxTime = maxTime;
    }


    public static ArrayList<VelocityVector> stabilitySim() {
        ArrayList<VelocityVector> velocityVectors = new ArrayList<>(1000);
        Rocket rocket = new Rocket(mass, cgArm, radius, baseSpin, maxTime);

        double inc = maxTime/1000;
        double time = 0;

        while (time < maxTime) {
            rocket.update();
            time += inc;
        }

        return velocityVectors;
    }


    private static double rpmToRad(double rpm) {
        return (rpm/60) * 2 * Math.PI;
    }
}
