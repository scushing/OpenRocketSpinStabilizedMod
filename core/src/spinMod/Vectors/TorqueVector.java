package spinMod.Vectors;

import spinMod.AngularMomentum;
import spinMod.ForceCalculator;
import spinMod.Vectors.Vector;

public class TorqueVector extends Vector {

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


    public void setMagnitudeS(AngularMomentum x, AngularMomentum y, AngularMomentum z) {
        setI((x.getModifier() + ((y.getAngularVelocity()*z.getMagnitude()) - (z.getAngularVelocity()*y.getMagnitude()))));
        setJ((y.getModifier() + ((z.getAngularVelocity()*x.getMagnitude()) - (x.getAngularVelocity()*z.getMagnitude()))));
        setK((z.getModifier() + ((x.getAngularVelocity()*y.getMagnitude()) - (y.getAngularVelocity()*x.getMagnitude()))));
        calculateMagnitude();
    }


    public void becomeUnitVector() {
        this.i = i/magnitude;
        this.j = j/magnitude;
        this.k = k/magnitude;
    }


    private void updateIJK(){
        i = i * magnitude;
        j = j * magnitude;
        k = k * magnitude;
    }

}
