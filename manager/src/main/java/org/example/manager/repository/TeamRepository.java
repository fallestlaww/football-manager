package org.example.manager.repository;

import org.example.manager.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for Team entity
 * Provides CRUD operations and custom queries for Team entities
 */
public interface TeamRepository extends JpaRepository<Team, Long> {
    /**
     * Find a team by its name
     * @param name the name of the team to find
     * @return the Team entity with the specified name, or null if not found
     */
    Team findByName(String name);

    /**
     * Check if a team with the given name exists, excluding the team with the specified ID
     * @param name the name to check for uniqueness
     * @param id the ID to exclude from the check
     * @return true if a team with the given name exists (excluding the specified ID), false otherwise
     */
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * Custom query to find all teams
     * @return List of all teams in the database
     */
    @Query("SELECT t FROM Team t")
    List<Team> findAllTeams();
}
