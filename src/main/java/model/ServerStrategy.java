package model;

import java.util.List;

public class ServerStrategy {
    public synchronized void addTaskToServer(List<Server> servers, Task task) {
        int minimumWaitingPeriod = Integer.MAX_VALUE;

        for (Server server: servers) {
            if (server.getIntCurrentWaitingTime() < minimumWaitingPeriod) {
                minimumWaitingPeriod = server.getIntCurrentWaitingTime();
            }
        }

        for (Server server: servers) {
            if (server.getIntCurrentWaitingTime() == minimumWaitingPeriod) {
                server.addTask(task);
                break;
            }
        }
    }
}
