package spinMod;

public class PositionVector implements Vector{

    double i;
    double j;
    double k;

    public PositionVector() {
        i = 0;
        j = 0;
        k = 0;
    }


    @Override
    public void setI(double i) {
        this.i = i;
    }

    @Override
    public void setJ(double j) {
        this.j = j;
    }

    @Override
    public void setK(double k) {
        this.k = k;
    }

    @Override
    public void setAll(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
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
        return Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2) + Math.pow(k, 2));
    }
}
