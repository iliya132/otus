package ru.otus.appcontainer;

public class ComponentNotRegisteredException extends RuntimeException{
    public ComponentNotRegisteredException(String message) {
        super(message);
    }
}
