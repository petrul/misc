package everymatrix.homework.exceptions;

import everymatrix.homework.handlers.Handler;

/**
 * thrown by @{@link Handler#chooseHandler(String)} when
 * the URL does not correspond to any accepted request.
 *
 * It should translate into serving a 404 page.
 */
public class NoHandlerFoundException extends Exception {
    public NoHandlerFoundException(String message) {
        super(message);
    }
}
