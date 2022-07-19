package spinMod;

import spinMod.Vectors.Vector;


public class Gust {

    Vector wind;
    double startTime;
    double endTime;

    public Gust(Vector wind, double startTime, double endTime) {
        this.wind = wind;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Vector getWind() {
        return wind;
    }
}
