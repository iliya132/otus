package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.function.Supplier;

public class ProcessorEvenThrows implements Processor {
    private final Supplier<Integer> secondProvider;

    public ProcessorEvenThrows(Supplier<Integer> secondProvider) {
        this.secondProvider = secondProvider;
    }

    @Override
    public Message process(Message message) {
        if (secondProvider.get() % 2 == 0) {
            throw new RuntimeException("Processed at even second");
        }
        return message;
    }
}
