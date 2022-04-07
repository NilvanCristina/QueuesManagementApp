package controller;

import view.View;
import model.SimulationManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private View view;
    private SimulationManager simulator;

    public Controller(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (view.areEmptyFields()) {
            view.setSimulationContent("Can't start simulation! There are empty fields!");
        } else {
            int timeLimit = Integer.parseInt(view.getTimeField());
            int maxArrivalTime = Integer.parseInt(view.getMaxArrivalField());
            int minArrivalTime = Integer.parseInt(view.getMinArrivalField());
            int maxServiceTime = Integer.parseInt(view.getMaxServiceField());
            int minServiceTime = Integer.parseInt(view.getMinServiceField());
            int numberOfServers = Integer.parseInt(view.getNumberOfServersField());
            int numberOfClients = Integer.parseInt(view.getNumberOfClientsField());

            simulator = new SimulationManager(timeLimit, maxArrivalTime, minArrivalTime, maxServiceTime,
                    minServiceTime, numberOfServers, numberOfClients);

            Thread thread = new Thread(simulator);
            thread.start();
        }
    }
}
