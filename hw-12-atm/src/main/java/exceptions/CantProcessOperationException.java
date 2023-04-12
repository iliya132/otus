package exceptions;

public class CantProcessOperationException extends AtmException {
    public static final String CANT_PROCESS_OPERATION_MESSAGE = "Извините, запрошенная вами операция не может быть " +
            "совершена. Попробуйте ввести другую сумму";

    public CantProcessOperationException() {
        super(CANT_PROCESS_OPERATION_MESSAGE);
    }
}
