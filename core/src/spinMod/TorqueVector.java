package spinMod;

public class TorqueVector {
    double i;
    double j;
    double k;
    double magnitude;

    public TorqueVector() {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        findMagnitude();
    }


    public TorqueVector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        findMagnitude();
    }


    private void findMagnitude() {
        this.magnitude = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2) + Math.pow(k, 2));
    }


    public void setMagnitudeD(double airDensity, double linearVelocity, double dragCoefficient, double area, double arm) {
        double dragForce = 0.5 * airDensity * Math.pow(linearVelocity, 2) * dragCoefficient * area;
        magnitude = dragForce * arm;
    }


    public void setMagnitudeS() {
        //TODO: FILL WITH FORMULA
    }

}
