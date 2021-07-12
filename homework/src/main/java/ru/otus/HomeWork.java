package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.homework.Swap11And12FieldsProcessor;
import ru.otus.processor.homework.ThrowExceptionEveryEvenSecondProcessor;
import ru.otus.time.CurrentSecondsDefiner;

import java.util.List;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var processors = List.of(
            new ProcessorConcatFields(),
            new ThrowExceptionEveryEvenSecondProcessor(new CurrentSecondsDefiner()),
            new LoggerProcessor(new Swap11And12FieldsProcessor())
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> logger.error(ex.getMessage()));

        var historyListener = new HistoryListener(null);
        complexProcessor.addListener(historyListener);

        var message1 = new Message.Builder(1L)
            .field1("field1")
            .field6("field6")
            .field10("field10")
            .field11("field11")
            .field12("field12")
            .build();

        var message2 = new Message.Builder(1L)
            .field1("field1")
            .field2("field2")
            .field12("field12")
            .build();

        var message3 = new Message.Builder(1L)
            .field12("field12")
            .build();

        var message4 = new Message.Builder(2L)
            .field12("field12")
            .field11("field11")
            .build();

        var result1 = complexProcessor.handle(message1);
        System.out.println("result1:" + result1);

        var result2 = complexProcessor.handle(message2);
        System.out.println("result2:" + result2);

        var result3 = complexProcessor.handle(message3);
        System.out.println("result3:" + result3);

        var result4 = complexProcessor.handle(message4);
        System.out.println("result4:" + result4);

        complexProcessor.removeListener(historyListener);
    }
}
