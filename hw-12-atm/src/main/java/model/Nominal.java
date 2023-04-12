package model;

public enum Nominal {
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    Nominal(int amount) {
        this.amount = amount;
    }

    private final int amount;

    public int getAmount() {
        return this.amount;
    }
}
