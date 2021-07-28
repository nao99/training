package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * ThrowExceptionEveryEvenSecondProcessorTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
@DisplayName("ThrowExceptionEveryEvenSecondProcessor class")
class ThrowExceptionEveryEvenSecondProcessorTest {
    @DisplayName("Should throw the EvenSecondException when current second is even")
    @Test
    void shouldThrowExceptionWhenCurrentSecondIsEven() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .build();

        var processor = new ThrowExceptionEveryEvenSecondProcessor(() -> LocalDateTime.of(2021, 7, 13, 17, 0, 20));

        // when / then
        assertThatThrownBy(() -> processor.process(message))
            .isInstanceOf(EvenSecondException.class);
    }

    @DisplayName("Should not throw any exception when current second is odd")
    @Test
    void shouldNotThrowAnyExceptionWhenCurrentSecondIsOdd() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .build();

        var processor = new ThrowExceptionEveryEvenSecondProcessor(() -> LocalDateTime.of(2021, 7, 13, 17, 0, 21));

        // when / then
        var resultedMessage = assertDoesNotThrow(() -> processor.process(message));

        assertThat(message)
            .isSameAs(resultedMessage);
    }
}
