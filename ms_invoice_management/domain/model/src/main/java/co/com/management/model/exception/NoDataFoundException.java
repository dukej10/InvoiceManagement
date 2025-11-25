package co.com.management.model.exception;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {
        super("No hay información relacionada");
    }
}

