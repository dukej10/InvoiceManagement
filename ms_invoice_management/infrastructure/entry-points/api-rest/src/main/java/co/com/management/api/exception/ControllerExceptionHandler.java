package co.com.management.api.exception;

import co.com.management.api.Utility;
import co.com.management.api.dto.models.response.ResponseDTO;
import co.com.management.model.exception.DataFoundException;
import co.com.management.model.exception.GeneralException;
import co.com.management.model.exception.NoDataFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                "Validation failed",
                request.getDescription(false),
                fieldErrors
        );

        return new ResponseEntity<>(
                Utility.structureRS(errorResponse, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

   
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleValidationError(ConstraintViolationException exception) {
        List<Map<String, String>> errors = exception.getConstraintViolations()
                .stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString()
                                .substring(violation.getPropertyPath().toString().lastIndexOf('.') + 1),
                        "error", violation.getMessage()))
                .toList();

        return ResponseEntity.badRequest().body(
                Utility.structureRS(Map.of("errors", errors), HttpStatus.BAD_REQUEST.value())
        );
    }

    @ExceptionHandler({NoDataFoundException.class, GeneralException.class, DataFoundException.class})
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleCustomException(
            RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                null
        );

        return new ResponseEntity<>(
                Utility.structureRS(errorResponse,HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorResponse>> handleGlobalException(Exception ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                null
        );

        return new ResponseEntity<>(
                Utility.structureRS(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
