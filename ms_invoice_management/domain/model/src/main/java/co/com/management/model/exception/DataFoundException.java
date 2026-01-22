package co.com.management.model.exception;

public class DataFoundException extends RuntimeException {

    public DataFoundException(String text) {
        super( text + " ya se encuentra registrado");
    }
}

