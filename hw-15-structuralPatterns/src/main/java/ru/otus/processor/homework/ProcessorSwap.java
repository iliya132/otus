package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwap  implements Processor {
    @Override
    public Message process(Message message) {
        var field12 = message.getField12();
        return message.toBuilder()
                .field12(message.getField11())
                .field11(field12)
                .build();
    }
}
