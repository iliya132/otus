package ru.otus.appcontainer;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Exception ex) {
        super(ex);
    }
}
