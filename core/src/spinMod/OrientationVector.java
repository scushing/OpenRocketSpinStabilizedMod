package spinMod;

public class OrientationVector {

    double xPos;
    double yPos;
    double zPos;

    public void setXPos(double x) {
        xPos = x;
    }


    public void setYPos(double y) {
        yPos = y;
    }


    public void setzPos(double z) {
        zPos = z;
    }


    public void setPos(double x, double y, double z) {
        xPos = x;
        yPos = y;
        zPos = z;
    }


    public double getxPos() {
        return xPos;
    }


    public double getyPos() {
        return yPos;
    }


    public double getzPos() {
        return zPos;
    }
}
