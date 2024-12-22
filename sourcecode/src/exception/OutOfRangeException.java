package exception;

public class OutOfRangeException extends InvalidInputException {
    private static final long serialVersionUID = 1L;

    public OutOfRangeException(String message) {
        super(message);
    }
}
