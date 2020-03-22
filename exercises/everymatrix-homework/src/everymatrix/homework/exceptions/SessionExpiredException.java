package everymatrix.homework.exceptions;

/**
 * thrown when a session used for a request has expired.
 */
public class SessionExpiredException extends Exception {

    public SessionExpiredException(String message) {
        super(message);
    }
}
