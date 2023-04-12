package ru.otus;

public class Main {
    public static void main(String[] args) {
        var testEntity = Ioc.wrap(TestInterface.class, TestClass.class);
        testEntity.calculate(1, 2);
        testEntity.calculate(1, 2, 3);
        testEntity.min(1, 2);
        testEntity.max(1, 2);

        testEntity.calculate(1, 2);
        testEntity.calculate(1, 2, 3);
        testEntity.min(1, 2);
        testEntity.max(1, 2);

        var testEntity2 = Ioc.wrap(TestInterface.class, TestClass.class);
        testEntity2.calculate(1,2,3);
    }
}
