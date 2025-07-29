package org.example.manager.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.manager.exception.custom.LowBalanceException;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.exception.custom.WrongTeamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.postgresql.util.PSQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 * Handles various exceptions and returns appropriate HTTP responses with error details
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles bad request exceptions including validation errors
     * @param ex the exception that was thrown
     * @return ResponseEntity with HTTP status 400 and error details
     */
    @ExceptionHandler({NullableRequestException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class,
    PSQLException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles entity not found exceptions
     * @param ex the EntityNotFoundException that was thrown
     * @return ResponseEntity with HTTP status 404 and error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles entity already exists and wrong team exceptions
     * @param ex the EntityExistsException that was thrown
     * @return ResponseEntity with HTTP status 409 and error details
     */
    @ExceptionHandler({EntityExistsException.class, WrongTeamException.class})
    public ResponseEntity<Object> handleEntityExistsException(Exception ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles low balance exceptions during transfers
     * @param ex the LowBalanceException that was thrown
     * @return ResponseEntity with HTTP status 403 and error details
     */
    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<Object> handleLowBalanceException(LowBalanceException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Builds response for every exception using HTTP status, made to reduce repetition in code
     * @param status HTTP status to be thrown
     * @param message message to return, often is exception message
     * @return {@link ResponseEntity} with a status code and message according to the parameters
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }
}
