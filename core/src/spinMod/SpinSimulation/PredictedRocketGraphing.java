package spinMod.SpinSimulation;

import net.sf.openrocket.aerodynamics.AerodynamicCalculator;
import net.sf.openrocket.aerodynamics.BarrowmanCalculator;
import net.sf.openrocket.aerodynamics.FlightConditions;
import net.sf.openrocket.aerodynamics.WarningSet;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.masscalc.MassCalculation;
import net.sf.openrocket.rocketcomponent.FlightConfiguration;
import net.sf.openrocket.rocketcomponent.Rocket;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.rocketcomponent.SymmetricComponent;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.MathUtil;
import spinMod.Gust;
import spinMod.StabilitySim;
import spinMod.Vectors.Vector;
import net.sf.openrocket.masscalc.MassCalculator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.openrocket.unit.UnitGroup;

import static spinMod.StabilitySim.stabilitySim;

public class PredictedRocketGraphing {

    static double mass = 0;
    static double cgArm = 0;
    static double radius = 0;
    static double baseSpin = 0;
    static double airDensity = 0;
    static double topDragCoefficient = 0;
    static double sideDragCoefficient = 0;
    static double sideArea = 0;
    static double topArea = 0;
    static double dragCPArm = 0;
    static double windCPArm = 0;
    static double thrust = 0;
    static double burnTime = 0;
    static double incrementSize = 0;

    private static void setMass(FlightConfiguration flightConfiguration){
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        mass = cg.weight;
    }


    private static void setCgArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        double cgx = cg.x;
        cgArm = rocketLength - cgx;
    }


    private static void setRadius(FlightConfiguration flightConfiguration){
        for (RocketComponent c : flightConfiguration.getCoreComponents()) {
            if (c instanceof SymmetricComponent) {
                double d1 = ((SymmetricComponent) c).getForeRadius() * 2;
                double d2 = ((SymmetricComponent) c).getAftRadius() * 2;
                radius = MathUtil.max(radius, d1, d2) / 2;
            }
        }
    }
    private static void setBaseSpin(double value){
        baseSpin = value;
    }
    private static void setAirDensity(double value){
        airDensity = value;
    }
    private static void setTopDragCoefficient(double value){
        topDragCoefficient = value;
    }
    private static void setSideDragCoefficient(double value){
        sideDragCoefficient = value;
    }
    private static void setSideArea(double value){
        sideArea = value;
    }
    private static void setTopArea(double value){
        topArea = value;
    }
    private static void setDragCPArm(double value){
        dragCPArm = value;
    }
    private static void setWindCPArm(double value){
        windCPArm = value;
    }
    private static void setThrust(double value){
        thrust = value;
    }
    private static void setBurnTime(double value){
        burnTime = value;
    }
    private static void setIncrementSize(double value){
        incrementSize = value;
    }
    private static void retrieveData(Simulation sim) {

        FlightConfiguration curConfig = sim.getActiveConfiguration();


        setMass(curConfig);
        setCgArm(curConfig);
        setRadius(curConfig);
        setBaseSpin(0);
        setAirDensity(1.33);
        setTopDragCoefficient(0.2);
        setSideDragCoefficient(0.3);
        setSideArea(0.0016);
        setTopArea(0.000075);
        setDragCPArm(0.07);
        setWindCPArm(0.08);
        setThrust(44);
        setBurnTime(1);
        setIncrementSize(0.001);


        /*
        //Code for getting cp
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(curConfig);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        Coordinate cp = aerodynamicCalculator.getCP(curConfig, conditions, warnings);
        double cpx = cp.y;
        */
    }

    public static void runSpinSimulation(Simulation sim) {

        retrieveData(sim);

        StabilitySim stabilitySim = new StabilitySim(mass, cgArm, radius, baseSpin, airDensity, topDragCoefficient,
                sideDragCoefficient, sideArea, topArea, dragCPArm, windCPArm, thrust, burnTime, incrementSize);
        Queue<Gust> gusts = new LinkedList<>();
        gusts.add(new Gust(new Vector(20, 10, 0), 100, 200));
        ArrayList<Vector> data = stabilitySim(gusts);

        RocketGraphing.addPositionToSeries(data);
        RocketGraphing.setUpGraph();
        RocketGraphing.setUpFrame();
    }

}
