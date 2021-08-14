package com.luxoft.average;

import com.luxoft.average.model.MovingAverage;
import com.luxoft.average.model.MovingAverageException;
import com.luxoft.average.model.SimpleMovingAverage;

/**
 * DemonstrationApplication class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-12
 */
public class DemonstrationApplication {
    public static void main(String[] args) throws Exception {
        var period = 4;

        MovingAverage movingAverage = SimpleMovingAverage.of(period, new double[] {21.0, 13.0, 54.0});
        var average = 0.0;

        try {
            average = movingAverage.getAverage();
        } catch (MovingAverageException e) {
            System.out.println(e.getMessage());
        }

        movingAverage.addValue(18.0);
        average = movingAverage.getAverage();

        System.out.printf("Average: %f%n", average);

        movingAverage.addValue(4.0);
        average = movingAverage.getAverage();

        System.out.printf("New average: %f%n", average);
    }
}
