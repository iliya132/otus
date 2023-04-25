package model.interfaces;

import exceptions.NotEnoughMoneyException;

public interface Cell {
    void put(int amount);

    void take(int amount) throws NotEnoughMoneyException;
}
