package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.time.CurrentSecondsDefiner;

/**
 * ThrowExceptionEveryEvenSecondProcessor class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class ThrowExceptionEveryEvenSecondProcessor implements Processor {
    private final CurrentSecondsDefiner currentSecondsDefiner;

    public ThrowExceptionEveryEvenSecondProcessor(CurrentSecondsDefiner currentSecondsDefiner) {
        this.currentSecondsDefiner = currentSecondsDefiner;
    }

    @Override
    public Message process(Message message) {
        var currentSeconds = currentSecondsDefiner.define();
        if (currentSeconds % 2 == 0) {
            throw new EvenSecondException("Unable to process message on even seconds");
        }

        return message;
    }
}
