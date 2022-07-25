package spinMod;

import spinMod.Vectors.OrientationVector;
import spinMod.Vectors.TorqueVector;
import spinMod.Vectors.Vector;

public class Rocket {

    AngularMomentum xSpin;
    AngularMomentum ySpin;
    AngularMomentum zSpin;
    Vector position;
    OrientationVector orientation;
    Vector velocity;

    TorqueVector spin;
    TorqueVector drag;
    TorqueVector wind;
    TorqueVector gravity;

    TorqueCalculator netTorqueCalculator;

    double airDensity;
    double topDragCoefficient;
    double sideDragCoefficient;
    double sideArea;
    double topArea;

    double mass;
    double cgArm;
    double dragCPArm;
    double windCPArm;
    double radius;

    public Rocket(double mass, double cgArm, double radius, double baseSpin, double airDensity, double topDragCoefficient,
                  double sideDragCoefficient, double sideArea, double topArea, double dragCPArm, double windCPArm) {

        this.position = new Vector();
        this.velocity = new Vector();

        this.mass = mass;
        this.cgArm = cgArm;
        this.radius = radius;
        this.topDragCoefficient = topDragCoefficient;
        this.sideDragCoefficient = sideDragCoefficient;
        this.sideArea = sideArea;
        this.topArea = topArea;
        this.dragCPArm = dragCPArm;
        this.windCPArm = windCPArm;
        this.airDensity = airDensity;

        xSpin = new AngularMomentum(0, mass, radius, cgArm);
        ySpin = new AngularMomentum(0, mass, radius, cgArm);
        zSpin = new AngularMomentum(baseSpin, mass, radius);

        orientation = new OrientationVector(0, 0, 1);
        spin = new TorqueVector();
        drag = new TorqueVector();
        wind = new TorqueVector();
        gravity = new TorqueVector();
        netTorqueCalculator = new TorqueCalculator(spin, drag, wind, gravity);
    }


    public void update(Vector extWind, double stepTime, double thrust) {
        //Linear Force vectors
        Vector Grav = new Vector(0, 0, -ForceCalculator.calcG(mass));
        Vector Wind = new Vector(extWind.getI(), extWind.getJ(), 0);
        Vector Drag = new Vector(-velocity.getI(), -velocity.getJ(), -velocity.getK());
        Vector Thrust = new Vector(orientation.getI(), orientation.getJ(), orientation.getK());

        Wind.setMagnitude(ForceCalculator.calcD(airDensity, extWind.getMagnitude(), sideDragCoefficient, sideArea));
        Drag.setMagnitude(ForceCalculator.calcD(airDensity, velocity.getMagnitude(), topDragCoefficient, topArea));
        Thrust.setMagnitude(thrust);

        Vector NetForce = ForceCalculator.getNet(Grav, Thrust, Drag, Wind);

        //Torque vectors
        Vector cgArm = new Vector(orientation.getI(), orientation.getJ(), orientation.getK());
        cgArm.setMagnitude(this.cgArm);
        Vector dragCPArm = new Vector(orientation.getI(), orientation.getJ(), orientation.getK());
        dragCPArm.setMagnitude(this.cgArm-this.dragCPArm);
        Vector windCPArm = new Vector(orientation.getI(), orientation.getJ(), orientation.getK());
        windCPArm.setMagnitude(this.cgArm - this.windCPArm);
        gravity.crossProduct(Grav, cgArm);
        spin.setMagnitudeS(xSpin, ySpin, zSpin);
        wind.crossProduct(Wind, windCPArm);
        drag.crossProduct(Drag, dragCPArm);
        netTorqueCalculator.findNet();

        //Calculating angular momentum
        xSpin.updateMagnitude(netTorqueCalculator.getNet().getI(), stepTime);
        ySpin.updateMagnitude(netTorqueCalculator.getNet().getJ(), stepTime);
        zSpin.updateMagnitude(netTorqueCalculator.getNet().getK(), stepTime);

        //Updating orientation vector
        double xRot = xSpin.getAngularVelocity() * stepTime;
        double yRot = ySpin.getAngularVelocity() * stepTime;
        double zRot = zSpin.getAngularVelocity() * stepTime;
        orientation.update(xRot, yRot, zRot);

        updateVelocity(NetForce, stepTime);
        updatePosition(stepTime);

        System.out.println();
        System.out.println("New Iteration");
        System.out.println(Grav);
        System.out.println(Wind);
        System.out.println(Drag);
        System.out.println(Thrust);
        System.out.println(NetForce);
        System.out.println();
        System.out.println(gravity);
        System.out.println(spin);
        System.out.println(wind);
        System.out.println(drag);
        System.out.println(netTorqueCalculator.getNet());
        System.out.println();
        System.out.println(xSpin.getMagnitude());
        System.out.println(xSpin.getAngularVelocity());
        System.out.println(ySpin.getMagnitude());
        System.out.println(ySpin.getAngularVelocity());
        System.out.println(zSpin.getMagnitude());
        System.out.println(zSpin.getAngularVelocity());
        System.out.println();
        System.out.println(velocity);
        System.out.println(orientation);
        System.out.println();
    }


    public Vector getPosition() {
        return position;
    }


    public Vector getVelocity() {
        return velocity;
    }


    private void updateVelocity(Vector netForce, double stepTime) {
        velocity.setI(velocity.getI() + ((netForce.getI() / mass) * stepTime));
        velocity.setJ(velocity.getJ() + ((netForce.getJ() / mass) * stepTime));
        velocity.setK(velocity.getK() + ((netForce.getK() / mass) * stepTime));
    }


    private void updatePosition(double stepTime) {
        position.setAll(position.getI() + (velocity.getI() * stepTime), position.getJ() + (velocity.getJ() * stepTime),
                position.getK() + (velocity.getK() * stepTime));
    }

}
