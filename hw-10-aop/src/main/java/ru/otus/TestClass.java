package ru.otus;

import ru.otus.annotations.Log;

public class TestClass implements TestInterface {

    @Override
    public int calculate(int x, int y) {
        return x + y;
    }

    @Override
    @Log
    public int calculate(int x, int y, int z) {
        return x + y + z;
    }

    @Override
    public int min(int x, int y) {
        return Math.min(x, y);
    }

    @Override
    @Log
    public int max(int x, int y) {
        return Math.max(x, y);
    }
}
