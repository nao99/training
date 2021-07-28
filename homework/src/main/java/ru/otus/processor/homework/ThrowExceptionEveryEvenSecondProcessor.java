package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.time.CurrentTimeDefiner;

/**
 * ThrowExceptionEveryEvenSecondProcessor class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-07-12
 */
public class ThrowExceptionEveryEvenSecondProcessor implements Processor {
    private final CurrentTimeDefiner currentSecondsDefiner;

    public ThrowExceptionEveryEvenSecondProcessor(CurrentTimeDefiner currentSecondsDefiner) {
        this.currentSecondsDefiner = currentSecondsDefiner;
    }

    @Override
    public Message process(Message message) {
        var currentTime = currentSecondsDefiner.time();
        var currentTimeSecond = currentTime.getSecond();

        if (currentTimeSecond % 2 == 0) {
            throw new EvenSecondException("Unable to process message on even seconds");
        }

        return message;
    }
}
