package ru.otus.appcontainer;

import java.lang.reflect.Method;

public record Component(int order, Method method, String name) {
}
