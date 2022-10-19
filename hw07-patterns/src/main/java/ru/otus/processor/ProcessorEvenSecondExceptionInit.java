package ru.otus.processor;

import ru.otus.exception.EvenSecondException;
import ru.otus.model.Message;
import ru.otus.util.DateTimeProvider;

public class ProcessorEvenSecondExceptionInit implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondExceptionInit(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int currentSecond = dateTimeProvider.getCurrentSecond();
        if (currentSecond % 2 == 0) {
            String errorMessage = String.format("%d second is even => throw the exception%n", currentSecond);
            System.out.printf(errorMessage);
            throw new EvenSecondException(errorMessage);
        }
        System.out.printf("%s second is uneven => continue to work%n", currentSecond);
        return message;
    }
}
