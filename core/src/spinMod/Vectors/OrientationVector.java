package spinMod.Vectors;

public class OrientationVector extends Vector {

    double xAngle;
    double yAngle;
    double zAngle;


    public OrientationVector(){
        new OrientationVector(0, 0, 0);
    }


    public OrientationVector(double i, double j, double k) {
        super(i, j, k);
        xAngle = 0;
        yAngle = 0;
        zAngle = 0;
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

        double mag = Math.sqrt(Math.pow(Math.sin(xAngle), 2) + Math.pow(Math.cos(xAngle) * Math.sin(yAngle), 2));
        double theta = Math.atan(Math.sin(xAngle) / Math.sin(yAngle));

        i = Math.sin(theta) * mag;
        j = Math.cos(theta) * mag;
        double temp = Math.abs(1 - Math.pow(mag, 2));
        if (temp != 0) {
            k = Math.sqrt(temp) * ((1 - Math.pow(mag, 2) / temp));
        } else {
            k = 0;
        }
        becomeUnitVector();

        return getMagnitude() == 1;
    }

}
