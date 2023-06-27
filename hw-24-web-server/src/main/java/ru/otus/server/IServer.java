package ru.otus.server;

public interface IServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
