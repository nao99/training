package com.luxoft.average.model;

/**
 * MovingAverage interface
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-12
 */
public interface MovingAverage {
    /**
     * Adds a new value for average calculation
     *
     * @param value a new value
     */
    void addValue(double value);

    /**
     * Gets an average
     *
     * @return an average
     * @throws MovingAverageException if unable to get average (e.g. values count is less than period)
     */
    double getAverage() throws MovingAverageException;

    /**
     * Gets a calculation's period (e.g. 10 elements)
     *
     * @return a calculation's period
     */
    int getPeriod();
}
