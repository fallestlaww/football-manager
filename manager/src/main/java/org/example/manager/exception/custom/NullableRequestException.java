package org.example.manager.exception.custom;

/**
 * Custom exception thrown when a request object or required field is null
 * This is a runtime exception that indicates a validation error
 */
public class NullableRequestException extends RuntimeException {
    /**
     * Constructs a new NullableRequestException with the specified detail message
     * @param message the detail message explaining the reason for the exception
     */
    public NullableRequestException(String message) {
        super(message);
    }
}
