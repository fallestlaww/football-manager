package org.example.manager.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object for team information request
 * Used for creating or updating team information with validation constraints
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamInformationRequest {
    /**
     * Team's name with validation pattern
     * Must start with capital letter and contain only letters and hyphens
     */
    @Pattern(regexp = "^[A-Z][a-zA-Z-]*(?:\\s+[A-Z][a-zA-Z-]*)*$", message = "Team's name must start with capital letters and contain only letters")
    private String name;

    /**
     * Country's name with validation pattern
     * Must start with capital letter and contain only letters and hyphens
     */
    @Pattern(regexp = "^[A-Z][a-zA-Z-]*(?:\\s+[A-Z][a-zA-Z-]*)*$", message = "Country's name must start with capital letters and contain only letters")
    private String country;

    /**
     * Team's balance with validation constraint
     * Must be higher than or equal to 0
     */
    @Min(value = 0, message = "Team's balance must be higher than 0")
    private BigDecimal balance;

    /**
     * Team's commission rate with validation constraints
     * Must be between 0 and 10 inclusive
     */
    @Min(value = 0, message = "Team's commission rate must be higher or equals 0")
    @Max(value = 10, message = "Team's commission rate must be lower or equals 10")
    private Double commissionRate;
}
