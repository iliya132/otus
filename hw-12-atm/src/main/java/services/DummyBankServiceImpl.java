package services;

import exceptions.NotEnoughMoneyException;
import services.interfaces.BankService;

public class DummyBankServiceImpl implements BankService {
    @Override
    public void withdraw(int amount) throws NotEnoughMoneyException {
        //Not implemented
        //Представим что на банковском счету всегда есть деньги
    }

    @Override
    public void refund(int amount) {
        //Not implemented
    }

    @Override
    public void deposit(int amount) {
        //Not implemented
    }
}
