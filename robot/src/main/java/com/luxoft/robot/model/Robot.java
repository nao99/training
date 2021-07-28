package com.luxoft.robot.model;

/**
 * Robot class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-21
 */
public class Robot {
    private final Object robotWalkingMonitor = new Object();

    private final Thread leftLegThread;
    private final Thread rightLegThread;

    private boolean walking;
    private volatile boolean currentLeg;

    public Robot(boolean startFromLeft) {
        this.leftLegThread = new Thread(new Leg("LEFT", startFromLeft));
        this.rightLegThread = new Thread( new Leg("RIGHT", !startFromLeft));
        this.walking = false;
        this.currentLeg = true;
    }

    public void walk() {
        if (walking) {
            return;
        }

        leftLegThread.start();
        rightLegThread.start();

        walking = true;
    }

    public void stop() throws InterruptedException {
        if (!walking) {
            return;
        }

        leftLegThread.interrupt();
        rightLegThread.interrupt();

        leftLegThread.join();
        rightLegThread.join();

        walking = false;
    }

    public boolean walking() {
        return walking;
    }

    private class Leg implements Runnable {
        private final String name;
        private final boolean lead;

        private Leg(String name, boolean lead) {
            this.name = name;
            this.lead = lead;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (robotWalkingMonitor) {
                    while (currentLeg != lead) {
                        try {
                            // TODO: find a way to avoid double checking
                            if (Thread.currentThread().isInterrupted()) {
                                return;
                            }

                            robotWalkingMonitor.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    doStep();

                    currentLeg = !lead;
                    robotWalkingMonitor.notifyAll();
                }
            }
        }

        private void doStep() {
            System.out.println(name);
        }
    }
}
