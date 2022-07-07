package spinMod;

public class AngularMomentum {

    double magnitude;
    double inertialMoment;
    double angularVelocity;

    public AngularMomentum(double angularVelocity, double mass, double radius) {
        this.angularVelocity = angularVelocity;
        setZInertialMoment(mass, radius);
        this.magnitude = this.angularVelocity * this.inertialMoment;
    }


    public AngularMomentum(double angularVelocity, double mass, double radius, double arm) {
        this.angularVelocity = angularVelocity;
        setXYInertialMoment(mass, radius, arm);
        this.magnitude = this.angularVelocity * this.inertialMoment;
    }


    public void setZInertialMoment(double mass, double radius) {
        inertialMoment = 0.5 * mass * radius * radius;
    }


    public void setXYInertialMoment(double mass, double radius, double arm) {
        inertialMoment = (1.0/4.0 * mass * radius * radius) + (1.0/3.0 * mass * arm * arm);
    }


    public double getMagnitude() {
        return magnitude;
    }


    public double getInertialMoment() {
        return inertialMoment;
    }


    public double getAngularVelocity() {
        return angularVelocity;
    }


}
