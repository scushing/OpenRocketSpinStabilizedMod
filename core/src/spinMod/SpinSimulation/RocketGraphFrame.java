package spinMod.SpinSimulation;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.graphics3d.Drawable3D;
import com.orsoncharts.graphics3d.swing.Panel3D;
import javafx.embed.swing.JFXPanel;
import javafx.scene.chart.Chart;
import net.sf.openrocket.document.Simulation;
import spinMod.Rocket;
import spinMod.SpinSimulation.RocketGraphing.*;
import spinMod.Vectors.Vector;
import spinMod.SpinSimulation.PredictedRocketGraphing;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static spinMod.SpinSimulation.RocketGraphing.*;

public class RocketGraphFrame extends JFrame {

    public Chart3DPanel rocketGraph(ArrayList<Vector> data, String graphName, String graphSubtitle){

        RocketGraphing rocketGraphing = new RocketGraphing(graphName, graphSubtitle);
        rocketGraphing.updatePositionSeries(data, graphName);
        //rocketGraphing.updateGraphRange();
        return rocketGraphing.getChart3DPanel();
    }


    public RocketGraphFrame(Simulation sim){

        setLayout(new GridBagLayout());
        GridBagConstraints frameGbc = new GridBagConstraints();

        //Graph for original simulation
        PredictedRocketGraphing predictedRocketGraphing = new PredictedRocketGraphing();
        ArrayList<Vector> data = predictedRocketGraphing.runSpinSimulation(sim);

        //Adding graph to the left side of frame
        Chart3DPanel defaultRocketGraph = rocketGraph(data, "Default Behavior", "");
        defaultRocketGraph.setPreferredSize(new Dimension(600,600));
        defaultRocketGraph.setVisible(true);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(defaultRocketGraph);
        leftPanel.setPreferredSize(new Dimension(600,600));
        leftPanel.setVisible(true);
        frameGbc.gridx = 0;
        frameGbc.gridy = 0;
        add(leftPanel, frameGbc);
        //

        //Adding graph to the left side of frame
        Chart3DPanel spinRocketGraph = rocketGraph(data, "Spin Behavior", "");
        spinRocketGraph.setPreferredSize(new Dimension(600,600));
        spinRocketGraph.setVisible(true);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(spinRocketGraph);
        rightPanel.setPreferredSize(new Dimension(600,600));
        rightPanel.setVisible(true);
        frameGbc.gridx = 1;
        frameGbc.gridy = 0;
        add(rightPanel, frameGbc);
        //


        //Adding panel with data input fields to the right of the frame
        JPanel bottomPanel = new JPanel();
        bottomPanel.setVisible(true);
        frameGbc.gridx = 0;
        frameGbc.gridy = 1;
        frameGbc.gridwidth = 2;
        //frameGbc.fill = GridBagConstraints.HORIZONTAL;
        add(bottomPanel, frameGbc);


        //Adding to right panel
        GridBagLayout gbl = new GridBagLayout();
        bottomPanel.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        //Input Fields
        JTextField spinField = new JTextField(Double.toString(predictedRocketGraphing.getBaseSpin()));
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(spinField,gbc);

        JTextField airDensityField = new JTextField(Double.toString(predictedRocketGraphing.getAirDensity()));
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(airDensityField,gbc);

        JTextField incrementField = new JFormattedTextField(Double.toString(predictedRocketGraphing.getIncrementSize()));
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(incrementField,gbc);
        //

        //Input Field Buttons
        JButton spinButton = new JButton("Change Spin Rate");
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(spinButton,gbc);

        JButton airDensityButton = new JButton("Change Air Density");
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(airDensityButton,gbc);

        JButton incrementButton = new JButton("Change Data Increment Size");
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottomPanel.add(incrementButton,gbc);
        //
        pack();
        //setSize(1200,800);
        setVisible(true);
    }
}
