package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MyTestFramework {
    public static void run(String className) throws ClassNotFoundException {
        printStats(withStats(
                runTests(Class.forName(className))
        ));
    }

    private static List<Method> findMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(annotationClass))
                .toList();
    }

    private static Map<Boolean, Integer> withStats(List<Supplier<Boolean>> resultSupplier) {
        var result = new HashMap<Boolean, Integer>();
        resultSupplier.forEach(it -> {
            result.merge(it.get(), 1, Integer::sum);
        });
        return result;
    }

    private static List<Supplier<Boolean>> runTests(Class<?> clazz) {
        var testMethods = findMethodsAnnotatedWith(clazz, Test.class);
        var beforeMethods = findMethodsAnnotatedWith(clazz, Before.class);
        var afterMethods = findMethodsAnnotatedWith(clazz, After.class);
        return testMethods
                .stream()
                .map(it -> (Supplier<Boolean>) () -> runSingle(clazz, it, beforeMethods, afterMethods))
                .toList();
    }

    private static boolean runSingle(Class<?> clazz,
                                     Method testToRun,
                                     List<Method> beforeMethods,
                                     List<Method> afterMethods) {
        try {
            Object testClass = clazz.getConstructor().newInstance();
            return executeTest(testToRun, beforeMethods, afterMethods, testClass);
        } catch (Exception e) {
            System.out.println("Failed to run test");
            return false;
        }
    }

    private static boolean executeTest(Method testToRun,
                                       List<Method> beforeMethods,
                                       List<Method> afterMethods,
                                       Object testClass) throws IllegalAccessException, InvocationTargetException {
        try {
            runAllMethods(beforeMethods, testClass);
            testToRun.invoke(testClass);
        } catch (Exception e) {
            return false;
        } finally {
            runAllMethods(afterMethods, testClass);
        }
        return true;
    }


    private static void runAllMethods(List<Method> methodsToRun, Object instance)
            throws IllegalAccessException, InvocationTargetException {
        for (var before : methodsToRun) {
            before.invoke(instance);
        }
    }

    private static void printStats(Map<Boolean, Integer> result) {
        System.out.println("Tests completed. Results:");
        System.out.printf("Total tests: %d%n", safeSum(result.get(true), result.get(false)));
        System.out.printf("Passed: %d%n", defaultIfNull(result.get(true)));
        System.out.printf("Failed: %d%n", defaultIfNull(result.get(false)));
    }

    private static Integer safeSum(Integer left, Integer right) {
        return defaultIfNull(left) + defaultIfNull(right);
    }

    private static Integer defaultIfNull(Integer integer) {
        return defaultIfNull(integer, 0);
    }

    private static Integer defaultIfNull(Integer integer, int def) {
        return integer == null ? def : integer;
    }
}
