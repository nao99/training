package com.luxoft.robot.model;

import lombok.AllArgsConstructor;

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

    public Robot() {
        this.leftLegThread = new Thread(new Leg("LEFT"));
        this.rightLegThread = new Thread( new Leg("RIGHT"));
        this.walking = false;
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

    @AllArgsConstructor
    private class Leg implements Runnable {
        private final String name;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (robotWalkingMonitor) {
                    doStep();
                    robotWalkingMonitor.notifyAll();

                    try {
                        robotWalkingMonitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        private void doStep() {
            System.out.println(name);
        }
    }
}
