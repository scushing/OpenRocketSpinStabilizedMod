package spinMod;

public class TorqueVector implements Vector {
    double i;
    double j;
    double k;
    double magnitude;

    public TorqueVector() {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        calculateMagnitude();
    }


    public TorqueVector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        calculateMagnitude();
    }


    private void calculateMagnitude() {
        this.magnitude = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2) + Math.pow(k, 2));
    }


    public void setMagnitudeD(double airDensity, double linearVelocity, double dragCoefficient, double area, double arm) {
        becomeUnitVector();
        double dragForce = 0.5 * airDensity * Math.pow(linearVelocity, 2) * dragCoefficient * area;
        magnitude = dragForce * arm;
        updateIJK();
    }


    public void setMagnitudeS(AngularMomentum x, AngularMomentum y, AngularMomentum z) {
        i = x.getMagnitude() + (y.getAngularVelocity()*z.getMagnitude()) - (z.getAngularVelocity()*y.getMagnitude());
        j = y.getMagnitude() + (z.getAngularVelocity()*x.getMagnitude()) - (x.getAngularVelocity()*z.getMagnitude());
        k = z.getMagnitude() + (x.getAngularVelocity()*y.getMagnitude()) - (y.getAngularVelocity()*x.getMagnitude());
        calculateMagnitude();
    }


    private void becomeUnitVector() {
        this.i = i/magnitude;
        this.j = j/magnitude;
        this.k = k/magnitude;
    }


    private void updateIJK(){
        i = i * magnitude;
        j = j * magnitude;
        k = k * magnitude;
    }


    @Override
    public void setI(double i) {
        this.i = i;
        calculateMagnitude();
    }

    @Override
    public void setJ(double j) {
        this.j = j;
        calculateMagnitude();
    }

    @Override
    public void setK(double k) {
        this.k = k;
        calculateMagnitude();
    }

    @Override
    public void setAll(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        calculateMagnitude();
    }

    @Override
    public double getI() {
        return i;
    }

    @Override
    public double getJ() {
        return j;
    }

    @Override
    public double getK() {
        return k;
    }

    @Override
    public double getMagnitude() {
        return magnitude;
    }

}
