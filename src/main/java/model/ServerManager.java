package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerManager {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private ServerStrategy strategy;

    public ServerManager(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = Collections.synchronizedList(new ArrayList<>());
        this.strategy = new ServerStrategy();

        generateServers(maxNoServers);
    }

    private void generateServers(int maxNoServers) {
        for (int index = 0; index < maxNoServers; index++) {
            Server newServer = new Server(index + 1);
            servers.add(newServer);
            Thread newThread = new Thread(newServer);
            newThread.start();
        }
    }

    public synchronized List<Server> getServers() {
        return servers;
    }

    public synchronized void dispatchTask(Task task) {
        strategy.addTaskToServer(servers, task);
    }

    public synchronized void stopServers() {
        for(Server serv: servers)
            serv.stopServer();
    }

    public synchronized float getAverageWaitingTime() {
        float totalAverageTime = 0;

        for (Server server: servers)
            totalAverageTime += server.getServerAverageWaiting();

        return totalAverageTime / maxNoServers;
    }

    public synchronized int getCurrentNumberOfTasks() {
        int currentNumberOfTasks = 0;

        for (Server server: servers)
            currentNumberOfTasks += server.getNumberOfTasks();

        return currentNumberOfTasks;
    }
}
