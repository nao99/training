package com.luxoft.average.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SimpleMovingAverageTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-12
 */
@DisplayName("SimpleMovingAverageTest class")
class SimpleMovingAverageTest {
    @DisplayName("Should be initialized with 0 sum when initial data was not passed")
    @Test
    void shouldBeInitializedWith0SumWithoutInitialData() throws Exception {
        // when
        var simpleMovingAverage = SimpleMovingAverage.of(10);

        // then
        assertThat(simpleMovingAverage.getSum())
            .isZero();
    }

    @DisplayName("Should be initialized with some sum when initial data was passed")
    @Test
    void shouldBeInitializedWithSumWithInitialData() throws Exception {
        // given
        var initialData = new double[] {2.0, 3.0, 5.0};
        var expectedSum = 10.0;

        // when
        var simpleMovingAverage = SimpleMovingAverage.of(5, initialData);

        // then
        assertThat(simpleMovingAverage.getSum())
            .isEqualTo(expectedSum);
    }

    @DisplayName("Should throw an exception when period is less than passed data count")
    @Test
    void shouldThrowExceptionWhenPeriodLessThanPassedDataCount() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> SimpleMovingAverage.of(2, new double[] {2.0, 3.0, 5.0}));
    }

    @DisplayName("Should throw an exception when zero period was passed")
    @Test
    void shouldThrowExceptionWhenZeroPeriodWasPassed() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> SimpleMovingAverage.of(0));
    }

    @DisplayName("Should throw an exception when negative period was passed")
    @Test
    void shouldThrowExceptionWhenNegativePeriodWasPassed() throws Exception {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> SimpleMovingAverage.of(-1));
    }

    @DisplayName("Should return passed period")
    @Test
    void shouldReturnPassedPeriod() throws Exception {
        // given
        var expectedPeriod = 10;
        var simpleMovingAverage = SimpleMovingAverage.of(expectedPeriod);

        // when
        var period = simpleMovingAverage.getPeriod();

        // then
        assertThat(period)
            .isEqualTo(expectedPeriod);
    }

    @DisplayName("Should add a new value when values count is less than period")
    @Test
    void shouldAddNewValueWhenValuesCountLessThanPeriod() throws Exception {
        // given
        var initialData = new double[] {2.0, 3.0, 5.0};
        var simpleMovingAverage = SimpleMovingAverage.of(5, initialData);
        var expectedSumAfterAddingValues = 23.0;

        // when
        simpleMovingAverage.addValue(6.0);
        simpleMovingAverage.addValue(7.0);

        // then
        assertThat(simpleMovingAverage.getSum())
            .isEqualTo(expectedSumAfterAddingValues);
    }

    @DisplayName("Should add a new value to sum and sub an old")
    @Test
    void shouldAddNewValueToSumAndSubOld() throws Exception {
        // given
        var initialData = new double[] {2.0, 3.0, 5.0, 6.0, 7.0};
        var simpleMovingAverage = SimpleMovingAverage.of(5, initialData);
        var expectedSumAfterAddingValues = 29.0;

        // when
        simpleMovingAverage.addValue(8.0);

        // then
        assertThat(simpleMovingAverage.getSum())
            .isEqualTo(expectedSumAfterAddingValues);
    }

    @DisplayName("Should throw an exception when calculate average with not enough values count")
    @Test
    void shouldThrowExceptionWhenCalculateAverageWithNotEnoughValuesCount() throws Exception {
        // given
        var initialData = new double[] {2.0, 3.0, 5.0};
        var simpleMovingAverage = SimpleMovingAverage.of(5, initialData);

        // when / then
        assertThrows(MovingAverageException.class, simpleMovingAverage::getAverage);
    }

    @DisplayName("Should calculate an average when values count is enough")
    @Test
    void shouldCalculateAverageWhenValuesCountIsEnough() throws Exception {
        // given
        var initialData = new double[] {4.0, 3.0, 5.0};
        var simpleMovingAverage = SimpleMovingAverage.of(3, initialData);
        var expectedAverage = 4.0;

        // when
        var average = simpleMovingAverage.getAverage();

        // then
        assertThat(average)
            .isEqualTo(expectedAverage);
    }
}
