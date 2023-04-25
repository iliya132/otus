package services.interfaces;

import exceptions.NotEnoughMoneyException;

public interface BankService {
    void withdraw(int amount) throws NotEnoughMoneyException;
    void refund(int amount);
    void deposit(int amount);
}
