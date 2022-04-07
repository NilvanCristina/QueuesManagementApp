package model;

import view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxArrivalTime;
    private int minArrivalTime ;
    private int maxServiceTime;
    private int minServiceTime;
    private int numberOfServers;
    private int numberOfClients;
    private List<Task> generatedTasks;
    private ServerManager serverManager;
    private View view;

    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime, int maxProcessingTime,
                             int minProcessingTime, int numberOfServers, int numberOfClients) {
        this.timeLimit = timeLimit;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.maxServiceTime = maxProcessingTime;
        this.minServiceTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.generatedTasks = Collections.synchronizedList(new ArrayList<>());
        this.serverManager = new ServerManager(numberOfServers, numberOfClients);
        this.view = new View();

        generateRandomTasks();
    }

    public synchronized void generateRandomTasks() {
        for (int i = 0; i < numberOfClients; i++) {
            int randomId = (int)Math.floor(Math.random() * (numberOfClients - 1 + 1) + 1);
            int randomArrival = (int)Math.floor(Math.random() * (maxArrivalTime - minArrivalTime + 1) + minArrivalTime);
            int randomProcessing = (int)Math.floor(Math.random() * (maxServiceTime - minServiceTime + 1) + minServiceTime);
            Task randomTask = new Task(randomId, randomArrival, randomProcessing);
            generatedTasks.add(randomTask);
        }

        Collections.sort(generatedTasks);
    }

    public synchronized float getAverageServingTime() {
        int totalServiceTime = 0;

        for (Task task: generatedTasks)
            totalServiceTime += task.getServiceTime();

        return (float) (totalServiceTime / generatedTasks.size());
    }

    public synchronized String getStringCurrentEvent(int currentTime) {
        String toDisplay = "";
        toDisplay = toDisplay.concat("Time: " + currentTime + "\n");
        toDisplay = toDisplay.concat("Waiting clients: ");

        if (generatedTasks.size() > 0) {
            for (Task task : generatedTasks)
                toDisplay = toDisplay.concat(task.toString() + " ");
        } else
            toDisplay = toDisplay.concat("no more waiting clients");

        toDisplay = toDisplay.concat("\n");

        for (Server server: serverManager.getServers())
            toDisplay = toDisplay.concat(server.toString());

        toDisplay = toDisplay.concat("\n");

        return toDisplay;
    }

    public synchronized String getStringAverageServing() {
        String toDisplay = "";
        toDisplay = toDisplay.concat("Average serving time: " + getAverageServingTime() + "\n");

        return toDisplay;
    }

    public synchronized String getStringAverageWaiting() {
        String toDisplay = "";
        toDisplay = toDisplay.concat("Average waiting time: " + serverManager.getAverageWaitingTime() + "\n");

        return toDisplay;
    }

    public synchronized void writeEventsInFile(String stringEvents, String stringDataOfEvents) {
        String logOfEvents = "";
        logOfEvents = logOfEvents.concat(stringEvents);
        logOfEvents = logOfEvents.concat(stringDataOfEvents);

        Write writeObject = new Write("OutputFile.txt", logOfEvents);
        writeObject.writeOutput();
    }

    public void run() {
        String logOfEvents = "";
        String currentEvent;
        String dataOfEvents = getStringAverageServing();
        int currentTime = 0;
        int maximum = Integer.MIN_VALUE;
        int peakHour = 0;

        while(currentTime < timeLimit) {
            if(!generatedTasks.isEmpty()) {
                for(int i = 0; i < numberOfServers && !generatedTasks.isEmpty(); i++) {
                    if(currentTime >= generatedTasks.get(0).getArrivalTime()) {
                        serverManager.dispatchTask(generatedTasks.get(0));
                        generatedTasks.remove(0);
                    }
                }
            }

            if (serverManager.getCurrentNumberOfTasks() > maximum) {
                maximum = serverManager.getCurrentNumberOfTasks();
                peakHour = currentTime;
            }

            currentEvent = getStringCurrentEvent(currentTime);
            logOfEvents = logOfEvents.concat(currentEvent);
            view.setSimulationContent(currentEvent);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime++;
        }

        serverManager.stopServers();

        currentEvent = getStringCurrentEvent(currentTime);
        logOfEvents = logOfEvents.concat(currentEvent);
        view.setSimulationContent(currentEvent);

        dataOfEvents = dataOfEvents.concat(getStringAverageWaiting());
        dataOfEvents = dataOfEvents.concat("Peak hour: " + peakHour);

        writeEventsInFile(logOfEvents, dataOfEvents);
    }
}
