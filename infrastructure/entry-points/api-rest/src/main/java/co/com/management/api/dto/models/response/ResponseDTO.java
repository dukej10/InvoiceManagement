package co.com.management.api.dto.models.response;

import co.com.management.model.invoice.Invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ResponseDTO<T>(
        T data,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {


    public ResponseDTO(T data, String message, int statusCode) {
        this(data, message, statusCode, LocalDateTime.now());
    }
}
