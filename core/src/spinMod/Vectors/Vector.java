package spinMod.Vectors;

public class Vector implements VectorMethods {

    double i;
    double j;
    double k;
    double magnitude;

    public Vector() {
        i = 0;
        j = 0;
        k = 0;
        magnitude = getMagnitude();
    }


    public Vector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        magnitude = getMagnitude();
    }


    private void becomeUnitVector() {
        this.i = i/magnitude;
        this.j = j/magnitude;
        this.k = k/magnitude;
    }


    public void setMagnitude(double magnitude) {
        becomeUnitVector();
        i *= magnitude;
        j *= magnitude;
        k *= magnitude;
    }


    public void crossProduct(Vector a, Vector b) {
        setI((a.getJ() * b.getK()) - (a.getK() * b.getJ()));
        setJ((a.getK() * b.getI()) - (a.getI() * b.getK()));
        setK((a.getI() * b.getJ()) - (a.getJ() * b.getI()));
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
