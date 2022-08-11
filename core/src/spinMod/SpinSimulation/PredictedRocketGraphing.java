package spinMod.SpinSimulation;

import net.sf.openrocket.aerodynamics.AerodynamicCalculator;
import net.sf.openrocket.aerodynamics.BarrowmanCalculator;
import net.sf.openrocket.aerodynamics.FlightConditions;
import net.sf.openrocket.aerodynamics.WarningSet;
import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.masscalc.MassCalculator;
import net.sf.openrocket.motor.MotorConfiguration;
import net.sf.openrocket.rocketcomponent.*;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.MathUtil;
import spinMod.Gust;
import spinMod.StabilitySim;
import spinMod.StabilitySim.*;
import spinMod.Vectors.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PredictedRocketGraphing {

    StabilitySim stabilitySim;
    Queue<Gust> gusts = new LinkedList<>();

    private double mass;
    private double cgArm;
    private double radius;
    private double baseSpin;
    private double airDensity;
    private double topDragCoefficient;
    private double sideDragCoefficient;
    private double sideArea;
    private double topArea;
    private double dragCPArm;
    private double windCPArm;
    private double thrust;
    private double burnTime;
    private double incrementSize;

    public PredictedRocketGraphing(Simulation sim){
        this.baseSpin = 0;
        this.airDensity = 1.33;
        this.incrementSize = 0.001;

        retrieveData(sim);

        StabilitySim stabilitySim = new StabilitySim(this.mass, this.cgArm, this.radius, this.baseSpin,
                this.airDensity, this.topDragCoefficient, this.sideDragCoefficient, this.sideArea,
                this.topArea, this.dragCPArm, this.windCPArm, this.thrust, this.burnTime, this.incrementSize);

        //Use for manual testing
        /*
        StabilitySim stabilitySim = new StabilitySim(0.5, 0.13, 0.023, 200, 1.33, 0.2,
                0.3, 0.0016, 0.000075, 0.07, 0.08, 44, 1, 0.001);
        */
    }

    public void addGust(){
        gusts.add(new Gust(new Vector(400, 100, 0), 100, 200));
    }

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

    //Methods that allow us to make adjustments to data
    public void setBaseSpin(double value){
        baseSpin = value;
    }

    public void setAirDensity(double value){
        airDensity = value;
    }

    public void setIncrementSize(double value){
        incrementSize = value;
    }
    //

    private void setMass(FlightConfiguration flightConfiguration){
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        mass = cg.weight;
    }

    private void setCgArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        Coordinate cg = MassCalculator.calculateLaunch(flightConfiguration).getCM();
        double cgx = cg.x;
        cgArm = rocketLength - cgx;
    }

    private void setRadius(FlightConfiguration flightConfiguration){
        for (RocketComponent c : flightConfiguration.getCoreComponents()) {
            if (c instanceof SymmetricComponent) {
                double d1 = ((SymmetricComponent) c).getForeRadius() * 2;
                double d2 = ((SymmetricComponent) c).getAftRadius() * 2;
                radius = MathUtil.max(radius, d1, d2) / 2;
            }
        }
    }

    private void setTopDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        topDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCD();
    }

    private void setSideDragCoefficient(FlightConfiguration flightConfiguration){
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();
        sideDragCoefficient = aerodynamicCalculator.getAerodynamicForces(flightConfiguration, conditions,warnings).getCDaxial();
    }

    //Does not currently adjust for overlapping SETS of fins
    private void setSideArea(FlightConfiguration flightConfiguration){
        double finArea = 0;
        double currentArea = 0;
        ArrayList<RocketComponent> rocketComponents = flightConfiguration.getCoreComponents();
        for(RocketComponent component : rocketComponents){
            if (component instanceof SymmetricComponent) {
                double largestRadius = 0;
                double r1 = ((SymmetricComponent) component).getForeRadius();
                double r2 = ((SymmetricComponent) component).getAftRadius();
                if (largestRadius < r1 || largestRadius < r2) {
                    largestRadius = MathUtil.max(largestRadius, r1, r2);
                }
                //Does not account yet for two separate width sections on one piece.
                currentArea += largestRadius * 2 * component.getLength();
            } else if (component instanceof FinSet) {
                double singleFinArea = ((FinSet) component).getPlanformArea();
                double currentFinCount = ((FinSet) component).getFinCount();
                double currentCantAngle = ((FinSet) component).getCantAngle();
                double currentFinThickness = ((FinSet) component).getThickness();
                double currentFinLength = 0;
                double totalFinArea;

                if (component instanceof TrapezoidFinSet) {
                    currentFinLength = ((TrapezoidFinSet) component).getHeight();
                } else if(component instanceof EllipticalFinSet) {
                    currentFinLength = ((EllipticalFinSet) component).getHeight();
                } else if(component instanceof FreeformFinSet) {
                    Coordinate[] finPoints = ((FreeformFinSet) component).getFinPoints();
                    for (Coordinate finPoint : finPoints) {
                        currentFinLength = MathUtil.max(currentFinLength, finPoint.y);
                    }
                }

                if(((FinSet) component).getCantAngle() == 0){
                    totalFinArea = (singleFinArea);
                } else {
                    totalFinArea = ((singleFinArea * Math.cos(currentCantAngle))
                            + ((currentFinLength * currentFinThickness) * Math.cos(Math.PI - currentCantAngle)));
                }
                if (currentFinCount % 2 == 0){
                    totalFinArea *= 2;
                } else {
                    totalFinArea *= 2 * Math.sin((2 * Math.PI) / currentFinCount);
                }
                finArea += totalFinArea;
            }
        }
        sideArea = finArea + currentArea;
    }

    //Does not currently adjust for overlapping SETS of fins
    private void setTopArea(FlightConfiguration flightConfiguration){
        ArrayList<RocketComponent> rocketComponents = flightConfiguration.getCoreComponents();
        double largestRadius = 0;
        double finArea = 0;
        for(RocketComponent component : rocketComponents){
            if (component instanceof SymmetricComponent) {
                double r1 = ((SymmetricComponent) component).getForeRadius();
                double r2 = ((SymmetricComponent) component).getAftRadius();
                if (largestRadius < r1 || largestRadius < r2) {
                    largestRadius = MathUtil.max(largestRadius, r1, r2);
                }
            } else if (component instanceof FinSet) {
                double currentFinArea = ((FinSet) component).getPlanformArea();
                double currentFinCount = ((FinSet) component).getFinCount();
                double currentCantAngle = ((FinSet) component).getCantAngle();
                double currentFinThickness = ((FinSet) component).getThickness();
                double currentFinLength = 0;


                if (component instanceof TrapezoidFinSet) {
                    currentFinLength = ((TrapezoidFinSet) component).getHeight();
                } else if(component instanceof EllipticalFinSet) {
                    currentFinLength = ((EllipticalFinSet) component).getHeight();
                } else if(component instanceof FreeformFinSet) {
                    Coordinate[] finPoints = ((FreeformFinSet) component).getFinPoints();
                    for (Coordinate finPoint : finPoints) {
                        currentFinLength = MathUtil.max(currentFinLength, finPoint.y);
                    }
                }

                if(((FinSet) component).getCantAngle() == 0){
                    finArea += currentFinCount * (currentFinArea);
                } else {
                    finArea += currentFinCount * ((currentFinArea * Math.cos(Math.PI - currentCantAngle))
                            + ((currentFinLength * currentFinThickness) * Math.cos(currentCantAngle)));
                }
            }
        }
        topArea = (MathUtil.pow2(largestRadius) * Math.PI) + finArea;
    }

    private void setDragCPArm(FlightConfiguration flightConfiguration){
        double rocketLength = flightConfiguration.getLength();
        WarningSet warnings = new WarningSet();
        FlightConditions conditions = new FlightConditions(flightConfiguration);
        AerodynamicCalculator aerodynamicCalculator = new BarrowmanCalculator();

        Coordinate cp = aerodynamicCalculator.getWorstCP(flightConfiguration, conditions, warnings);
        double cpx = cp.x;
        dragCPArm = rocketLength - cpx;
    }

    //Adjustments still need to be made to the fin section of this code
    private void setWindCPArm(FlightConfiguration flightConfiguration){
        double currentIntegrationCenter = 0;
        double previousCombinedArea = 0;

        double currentComponentArea;
        double currentComponentAreaCenter = 0;


        ArrayList<RocketComponent> rocketComponents = flightConfiguration.getCoreComponents();
        for(RocketComponent component : rocketComponents) {
            double largestRadius = 0;
            if (component instanceof SymmetricComponent) {
                double r1 = ((SymmetricComponent) component).getForeRadius();
                double r2 = ((SymmetricComponent) component).getAftRadius();
                if (largestRadius < r1 || largestRadius < r2) {
                    largestRadius = MathUtil.max(largestRadius, r1, r2);
                }
            }
            if (component instanceof NoseCone) {
                currentComponentArea = component.getLength() * largestRadius;
                currentComponentAreaCenter = 2 * component.getLength() / 3;
                if(currentIntegrationCenter == 0) {
                    currentIntegrationCenter += currentComponentAreaCenter;
                } else {
                    currentIntegrationCenter += currentComponentAreaCenter * currentComponentArea / previousCombinedArea;
                }
                previousCombinedArea += currentComponentArea;
            } else if (component instanceof SymmetricComponent){
                currentComponentArea = (2 * largestRadius * component.getLength());
                currentComponentAreaCenter = component.getLength() / 2;
                if(currentIntegrationCenter == 0) {
                    currentIntegrationCenter += currentComponentAreaCenter;
                } else {
                    currentIntegrationCenter += currentComponentAreaCenter * currentComponentArea / previousCombinedArea;
                }
                previousCombinedArea += currentComponentArea;
            } else if (component instanceof FinSet) {
                double singleFinArea = ((FinSet) component).getPlanformArea();
                double currentFinCount = ((FinSet) component).getFinCount();
                double currentCantAngle = ((FinSet) component).getCantAngle();
                double currentFinThickness = ((FinSet) component).getThickness();
                double currentFinLength = 0;
                double totalFinArea;

                if (component instanceof TrapezoidFinSet) {
                    currentFinLength = ((TrapezoidFinSet) component).getHeight();
                } else if(component instanceof EllipticalFinSet) {
                    currentFinLength = ((EllipticalFinSet) component).getHeight();
                } else if(component instanceof FreeformFinSet) {
                    Coordinate[] finPoints = ((FreeformFinSet) component).getFinPoints();
                    for (Coordinate finPoint : finPoints) {
                        currentFinLength = MathUtil.max(currentFinLength, finPoint.y);
                    }
                }

                if(((FinSet) component).getCantAngle() == 0){
                    totalFinArea = (singleFinArea);
                } else {
                    totalFinArea = ((singleFinArea * Math.cos(currentCantAngle))
                            + ((currentFinLength * currentFinThickness) * Math.cos(Math.PI - currentCantAngle)));
                }
                if (currentFinCount % 2 == 0){
                    totalFinArea *= 2;
                } else {
                    totalFinArea *= 2 * Math.sin((2 * Math.PI) / currentFinCount);
                }
                currentComponentArea = totalFinArea;
                currentComponentAreaCenter = component.getLength() / 2;
                currentIntegrationCenter += (component.getAxialOffset()) + currentComponentAreaCenter * currentComponentArea / previousCombinedArea;
                previousCombinedArea += currentComponentArea;
            }

        }

        windCPArm = flightConfiguration.getLength() - currentIntegrationCenter;
    }

    private void setThrust(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        thrust = motorConfiguration.getMotor().getAverageThrustEstimate();
    }

    private void setBurnTime(FlightConfiguration flightConfiguration){
        MotorConfiguration motorConfiguration = flightConfiguration.getAllMotors().iterator().next();
        burnTime = motorConfiguration.getMotor().getBurnTimeEstimate();
    }

    private void retrieveData(Simulation sim) {

        FlightConfiguration curConfig = sim.getActiveConfiguration();

        // Use for application
        setMass(curConfig);
        setCgArm(curConfig);
        setRadius(curConfig);
        setTopDragCoefficient(curConfig);
        setSideDragCoefficient(curConfig);
        setTopDragCoefficient(curConfig);
        //setTopDragCoefficient(0.2);
        //SideDragCoefficient is currently returns the same as value as the TopDragCoefficient
        //I would recommend putting in a value manually for testing if this does not seem correct
        setSideDragCoefficient(curConfig);
        //setSideDragCoefficient(0.3);
        setSideArea(curConfig); // Cross Sectional Area
        //setSideArea(0.0016);
        setTopArea(curConfig); // Biggest Diameter + Fin top area * fin amount
        //setTopArea(0.000075);
        setDragCPArm(curConfig); // Bottom to Center of pressure
        //setDragCPArm(0.07);
        setWindCPArm(curConfig); // Cross Sectional Area - Integrate & Find midpoint
        //setWindCPArm(0.08);
        setThrust(curConfig);
        setBurnTime(curConfig);
//        setIncrementSize(0.001);


        // Use for manual testing
        /*
        this.mass = 3.35;
        this.cgArm = 1.61;
        this.radius = 0.25;
        //this.baseSpin = 0;
        //this.airDensity = 1.33;
        this.topDragCoefficient = 0.2;
        this.sideDragCoefficient = 0.3;
        this.sideArea = 0.0016;
        this.topArea = 0.000075;
        this.dragCPArm = 0.07;
        this.windCPArm = 0.08;
        this.thrust = 44;
        this.burnTime = 1;
        //this.incrementSize = 0.001;
        */

    }

    public ArrayList<Vector> runSpinSimulation() {
        return stabilitySim.stabilitySim(gusts);
    }

}
