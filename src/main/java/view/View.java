package view;

import controller.Controller;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private JTextField timeField = new JTextField(5);
    private JTextField maxArrivalField = new JTextField(5);
    private JTextField minArrivalField = new JTextField(5);
    private JTextField maxServiceField = new JTextField(5);
    private JTextField minServiceField = new JTextField(5);
    private JTextField numberOfServersField = new JTextField(5);
    private JTextField numberOfClientsField = new JTextField(5);
    private JButton startButton = new JButton("Start");
    private JTextArea simulationContent = new JTextArea();

    private Controller controller = new Controller(this);

    public View() {
        resetFields();
        resetTextArea();

        JFrame frame = new JFrame("Queues Management App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);

        JPanel timePanel = getPanel(timeField, "Simulation time: ");
        JPanel maxArrivalPanel = getPanel(maxArrivalField, "Maximum arrival time: ");
        JPanel minArrivalPanel = getPanel(minArrivalField, "Minimum arrival time: ");
        JPanel maxServicePanel = getPanel(maxServiceField, "Maximum service time: ");
        JPanel minServicePanel = getPanel(minServiceField, "Minimum service time: ");
        JPanel numberOfServersPanel = getPanel(numberOfServersField, "Number of servers: ");
        JPanel numberOfClientsPanel = getPanel(numberOfClientsField, "Number of clients: ");
        JScrollPane simulationContentPanel = getTextAreaPanel(simulationContent);

        JPanel mainPanel = new JPanel();
        mainPanel.add(timePanel);
        mainPanel.add(maxArrivalPanel);
        mainPanel.add(minArrivalPanel);
        mainPanel.add(maxServicePanel);
        mainPanel.add(minServicePanel);
        mainPanel.add(numberOfClientsPanel);
        mainPanel.add(numberOfServersPanel);
        mainPanel.add(startButton);
        startButton.addActionListener(controller);
        mainPanel.add(simulationContentPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public void resetFields() {
        timeField.setText("");
        maxArrivalField.setText("");
        minArrivalField.setText("");
        maxServiceField.setText("");
        minServiceField.setText("");
        numberOfServersField.setText("");
        numberOfClientsField.setText("");
    }

    public void resetTextArea() {
        simulationContent.setText("");
    }

    @NotNull
    private JPanel getPanel(JTextField field, String labelText) {
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(labelText);

        newPanel.add(newLabel);
        newPanel.add(field);
        newPanel.setLayout(new FlowLayout());

        return newPanel;
    }

    @NotNull
    private JScrollPane getTextAreaPanel(JTextArea content) {
        JScrollPane simulationContentPanel = new JScrollPane(content);
        simulationContentPanel.setPreferredSize(new Dimension(480, 400));

        return simulationContentPanel;
    }

    public String getTimeField() {
        return timeField.getText();
    }

    public String getMaxArrivalField() {
        return maxArrivalField.getText();
    }

    public String getMinArrivalField() {
        return minArrivalField.getText();
    }

    public String getMaxServiceField() {
        return maxServiceField.getText();
    }

    public String getMinServiceField() {
        return minServiceField.getText();
    }

    public String getNumberOfServersField() {
        return numberOfServersField.getText();
    }

    public String getNumberOfClientsField() {
        return numberOfClientsField.getText();
    }

    public void setSimulationContent(String simulationContent) {
        this.simulationContent.setText(simulationContent);
    }

    public boolean checkArrivalData() {
        return maxArrivalField.getText().equals("") || minArrivalField.getText().equals("");
    }

    public boolean checkServiceData() {
        return maxServiceField.getText().equals("") || minServiceField.getText().equals("");
    }

    public boolean areEmptyFields() {
        return timeField.getText().equals("") || checkArrivalData() || checkServiceData() ||
                numberOfServersField.getText().equals("") || numberOfClientsField.getText().equals("");
    }
}
