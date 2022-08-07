package spinMod.SpinSimulation;

import net.sf.openrocket.aerodynamics.AerodynamicCalculator;
import net.sf.openrocket.aerodynamics.BarrowmanCalculator;
import net.sf.openrocket.aerodynamics.FlightConditions;
import net.sf.openrocket.aerodynamics.WarningSet;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.masscalc.MassCalculation;
import net.sf.openrocket.motor.MotorConfiguration;
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
import spinMod.SpinSimulation.RocketGraphing.*;
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





    public double getMass() {
        return mass;
    }

    public double getCgArm() {
        return cgArm;
    }

    public double getRadius() {
        return radius;
    }

    public double getBaseSpin() {
        return baseSpin;
    }

    public double getAirDensity() {
        return airDensity;
    }

    public double getTopDragCoefficient() {
        return topDragCoefficient;
    }

    public double getSideDragCoefficient() {
        return sideDragCoefficient;
    }

    public double getSideArea() {
        return sideArea;
    }

    public double getTopArea() {
        return topArea;
    }

    public double getDragCPArm() {
        return dragCPArm;
    }

    public double getWindCPArm() {
        return windCPArm;
    }

    public double getThrust() {
        return thrust;
    }

    public double getBurnTime() {
        return burnTime;
    }

    public double getIncrementSize() {
        return incrementSize;
    }

    public static void setMass(double value) {
        mass = value;
    }

    public static void setCgArm(double value) {
        cgArm = value;
    }

    public static void setRadius(double value) {
        radius = value;
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

    private static void retrieveMass(FlightConfiguration flightConfiguration){
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        mass = cg.weight;
    }


    private static void retrieveCgArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        double cgx = cg.x;
        cgArm = rocketLength - cgx;
    }


    private static void retrieveRadius(FlightConfiguration flightConfiguration){
        for (RocketComponent c : flightConfiguration.getCoreComponents()) {
            if (c instanceof SymmetricComponent) {
                double d1 = ((SymmetricComponent) c).getForeRadius() * 2;
                double d2 = ((SymmetricComponent) c).getAftRadius() * 2;
                radius = MathUtil.max(radius, d1, d2) / 2;
            }
        }
    }

    private static void retrieveTopDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        topDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCD();
    }

    private static void retrieveSideDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        sideDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCDaxial();
    }

    private static void retrieveDragCPArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        //aerodynamicCalculator.getAerodynamicForces(curConfig, conditions, warnings);

        Coordinate cp = aerodynamicCalculator.getWorstCP(flightConfiguration, conditions, warnings);
        double cpx = cp.x;
        dragCPArm = rocketLength - cpx;
        System.out.println();
    }
    private static void retrieveThrust(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        thrust = motorConfiguration.getMotor().getAverageThrustEstimate();
    }

    private static void retrieveBurnTime(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        burnTime = motorConfiguration.getMotor().getBurnTimeEstimate();
    }
    private static void retrieveData(Simulation sim) {

        FlightConfiguration curConfig = sim.getActiveConfiguration();


        retrieveMass(curConfig);
        retrieveCgArm(curConfig);
        retrieveRadius(curConfig);
        setBaseSpin(0);
        setAirDensity(1.33);
        //retrieveTopDragCoefficient(curConfig);
        //retrieveSideDragCoefficient(curConfig);
        setTopDragCoefficient(0.2);
        setSideDragCoefficient(0.3);
        setSideArea(0.0016); // Cross Sectional Area
        setTopArea(0.000075); // Biggest Diameter + Fin top area * fin amount
        retrieveDragCPArm(curConfig);
        //setDragCPArm(0.07); // Bottom to Center of pressure
        setWindCPArm(0.08); // Cross Sectional Area - Integrate & Find midpoint
        retrieveThrust(curConfig);
        retrieveBurnTime(curConfig);
        setIncrementSize(0.001);



        //Code for getting cp
        /*
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(curConfig);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        //aerodynamicCalculator.getAerodynamicForces(curConfig, conditions, warnings);

        Coordinate cp = aerodynamicCalculator.getWorstCP(curConfig, conditions, warnings);
        double cpx = cp.y;
        */

    }

    public void runSpinSimulation(Simulation sim) {

        retrieveData(sim);

        StabilitySim stabilitySim = new StabilitySim(mass, cgArm, radius, baseSpin, airDensity, topDragCoefficient,
                sideDragCoefficient, sideArea, topArea, dragCPArm, windCPArm, thrust, burnTime, incrementSize);
        Queue<Gust> gusts = new LinkedList<>();
        //gusts.add(new Gust(new Vector(20, 10, 0), 100, 200));
        ArrayList<Vector> data = stabilitySim(gusts);

        RocketGraphFrame rocketGraphFrame = new RocketGraphFrame(data);
    }

}
