package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import org.assertj.core.api.Assertions;

public class TestSuite {

    @Before
    public void setUp() {
        System.out.println("preparing");
    }

    @After
    public void tearDown() {
        System.out.println("Shutting down");
    }

    @Test
    public void testIntegersSum() {
        Assertions.assertThat(true).isTrue();
    }

    @Test
    public void thisOneIsFailed() {
        Assertions.assertThat(false).isTrue();
    }
}
