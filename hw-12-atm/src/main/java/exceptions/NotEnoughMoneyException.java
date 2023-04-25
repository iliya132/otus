package exceptions;

public class NotEnoughMoneyException extends AtmException {
    public static final String NOT_ENOUGH_MONEY_MESSAGE = "К сожалению денег на вашем счете не достаточно";

    public NotEnoughMoneyException() {
        super(NOT_ENOUGH_MONEY_MESSAGE);
    }
}
