package spinMod;

import net.sf.openrocket.util.ArrayList;
import spinMod.Vectors.Vector;

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

    static double maxTime;
    static int stepMax;


    public static void main(String args[]) {
        StabilitySim sim = new StabilitySim(0.5, 0.13, 0.023, 200, 1.33,0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 8, 1000);
        int[] startAlt = new int[1];
        startAlt[0] = 100;
        int[] endAlt = new int[1];
        endAlt[0] = 200;
        Vector[] v = new Vector[1];
        v[0] = new Vector(1, 0, 0);
        ArrayList<Vector> data = stabilitySim(startAlt, endAlt, v);
        //System.out.println(data);
    }


    public StabilitySim(double mass, double cgArm, double radius, double baseSpin, double airDensity, double topDragCoefficient,
                        double sideDragCoefficient, double sideArea, double topArea, double dragCPArm, double windCPArm,
                        double thrust, double maxTime, int stepMax) {
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

        this.baseSpin = baseSpin;
        this.airDensity = airDensity;
        this.maxTime = maxTime;
        this.stepMax = stepMax;
    }


    public static ArrayList<Vector> stabilitySim(int[] startAlt, int[] endAlt, Vector[] v) {
        ArrayList<Vector> vectors = new ArrayList<>(stepMax);
        Rocket rocket = new Rocket(mass, cgArm, radius, rpmToRad(baseSpin), airDensity, topDragCoefficient, sideDragCoefficient, sideArea,
                topArea, dragCPArm, windCPArm);

        double inc = maxTime/stepMax;
        double time = 0;

        ArrayList<Gust> gusts = windVelocities(startAlt, endAlt, v);
        int step = 0;
        int index = 0;

        while (time < maxTime) {
            Vector wind = new Vector();
            //TODO: FIND BETTER WAY OF ADDING GUSTS
//            if (index < gusts.size()) {
//                if (gusts.get(index).getStartAlt() < rocket.getPosition().getK() && gusts.get(index).getEndAlt() > rocket.getPosition().getK()) {
//                    wind = gusts.get(index).getWind();
//                }else if (gusts.get(index).getEndAlt() < rocket.getPosition().getK()) {
//                    wind.setAll(0, 0, 0);
//                    index++;
//                }
//            }
            if (time > 1) {
                thrust = 0;
            }
            wind.setAll(0.001, 0, 0);
            rocket.update(wind, inc, thrust);
            System.out.println(time);
            step++;
            time += inc;
            vectors.add(rocket.getVelocity());
        }
        System.out.println(rocket.getPosition());
        System.out.println(rocket.getVelocity());

        return vectors;
    }


    private static ArrayList<Gust> windVelocities(int[] startAlt, int[] endAlt, Vector[] v) {
        ArrayList<Gust> list = new ArrayList<Gust>(v.length);
        for (int i = 0; i < v.length; i++) {
            list.add(new Gust(v[i], startAlt[i], endAlt[i]));
        }
        return list;
    }


    private static double rpmToRad(double rpm) {
        return (rpm/60) * 2 * Math.PI;
    }
}
