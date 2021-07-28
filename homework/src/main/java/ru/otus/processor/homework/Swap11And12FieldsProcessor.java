package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * Swap11And12FieldsProcessor class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class Swap11And12FieldsProcessor implements Processor {
    @Override
    public Message process(Message message) {
        var messageField11 = message.getField11();
        var messageField12 = message.getField12();

        var messageBuilder = message.toBuilder();
        messageBuilder
            .field11(messageField12)
            .field12(messageField11);

        return messageBuilder.build();
    }
}
