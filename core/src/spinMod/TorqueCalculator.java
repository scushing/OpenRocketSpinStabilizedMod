package spinMod;

public class TorqueCalculator {

    TorqueVector spin;
    TorqueVector drag;
    TorqueVector wind;
    TorqueVector net;

    public TorqueCalculator(TorqueVector spin, TorqueVector drag, TorqueVector wind) {
        this.spin = spin;
        this.drag = drag;
        this.wind = wind;
        this.net = new TorqueVector();
        findNet();
    }


    public void findNet() {
        net.i = spin.i + drag.i + wind.i;
        net.j = spin.j + drag.j + wind.j;
        net.k = spin.k + drag.k + wind.j;
    }

}
