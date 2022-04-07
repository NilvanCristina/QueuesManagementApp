package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private int serverId;
    private BlockingQueue<Task> tasks;
    private AtomicInteger currentWaitingTime;
    private AtomicInteger totalWaitingTime;
    private int totalNoTasks;
    private boolean stopped;

    public Server(int serverId) {
        this.serverId = serverId;
        this.tasks = new LinkedBlockingQueue<>();
        this.currentWaitingTime = new AtomicInteger(0);
        this.totalWaitingTime = new AtomicInteger(0);
        this.totalNoTasks = 0;
        this.stopped = false;
    }

    public synchronized int getNumberOfTasks() {
        return tasks.size();
    }

    public synchronized int getIntCurrentWaitingTime() {
        return currentWaitingTime.intValue();
    }

    public AtomicInteger getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public synchronized void addTask(Task newTask) {
        tasks.add(newTask);
        currentWaitingTime.addAndGet(newTask.getServiceTime());
        totalWaitingTime.addAndGet(newTask.getServiceTime());
        totalNoTasks += 1;
    }

    public synchronized float getServerAverageWaiting() {
        return (float) (totalWaitingTime.intValue() / totalNoTasks);
    }

    public synchronized void stopServer() {
        stopped = true;
    }

    public void run() {
        while(true) {
            if(!tasks.isEmpty()) {
                try {
                    Task currentTask = tasks.peek();
                    int currentTaskServiceTime = currentTask.getServiceTime();
                    Thread.sleep(currentTaskServiceTime * 1000L);

                    currentTask.setServiceTime(0);

                    int auxiliary = currentWaitingTime.intValue();
                    auxiliary -= currentTaskServiceTime;
                    currentWaitingTime.set(auxiliary);

                    tasks.remove();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(stopped) {
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        if (tasks.isEmpty())
            return "Queue " + serverId +
                    ": closed\n";
        else
            return "Queue " + serverId +
                    ": " + tasks + "\n";
    }
}
