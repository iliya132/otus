package services.interfaces;

import exceptions.CantProcessOperationException;
import exceptions.NotEnoughMoneyException;
import model.Cash;

public interface AtmCellsService {
    Cash withdraw(int amount) throws CantProcessOperationException, NotEnoughMoneyException;

    void deposit(Cash banknotes);
}
