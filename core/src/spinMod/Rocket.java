package spinMod;

public class Rocket {

    AngularMomentum xSpin;
    AngularMomentum ySpin;
    AngularMomentum zSpin;
    OrientationVector orientation;
    TorqueVector spin;
    TorqueVector drag;
    TorqueVector wind;
    TorqueCalculator NetTorqueCalculator;

    double mass;
    double cgArm;
    double dragCPArm;
    double windCPArm;
    double radius;
    double baseSpin;

    double maxTime;

    public Rocket(double mass, double cgArm, double radius, double baseSpin, double maxTime) {
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


    public void update() {

    }

}
