package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Swap11And12FieldsProcessorTest class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
@DisplayName("Swap11And12FieldsProcessor class")
class Swap11And12FieldsProcessorTest {
    private Swap11And12FieldsProcessor processor;

    @BeforeEach
    public void setUp() throws Exception {
        processor = new Swap11And12FieldsProcessor();
    }

    @DisplayName("Should swap field11 and field12 when both are not nullable")
    @Test
    void shouldSwapField11AndField12WhenBothAreNotNullable() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .field11("field11")
            .field12("field12")
            .build();

        // when
        var messageWithSwappedFields = processor.process(message);

        // then
        assertThat(message)
            .isEqualTo(messageWithSwappedFields);

        assertThat(message.getField11())
            .isSameAs(messageWithSwappedFields.getField12());

        assertThat(message.getField12())
            .isSameAs(messageWithSwappedFields.getField11());
    }

    @DisplayName("Should swap field11 and field12 when field11 is nullable")
    @Test
    void shouldSwapField11AndField12WhenField11IsNullable() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .field12("field12")
            .build();

        // when
        var messageWithSwappedFields = processor.process(message);

        // then
        assertThat(message)
            .isEqualTo(messageWithSwappedFields);

        assertThat(message.getField12())
            .isSameAs(messageWithSwappedFields.getField11());

        assertThat(messageWithSwappedFields.getField12())
            .isNull();
    }

    @DisplayName("Should swap field11 and field12 when field12 is nullable")
    @Test
    void shouldSwapField11AndField12WhenField12IsNullable() throws Exception {
        // given
        var message = new Message.Builder(1L)
            .field11("field11")
            .build();

        // when
        var messageWithSwappedFields = processor.process(message);

        // then
        assertThat(message)
            .isEqualTo(messageWithSwappedFields);

        assertThat(message.getField11())
            .isSameAs(messageWithSwappedFields.getField12());

        assertThat(messageWithSwappedFields.getField11())
            .isNull();
    }
}
