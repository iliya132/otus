package services;

import services.interfaces.UiOutputService;

public class ConsoleUiOutputService implements UiOutputService {
    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
