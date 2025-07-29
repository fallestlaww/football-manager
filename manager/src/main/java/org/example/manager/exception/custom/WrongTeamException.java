package org.example.manager.exception.custom;

/**
 * Custom exception thrown when choose for transfer same team in which player already is
 * This is a runtime exception that indicates a business logic error
 */
public class WrongTeamException extends RuntimeException {
    /**
     * Constructs a new WrongTeamException with the specified detail message
     * @param message the detail message explaining the reason for the exception
     */
    public WrongTeamException(String message) {
        super(message);
    }
}
