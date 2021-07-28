package com.luxoft.robot.model;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RobotTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-25
 */
class RobotTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() throws Exception {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() throws Exception {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @DisplayName("Should start walking from the left leg")
    @Test
    void shouldStartWalkingFromLeftLeg() throws Exception {
        // given
        var robot = new Robot(true);

        // when
        robot.walk();
        doSomethingUseless();

        robot.stop();

        // then
        var output = outContent.toString();
        var outputArray = output.split("\\n");

        for (int i = 0; i < outputArray.length; i++) {
            if (i % 2 == 0) {
                assertThat(outputArray[i])
                    .isEqualTo("LEFT");

                continue;
            }

            assertThat(outputArray[i])
                .isEqualTo("RIGHT");
        }
    }

    @DisplayName("Should start walking from the right leg")
    @Test
    void shouldStartWalkingFromRightLeg() throws Exception {
        // given
        var robot = new Robot(false);

        // when
        robot.walk();
        doSomethingUseless();

        robot.stop();

        // then
        var output = outContent.toString();
        var outputArray = output.split("\\n");

        for (int i = 0; i < outputArray.length; i++) {
            if (i % 2 == 0) {
                assertThat(outputArray[i])
                    .isEqualTo("RIGHT");

                continue;
            }

            assertThat(outputArray[i])
                .isEqualTo("LEFT");
        }
    }

    @DisplayName("Should stop walking")
    @Test
    void shouldStopWalking() throws Exception {
        // given
        var robot = new Robot(false);

        // when / then
        robot.walk();
        assertThat(robot.walking())
            .isTrue();

        robot.stop();
        assertThat(robot.walking())
            .isFalse();
    }

    // TODO: find a better way to wait a while excepting the Thread.sleep() method
    private void doSomethingUseless() {
        var dataStream = new Random(1)
            .ints(10000, 0, 1000);

        dataStream.sorted()
            .distinct()
            .sum();
    }
}
