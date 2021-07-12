package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.time.CurrentSecondsDefiner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

/**
 * ThrowExceptionEveryEvenSecondProcessorTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
@DisplayName("ThrowExceptionEveryEvenSecondProcessor class")
class ThrowExceptionEveryEvenSecondProcessorTest {
    private CurrentSecondsDefiner currentSecondsDefiner;
    private ThrowExceptionEveryEvenSecondProcessor processor;

    @BeforeEach
    public void setUp() throws Exception {
        currentSecondsDefiner = mock(CurrentSecondsDefiner.class);
        processor = new ThrowExceptionEveryEvenSecondProcessor(currentSecondsDefiner);
    }

    @DisplayName("Should throw the EvenSecondException when current second is even")
    @Test
    void shouldThrowExceptionWhenCurrentSecondIsEven() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .build();

        // when / then
        when(currentSecondsDefiner.define())
            .thenReturn(1626071644);

        assertThatThrownBy(() -> processor.process(message))
            .isInstanceOf(EvenSecondException.class);

        verify(currentSecondsDefiner, times(1))
            .define();
    }

    @DisplayName("Should not throw any exception when current second is odd")
    @Test
    void shouldNotThrowAnyExceptionWhenCurrentSecondIsOdd() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .build();

        // when / then
        when(currentSecondsDefiner.define())
            .thenReturn(1626071645);

        var resultedMessage = assertDoesNotThrow(() -> processor.process(message));

        verify(currentSecondsDefiner, times(1))
            .define();

        assertThat(message)
            .isSameAs(resultedMessage);
    }
}
