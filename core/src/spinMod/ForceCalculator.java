package spinMod;

import spinMod.Vectors.Vector;

public class ForceCalculator {

    public static double calcG(double mass) {
        return mass * 9.8;
    }


    public static double calcD(double airDensity, double linearVelocity, double dragCoefficient, double area) {
        return 0.5 * airDensity * Math.pow(linearVelocity, 2) * dragCoefficient * area;
    }


    public static Vector getNet(Vector gravity, Vector thrust, Vector drag, Vector wind) {
        Vector net = new Vector();
        net.setI(thrust.getI() + drag.getI() + wind.getI() + gravity.getI());
        net.setJ(thrust.getJ() + drag.getJ() + wind.getJ() + gravity.getJ());
        net.setK(thrust.getK() + drag.getK() + wind.getK() + gravity.getK());
        return net;
    }

}
