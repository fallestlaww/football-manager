package org.example.manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Entity class representing a team in the football management system
 * Maps to the 'team' table in the database
 */
@Entity
@Table(name = "team")
@Getter
@Setter
@ToString
public class Team {
    /**
     * Unique identifier for the team
     * Generated automatically by the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Team's name with validation constraints
     * Must not be blank, must be unique, and must follow specific naming conventions
     */
    @NotBlank(message = "Team's name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z-]*(?:\\s+[A-Z][a-zA-Z-]*)*$", message = "Team's name must start with capital letters and contain only letters")
    @Column(unique = true)
    private String name;

    /**
     * Country where the team is based with validation constraints
     * Must not be blank and must follow specific naming conventions
     */
    @NotBlank(message = "Country's name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z-]*(?:\\s+[A-Z][a-zA-Z-]*)*$", message = "Country's name must start with capital letters and contain only letters")
    private String country;

    /**
     * Team's financial balance with precision and validation constraints
     * Must be higher than or equal to 0.00 with 15 digits precision and 2 decimal places
     */
    @Column(name = "balance", precision = 15, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", message = "Balance must be higher or equals 0")
    private BigDecimal balance;

    /**
     * Team's commission rate (percentage) with validation constraints
     * Must be between 0 and 10 inclusive
     */
    @Min(value = 0, message = "Team's commission rate must be higher or equals 0")
    @Max(value = 10, message = "Team's commission rate must be lower or equals 10")
    private Double commissionRate;
}
