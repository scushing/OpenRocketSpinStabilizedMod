package spinMod;

import spinMod.Vectors.TorqueVector;

public class TorqueCalculator {

    private TorqueVector spin;
    private TorqueVector drag;
    private TorqueVector wind;
    private TorqueVector gravity;
    private TorqueVector net;

    public TorqueCalculator(TorqueVector spin, TorqueVector drag, TorqueVector wind, TorqueVector gravity) {
        this.spin = spin;
        this.drag = drag;
        this.wind = wind;
        this.gravity = gravity;
        this.net = new TorqueVector();
        findNet();
    }


    public void findNet() {
        net.setI(spin.getI() + drag.getI() + wind.getI() + gravity.getI());
        net.setJ(spin.getJ() + drag.getJ() + wind.getJ() + gravity.getJ());
        net.setK(spin.getK() + drag.getK() + wind.getK() + gravity.getK());
    }


    public TorqueVector getNet() {
        return net;
    }

}
