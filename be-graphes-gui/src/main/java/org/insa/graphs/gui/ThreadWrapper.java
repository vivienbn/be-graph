package org.insa.graphs.gui;

import java.time.Duration;
import java.time.Instant;

public class ThreadWrapper implements RunningAction {

    // Thread hold by this wrapper.
    private Thread thread;

    // Starting time of the thread.
    Instant startingTime;

    // MainWindow
    private MainWindow mainWindow;

    public ThreadWrapper(MainWindow mainWindow) {
        this.thread = null;
        this.mainWindow = mainWindow;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void startThread() {
        this.startingTime = Instant.now();
        this.thread.start();
    }

    public Thread getThread() {
        return this.thread;
    }

    @Override
    public boolean isRunning() {
        return thread != null && thread.isAlive();
    }

    @SuppressWarnings("removal")
    @Override
    public void interrupt() {
        thread.stop();
        this.mainWindow.clearCurrentThread();
    }

    @Override
    public Instant getStartingTime() {
        return startingTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(getStartingTime(), Instant.now());
    }

    @Override
    public String getInformation() {
        return getClass().getName();
    }

}
