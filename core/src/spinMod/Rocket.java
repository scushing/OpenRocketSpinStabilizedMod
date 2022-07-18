package spinMod;

import net.sf.openrocket.util.ArrayList;

public class Rocket {

    AngularMomentum xSpin;
    AngularMomentum ySpin;
    AngularMomentum zSpin;
    PositionVector position;
    OrientationVector orientation;
    VelocityVector velocity;
    TorqueVector spin;
    TorqueVector drag;
    TorqueVector wind;
    TorqueCalculator NetTorqueCalculator;

    double airDensity;
    double dragCoefficient;
    double sideArea;
    double topArea;

    double mass;
    double cgArm;
    double dragCPArm;
    double windCPArm;
    double radius;
    double baseSpin;

    double maxTime;

    public Rocket(double mass, double cgArm, double radius, double baseSpin, double maxTime) {
        this.position = new PositionVector();
        this.velocity = new VelocityVector();
        this.mass = mass;
        this.cgArm = cgArm;
        this.radius = radius;
        this.baseSpin = baseSpin;
        this.maxTime = maxTime;

        xSpin = new AngularMomentum(0, mass, radius, cgArm);
        ySpin = new AngularMomentum(0, mass, radius, cgArm);
        zSpin = new AngularMomentum(baseSpin, mass, radius);

        orientation = new OrientationVector();
        spin = new TorqueVector();
        drag = new TorqueVector();
        wind = new TorqueVector();
        NetTorqueCalculator = new TorqueCalculator(spin, drag, wind);
    }


    public void update(VelocityVector windVelocity) {
        this.spin.setMagnitudeS(xSpin, ySpin, zSpin);
        this.wind.setMagnitudeD();
    }

}
