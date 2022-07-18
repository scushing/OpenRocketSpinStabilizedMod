package spinMod;

public class OrientationVector implements Vector {

    double i;
    double j;
    double k;

    double magnitude;

    public OrientationVector(){
        this.i = 0;
        this.j = 0;
        this.k = 0;
        calculateMagnitude();
    }


    public OrientationVector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        calculateMagnitude();
        becomeUnitVector();
    }


    private void becomeUnitVector() {
        this.i = i/magnitude;
        this.j = j/magnitude;
        this.k = k/magnitude;
    }


    public double getXAxisAngle() {
        return Math.atan(j/k);
    }


    public double getYAxisAngle() {
        return Math.atan(i/k);
    }


    public double getZAxisAngle() {
        return Math.atan(j/i);
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
        becomeUnitVector();
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


    private void calculateMagnitude() {
        this.magnitude = Math.sqrt(Math.pow(i,2) + Math.pow(j,2) + Math.pow(k,2));
    }
}
