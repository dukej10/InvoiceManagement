package co.com.management.model.exception;

public class DataFoundException extends RuntimeException {

    public DataFoundException(String nameClass) {
        super( nameClass + " ya existe");
    }
}

