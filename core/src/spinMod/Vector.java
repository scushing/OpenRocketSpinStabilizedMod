package spinMod;

public interface Vector {

    void setI(double i);

    void setJ(double j);

    void setK(double k);

    void setAll(double i, double j, double k);

    double getI();
    double getJ();
    double getK();

    double getMagnitude();

}
