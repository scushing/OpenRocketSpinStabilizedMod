package spinMod.SpinSimulation;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.data.xyz.XYZSeries;
import net.sf.openrocket.document.Simulation;
import spinMod.Vectors.Vector;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class RocketGraphFrame extends JFrame {

    private OpenRocketGraphs openRocketGraphs;

    private Chart3DPanel defaultAndSpinGraph;
    private Chart3DPanel defaultGraph;
    private Chart3DPanel spinGraph;

    private Hashtable<String, ArrayList<Vector>> defaultAndSpinHashTable;
    private Hashtable<String, ArrayList<Vector>> defaultHashTable;
    private Hashtable<String, ArrayList<Vector>> spinHashTable;

    private PredictedRocketGraphing predictedDefault, predictedSpin;
    private ArrayList<Vector> defaultData, spinData;

    private JPanel leftPanel = new JPanel();
    private JPanel middlePanel = new JPanel();
    private JPanel rightPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    GridBagConstraints frameGbc = new GridBagConstraints();

    boolean testValue = true;

    public RocketGraphFrame(Simulation sim){

        defaultAndSpinHashTable = new Hashtable<>();
        defaultHashTable = new Hashtable<>();
        spinHashTable = new Hashtable<>();

        predictedDefault = new PredictedRocketGraphing(sim);
        predictedSpin = new PredictedRocketGraphing(sim);

        defaultData = predictedDefault.runSpinSimulation();
        spinData = predictedSpin.runSpinSimulation();

        defaultAndSpinHashTable.put("Default Data", defaultData);
        defaultAndSpinHashTable.put("Spin Data", spinData);
        defaultHashTable.put("Default Data", defaultData);
        spinHashTable.put("Spin Data", spinData);

        openRocketGraphs = new OpenRocketGraphs();

        openRocketGraphs.addGraphPanel(defaultAndSpinHashTable, "Default and Spin Data", "");
        openRocketGraphs.addGraphPanel(defaultHashTable, "Default Data", "");
        openRocketGraphs.addGraphPanel(spinHashTable, "Spin Data", "");

        defaultAndSpinGraph = openRocketGraphs.getGraphPanel("Default and Spin Data");
        defaultGraph = openRocketGraphs.getGraphPanel("Default Data");
        spinGraph = openRocketGraphs.getGraphPanel("Spin Data");


        //setLayout(new GridBagLayout());
        setLayout(new GridBagLayout());
        addGraphsToFrame();
        addInputsToFrame();
        pack();
        setVisible(true);
        openTestDataFrame();
    }

    public void addGraphsToFrame(){
        //Adding graph to the left side of frame
        defaultAndSpinGraph.setPreferredSize(new Dimension(500,500));
        defaultAndSpinGraph.setVisible(true);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(defaultAndSpinGraph);
        leftPanel.setPreferredSize(new Dimension(500,500));
        leftPanel.setVisible(true);
        frameGbc.gridx = 0;
        frameGbc.gridy = 0;
        add(leftPanel, frameGbc);
        //

        //Adding graph to the middle of frame
        defaultGraph.setPreferredSize(new Dimension(500,500));
        defaultGraph.setVisible(true);

        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        middlePanel.add(defaultGraph);
        middlePanel.setPreferredSize(new Dimension(500,500));
        middlePanel.setVisible(true);
        frameGbc.gridx = 1;
        frameGbc.gridy = 0;
        add(middlePanel, frameGbc);
        //

        //Adding graph to the right side of frame
        spinGraph.setPreferredSize(new Dimension(500,500));
        spinGraph.setVisible(true);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(spinGraph);
        rightPanel.setPreferredSize(new Dimension(500,500));
        rightPanel.setVisible(true);
        frameGbc.gridx = 2;
        frameGbc.gridy = 0;
        add(rightPanel, frameGbc);
        //
    }

    public void addInputsToFrame(){
        //Adding panel with data input fields to the right of the frame
        bottomPanel = new JPanel();
        bottomPanel.setVisible(true);
        frameGbc.gridx = 0;
        frameGbc.gridy = 1;
        frameGbc.gridwidth = 3;
        //frameGbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, frameGbc);


        //Adding to bottom panel
        GridBagLayout gbl = new GridBagLayout();
        bottomPanel.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();

        //Field Labels
        JLabel spinLabel = new JLabel("Spin (rpm)");
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(spinLabel,gbc);

        JLabel airDensityLabel = new JLabel("Air Density (?)");
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(airDensityLabel,gbc);

        JLabel incrementLabel = new JLabel("Increment Size (s)");
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(incrementLabel,gbc);
        //

        //Input Fields
        JTextField spinField = new JTextField(Double.toString(predictedDefault.getBaseSpin()));
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(spinField,gbc);

        JTextField airDensityField = new JTextField(Double.toString(predictedDefault.getAirDensity()));
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(airDensityField,gbc);

        JTextField incrementField = new JFormattedTextField(Double.toString(predictedDefault.getIncrementSize()));
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(incrementField,gbc);
        //

        //Update Button
        JButton updateButton = new JButton("Update Graphs");
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        bottomPanel.add(updateButton,gbc);
        if (testValue) {
            updateButton.addActionListener(e -> {
                testValue = false;
                double spinVal = Double.parseDouble(spinField.getText());
                double airDensityVal = Double.parseDouble(airDensityField.getText());
                double incrementVal = Double.parseDouble(incrementField.getText());

                updateSimulationGraphs(spinVal, airDensityVal, incrementVal);
            });
        }
        //
    }

    public void updateSimulationGraphs(double spinVal, double airDensityVal, double incrementVal){

        //Sets new data variables for default data
        predictedDefault.setAirDensity(airDensityVal);
        predictedDefault.setIncrementSize(incrementVal);

        //Sets new data variables for spin data
        predictedSpin.setBaseSpin(spinVal);
        predictedSpin.setAirDensity(airDensityVal);
        predictedSpin.setIncrementSize(incrementVal);

        //Runs new simulations for default and spin
        defaultData = predictedDefault.runSpinSimulation();
        spinData = predictedSpin.runSpinSimulation();

        //Replaces old values in default and spin hash tables
        defaultAndSpinHashTable.replace("Default Data", defaultData);
        defaultAndSpinHashTable.replace("Spin Data", spinData);
        defaultHashTable.replace("Default Data", defaultData);
        spinHashTable.replace("Spin Data", spinData);

        //Replaces graph panels in openRocketGraphs object
        openRocketGraphs.addGraphPanel(defaultAndSpinHashTable, "Default and Spin Data", "");
        openRocketGraphs.addGraphPanel(defaultHashTable, "Default Data", "");
        openRocketGraphs.addGraphPanel(spinHashTable, "Spin Data", "");

        //Replaces graphs
        defaultAndSpinGraph = openRocketGraphs.getGraphPanel("Default and Spin Data");
        defaultGraph = openRocketGraphs.getGraphPanel("Default Data");
        spinGraph = openRocketGraphs.getGraphPanel("Spin Data");

        //revalidate();

        removeAll();
        validate();

        setLayout(new GridBagLayout());
        addGraphsToFrame();
        addInputsToFrame();
        //getContentPane().update(getGraphics());
        validate();
        repaint();
        pack();

    }

    public void openTestDataFrame(){
        JFrame collectedData = new JFrame();

        collectedData.setLayout(new GridBagLayout());

        GridBagConstraints gbbc = new GridBagConstraints();

        String [][] controlArray = {{"core/src/spinMod/SpinSimulation/test-data/Control.txt", "Control"}};
        String [][] cantedArray = {{"core/src/spinMod/SpinSimulation/test-data/Canted.txt", "Canted"}};
        String [][] camberedArray = {{"core/src/spinMod/SpinSimulation/test-data/Cambered_2.txt", "Cambered"}};
        openRocketGraphs.addGraphPanel(controlArray, "Control", "");
        openRocketGraphs.addGraphPanel(cantedArray, "Canted", "");
        openRocketGraphs.addGraphPanel(camberedArray, "Cambered", "");

        gbbc.gridx = 0;
        gbbc.gridy = 0;
        openRocketGraphs.getGraphPanel("Control").setPreferredSize(new Dimension(500,500));
        collectedData.add(openRocketGraphs.getGraphPanel("Control"),gbbc);
        gbbc.gridx = 1;
        gbbc.gridy = 0;
        openRocketGraphs.getGraphPanel("Canted").setPreferredSize(new Dimension(500,500));
        collectedData.add(openRocketGraphs.getGraphPanel("Canted"),gbbc);
        gbbc.gridx = 2;
        gbbc.gridy = 0;
        openRocketGraphs.getGraphPanel("Cambered").setPreferredSize(new Dimension(500,500));
        collectedData.add(openRocketGraphs.getGraphPanel("Cambered"),gbbc);
        collectedData.pack();
        collectedData.setVisible(true);


    }
}
