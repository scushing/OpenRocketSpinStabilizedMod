package spinMod;

public class AngularMomentum {

    private double magnitude;
    private double inertialMoment;
    private double angularVelocity;

    private double modifier;

    public AngularMomentum(double angularVelocity, double mass, double radius) {
        this.angularVelocity = angularVelocity;
        setZInertialMoment(mass, radius);
        this.magnitude = this.angularVelocity * this.inertialMoment;
        modifier = 0;
    }


    public AngularMomentum(double angularVelocity, double mass, double radius, double arm) {
        this.angularVelocity = angularVelocity;
        setXYInertialMoment(mass, radius, arm);
        this.magnitude = this.angularVelocity * this.inertialMoment;
        modifier = 0;
    }


    public void setZInertialMoment(double mass, double radius) {
        inertialMoment = 0.5 * mass * radius * radius;
    }


    public void setXYInertialMoment(double mass, double radius, double arm) {
        inertialMoment = (1.0/4.0 * mass * radius * radius) + (1.0/12.0 * mass * arm * arm);
    }


    public void updateMagnitude(double torque, double time) {
        modifier = torque * time;
        magnitude += modifier;
        updateAngularVelocity();
    }


    private void updateAngularVelocity() {
        angularVelocity = magnitude/inertialMoment;
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

    public double getModifier() {
        return modifier;
    }


}
