package spinMod;

import spinMod.Vectors.Vector;


public class Gust {

    Vector wind;
    double startAlt;
    double endAlt;

    public Gust(Vector wind, double startAlt, double endAlt) {
        this.wind = wind;
        this.startAlt = startAlt;
        this.endAlt = endAlt;
    }


    public Vector getWind() {
        return wind;
    }


    public double getStartAlt() {
        return startAlt;
    }


    public double getEndAlt() {
        return endAlt;
    }

}
