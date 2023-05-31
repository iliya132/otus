package ru.otus.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

public class ProcessorSwapTest {
    private ProcessorSwap processor;
    private Message message;
    private static final String FIELD_11 = "field_11";
    private static final String FIELD_12 = "field_12";
    @BeforeEach
    public void setUp() {
        message = new Message.Builder(0)
                .field11(FIELD_11)
                .field12(FIELD_12)
                .build();
        processor = new ProcessorSwap();
    }

    @Test
    public void itShouldSwapValues() {
        Assertions.assertThat(message.getField11()).isEqualTo(FIELD_11);
        Assertions.assertThat(message.getField12()).isEqualTo(FIELD_12);
        var newMessage = processor.process(message);
        Assertions.assertThat(newMessage.getField11()).isEqualTo(FIELD_12);
        Assertions.assertThat(newMessage.getField12()).isEqualTo(FIELD_11);
    }
}
