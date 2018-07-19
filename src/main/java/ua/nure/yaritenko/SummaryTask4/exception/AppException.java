package ua.nure.yaritenko.SummaryTask4.exception;
/**
 * An exception that provides information on an application error.
 *
 * @author V.Yaritenko
 *
 */
public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }
}
