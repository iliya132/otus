package services.interfaces;

import exceptions.NotAuthorizedException;
import model.Cash;

public interface AtmService {
    void authorize(String cardNumber);

    Cash withdraw(int amount) throws NotAuthorizedException;

    void deposit(Cash banknotes) throws NotAuthorizedException;
}
