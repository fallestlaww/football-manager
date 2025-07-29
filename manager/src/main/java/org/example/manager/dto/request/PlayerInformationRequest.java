package org.example.manager.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Data Transfer Object for player information request
 * Used for creating or updating player information with validation constraints
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerInformationRequest {
    /**
     * Player's first name with validation pattern
     * Must start with capital letter and follow specific naming conventions
     */
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*(?:\\s[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*)*$", message = "First name must start with capital letters")
    private String firstName;

    /**
     * Player's last name with validation pattern
     * Must start with capital letter and follow specific naming conventions
     */
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*(?:\\s[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*)*$", message = "Last name must start with capital letters")
    private String lastName;

    /**
     * Player's age with validation constraints
     * Must be between 16 and 45 years inclusive
     */
    @Min(value = 16, message = "Player's age must be higher or equals 16")
    @Max(value = 45, message = "Player's age must be lower or equals 45")
    private Integer age;

    /**
     * Player's months of experience with validation constraints
     * Must be between 0 and 468 months inclusive
     */
    @Min(value = 0, message = "Player's months of experience must be higher or equals 0")
    @Max(value = 468, message = "Player's months of experience must be lower or equals 468")
    private Integer monthsOfExperience;

    /**
     * Team ID associated with the player
     * Must be a positive number
     */
    @Positive(message = "Team ID must be positive")
    private Long teamId;
}
