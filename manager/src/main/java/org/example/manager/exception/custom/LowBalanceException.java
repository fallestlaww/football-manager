
package org.example.manager.exception.custom;

/**
 * Custom exception thrown when a team's balance is insufficient for a transaction
 * This is a runtime exception that indicates a business logic error
 */
public class LowBalanceException extends RuntimeException {
    /**
     * Constructs a new LowBalanceException with the specified detail message
     * @param message the detail message explaining the reason for the exception
     */
    public LowBalanceException(String message) {
        super(message);
    }
}