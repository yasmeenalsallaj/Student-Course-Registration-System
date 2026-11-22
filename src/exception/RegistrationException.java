package exception;

/** Thrown when an operation breaks a rule of the system (duplicate ID, full course, ...). */
public class RegistrationException extends Exception {

    private static final long serialVersionUID = 1L;

    public RegistrationException(String message) {
        super(message);
    }
}
