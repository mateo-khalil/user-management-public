package lqqd.asur.exceptions;

public class NullCIException extends NullPointerException {
    private static final String MESSAGE = "CI must not be Null.";

    public NullCIException() {
        super(MESSAGE);
    }
}
