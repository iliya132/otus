package model;

import exceptions.NotEnoughMoneyException;
import model.interfaces.Cell;

public class AtmCell implements Cell {
    private int currentAmount = 0;
    private final Nominal nominal;

    public AtmCell(Nominal nominal, int initialAmount) {
        this.currentAmount = initialAmount;
        this.nominal = nominal;
    }

    public AtmCell(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
        return this.nominal;
    }

    @Override
    public void put(int amount) {
        currentAmount += amount;
    }

    @Override
    public void take(int amount) throws NotEnoughMoneyException {
        if (currentAmount < amount) {
            throw new NotEnoughMoneyException();
        } else {
            currentAmount -= amount;
        }
    }

    public int getTotalAmount() {
        return currentAmount * nominal.getAmount();
    }
}
