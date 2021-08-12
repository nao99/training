package com.luxoft.average.model;

import java.util.Deque;
import java.util.LinkedList;

/**
 * SimpleMovingAverage class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-12
 */
public class SimpleMovingAverage implements MovingAverage {
    private final int period;

    private final Deque<Double> values;
    private double sum;

    private SimpleMovingAverage(int period, double[] initialValues) {
        if (period <= 0) {
            throw new IllegalArgumentException(String.format("Period should be more than 0, but %d passed", period));
        }

        if (period < initialValues.length) {
            throw new IllegalArgumentException("Period should be greater than initial values count");
        }

        this.period = period;

        this.values = new LinkedList<>();
        for (var value : initialValues) {
            this.values.add(value);
            this.sum += value;
        }
    }

    public static SimpleMovingAverage of(int period) {
        return new SimpleMovingAverage(period, new double[] {});
    }

    public static SimpleMovingAverage of(int period, double[] initialValues) {
        return new SimpleMovingAverage(period, initialValues);
    }

    public double getSum() {
        return sum;
    }

    @Override
    public void addValue(double value) {
        sum += value;
        values.addLast(value);

        if (values.size() <= period) {
            return;
        }

        sum -= values.getFirst();
        values.removeFirst();
    }

    @Override
    public double getAverage() throws MovingAverageException {
        if (values.size() < period) {
            throw new MovingAverageException("Unable to get average when values are not enough");
        }

        return sum / period;
    }

    @Override
    public int getPeriod() {
        return period;
    }
}
