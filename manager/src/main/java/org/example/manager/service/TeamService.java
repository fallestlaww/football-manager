package org.example.manager.service;

import org.example.manager.dto.request.TeamInformationRequest;
import org.example.manager.model.Team;

import java.util.List;

/**
 * Service interface for team management operations
 * Provides methods for creating, reading, updating, deleting and listing teams
 */
public interface TeamService {
    /**
     * Creates a new team based on the provided request
     * @param request the team information request containing team details
     * @return the created Team entity
     */
    Team createTeam(TeamInformationRequest request);

    /**
     * Updates an existing team with the provided information
     * @param id the ID of the team to update
     * @param request the team information request containing updated details
     * @return the updated Team entity
     */
    Team updateTeam(Long id, TeamInformationRequest request);

    /**
     * Retrieves a team by ID
     * @param id the ID of the team to retrieve
     * @return the Team entity with the specified ID
     */
    Team readTeam(Long id);

    /**
     * Deletes a team by ID
     * @param id the ID of the team to delete
     */
    void deleteTeam(Long id);

    /**
     * Retrieves all teams
     * @return a list of all Team entities
     */
    List<Team> getAllTeams();
}
