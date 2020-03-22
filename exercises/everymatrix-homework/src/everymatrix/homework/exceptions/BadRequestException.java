package everymatrix.homework.exceptions;

/**
 * translates to 400 HTTP code 
 */
public class BadRequestException extends Exception {

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
