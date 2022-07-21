package spinMod.Vectors;

public class OrientationVector extends Vector {

    double i;
    double j;
    double k;

    double xAngle;
    double yAngle;
    double zAngle;

    double magnitude;

    public OrientationVector(){
        this.i = 0;
        this.j = 0;
        this.k = 0;
        xAngle = 0;
        yAngle = 0;
        zAngle = 0;
        magnitude = this.getMagnitude();
    }


    public OrientationVector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
        xAngle = 0;
        yAngle = 0;
        zAngle = 0;
        magnitude = this.getMagnitude();
        becomeUnitVector();
    }


    public double getXAxisAngle() {
        return xAngle;
    }


    public double getYAxisAngle() {
        return yAngle;
    }


    public double getZAxisAngle() {
        return zAngle;
    }


    public boolean update(double xRot, double yRot, double zRot) {
        xAngle = (xAngle + xRot) % (2 * Math.PI);
        yAngle = (yAngle + yRot) % (2 * Math.PI);
        zAngle = (zAngle + zRot) % (2 * Math.PI);

        double mag = Math.sqrt(Math.pow(Math.cos(xAngle), 2) + Math.pow(Math.cos(yAngle), 2));
        double theta = Math.atan(Math.cos(xAngle) / Math.cos(yAngle));
        i = mag * Math.cos(theta);
        j = mag * Math.sin(theta);

        k = Math.sqrt(1 - Math.pow(mag, 2));

        return getMagnitude() == 1;
    }

}
