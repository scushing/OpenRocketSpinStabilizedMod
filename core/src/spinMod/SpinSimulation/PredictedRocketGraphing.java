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
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.openrocket.unit.UnitGroup;

import static spinMod.StabilitySim.stabilitySim;
import spinMod.SpinSimulation.RocketGraphing.*;
public class PredictedRocketGraphing {

    private double mass = 0;
    private double cgArm = 0;
    private double radius = 0;
    private double baseSpin = 0;
    private double airDensity = 0;
    private double topDragCoefficient = 0;
    private double sideDragCoefficient = 0;
    private double sideArea = 0;
    private double topArea = 0;
    private double dragCPArm = 0;
    private double windCPArm = 0;
    private double thrust = 0;
    private double burnTime = 0;
    private double incrementSize = 0;





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

    public void setMass(double value) {
        this.mass = value;
    }

    public void setCgArm(double value) {
        cgArm = value;
    }

    public void setRadius(double value) {
        radius = value;
    }

    public void setBaseSpin(double value){
        baseSpin = value;
    }

    public void setAirDensity(double value){
        airDensity = value;
    }

    public void setTopDragCoefficient(double value){
        topDragCoefficient = value;
    }

    public void setSideDragCoefficient(double value){
        sideDragCoefficient = value;
    }

    public void setSideArea(double value){
        sideArea = value;
    }

    public void setTopArea(double value){
        topArea = value;
    }

    public void setDragCPArm(double value){
        dragCPArm = value;
    }

    public void setWindCPArm(double value){
        windCPArm = value;
    }

    public void setThrust(double value){
        thrust = value;
    }

    public void setBurnTime(double value){
        burnTime = value;
    }

    public void setIncrementSize(double value){
        incrementSize = value;
    }

    private void retrieveMass(FlightConfiguration flightConfiguration){
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        mass = cg.weight;
    }


    private void retrieveCgArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        double cgx = cg.x;
        cgArm = rocketLength - cgx;
    }


    private void retrieveRadius(FlightConfiguration flightConfiguration){
        for (RocketComponent c : flightConfiguration.getCoreComponents()) {
            if (c instanceof SymmetricComponent) {
                double d1 = ((SymmetricComponent) c).getForeRadius() * 2;
                double d2 = ((SymmetricComponent) c).getAftRadius() * 2;
                radius = MathUtil.max(radius, d1, d2) / 2;
            }
        }
    }

    private void retrieveTopDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        topDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCD();
    }

    private void retrieveSideDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        sideDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCDaxial();
    }

    private void retrieveDragCPArm(FlightConfiguration flightConfiguration){
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

    private void retrieveSideArea(FlightConfiguration flightConfiguration){

    }

    private void retrieveTopArea(FlightConfiguration flightConfiguration){
        ArrayList<RocketComponent> rocketComponents = flightConfiguration.getCoreComponents();
        for(RocketComponent component : rocketComponents){
            if (component instanceof SymmetricComponent) {
                double d1 = ((SymmetricComponent) component).getForeRadius() * 2;
                double d2 = ((SymmetricComponent) component).getAftRadius() * 2;
                radius = MathUtil.max(radius, d1, d2) / 2;
            }
        }
    }

    private void retrieveDragCPArm(){

    }

    private void retrieveWindCPArm(){

    }

    private void retrieveThrust(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        thrust = motorConfiguration.getMotor().getAverageThrustEstimate();
    }

    private void retrieveBurnTime(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        burnTime = motorConfiguration.getMotor().getBurnTimeEstimate();
    }

    private void retrieveData(Simulation sim) {

        FlightConfiguration curConfig = sim.getActiveConfiguration();

        // Use for application
        retrieveMass(curConfig);
        retrieveCgArm(curConfig);
        retrieveRadius(curConfig);
        setBaseSpin(0);
        setAirDensity(1.33);
        retrieveTopDragCoefficient(curConfig);
        retrieveSideDragCoefficient(curConfig);
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


        // Use for manual testing
        /*
        setMass(3.35);
        setCgArm(1.61);
        setRadius(0.25);
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
        */

    }

    public void runSpinSimulation(Simulation sim) {

        retrieveData(sim);

        StabilitySim stabilitySim = new StabilitySim(this.mass, cgArm, radius, baseSpin, airDensity, topDragCoefficient,
                sideDragCoefficient, sideArea, topArea, dragCPArm, windCPArm, thrust, burnTime, incrementSize);

        //Use for manual testing
        /*
        StabilitySim stabilitySim = new StabilitySim(0.5, 0.13, 0.023, 200, 1.33, 0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 1, 0.001);
        */

        Queue<Gust> gusts = new LinkedList<>();
        gusts.add(new Gust(new Vector(40, 10, 0), 100, 200));
        ArrayList<Vector> data = stabilitySim(gusts);

        RocketGraphFrame rocketGraphFrame = new RocketGraphFrame(data);
    }

}
