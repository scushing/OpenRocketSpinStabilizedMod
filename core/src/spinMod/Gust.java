package spinMod;

public class Gust {

    VelocityVector wind;
    double startTime;
    double endTime;

    public Gust(VelocityVector wind, double startTime, double endTime) {
        this.wind = wind;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
