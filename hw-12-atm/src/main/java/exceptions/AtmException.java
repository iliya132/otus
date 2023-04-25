package exceptions;

public abstract class AtmException extends Exception {
    private final String message;

    protected AtmException(String message) {
        super(message);
        this.message = message;
    }

    public String getUiMessage() {
        return message;
    }
}
