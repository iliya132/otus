package ru.otus.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;

public class ProcessorEvenThrowsTest {
    @Test
    public void whenProcessedAtEvenSecondThrowsException() {
        var processor = new ProcessorEvenThrows(() -> 2);
        Assertions.assertThatThrownBy(() -> processor.process(Mockito.mock(Message.class)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void whenProcessedAtOddSecondNotThrowsException() {
        var processor = new ProcessorEvenThrows(() -> 1);
        processor.process(Mockito.mock(Message.class));
    }
}
