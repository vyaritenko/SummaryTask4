package ua.nure.yaritenko.SummaryTask4.exception;
/**
 * An exception that provides information on a database access error.
 *
 * @author V.Yaritenko
 *
 */
public class DBException extends AppException {

    public DBException() {
        super();
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}