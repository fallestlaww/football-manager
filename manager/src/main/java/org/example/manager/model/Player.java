
package org.example.manager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing a player in the football management system
 * Maps to the 'player' table in the database
 */
@Entity
@Table(name = "player")
@Getter
@Setter
@ToString
public class Player {
    /**
     * Unique identifier for the player
     * Generated automatically by the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Player's first name with validation constraints
     * Must not be blank and must follow specific naming conventions
     */
    @NotBlank(message = "Player's first name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*(?:\\s[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*)*$", message = "First name must start with capital letters")
    private String firstName;

    /**
     * Player's last name with validation constraints
     * Must not be blank and must follow specific naming conventions
     */
    @NotBlank(message = "Player's last name is required")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*(?:\\s[A-Z][a-zA-Z]*(?:-[A-Z][a-zA-Z]*)*)*$", message = "Last name must start with capital letters")
    private String lastName;

    /**
     * Player's age in years with validation constraints
     * Must be between 16 and 45 years inclusive
     */
    // 16 - min age for professional career's starting, 45 - suitable age for ending of the career
    @Min(value = 16, message = "Player's age must be higher or equals 16")
    @Max(value = 45, message = "Player's age must be lower or equals 45")
    private Integer age;

    /**
     * Player's experience in months with validation constraints
     * Must be between 0 and 468 months inclusive
     */
    // by formula: max experience = (years - 6) * 12 = (45 - 6) * 12 = 468 - that's max possible experience
    @Min(value = 0, message = "Player's months of experience must be higher or equals 0")
    @Max(value = 468, message = "Player's months of experience must be lower or equals 468")
    private Integer monthsOfExperience;

    /**
     * The team that the player belongs to
     * Many players can belong to one team (Many-to-One relationship)
     */
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @ToString.Exclude
    private Team team;
}