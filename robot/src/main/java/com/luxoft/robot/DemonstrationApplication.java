package com.luxoft.robot;

import com.luxoft.robot.model.Robot;

/**
 * DemonstrationApplication class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-21
 */
public class DemonstrationApplication {
    public static void main(String[] args) throws InterruptedException {
        var robot = new Robot(true);
        robot.walk();

        Thread.sleep(1000);
        robot.stop();
    }
}
