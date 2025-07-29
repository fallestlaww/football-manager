package org.example.manager.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.manager.dto.request.TeamInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Team;
import org.example.manager.repository.TeamRepository;
import org.example.manager.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of TeamService interface
 * Provides business logic for team management operations
 */
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

    /**
     * Constructor for TeamServiceImpl
     * @param teamRepository repository for team data access
     */
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Creates a new team based on the provided request
     * @param request the team information request containing team details
     * @return the created Team entity
     * @throws NullableRequestException if the request is null
     * @throws EntityExistsException if a team with the same name already exists
     */
    @Transactional
    @Override
    public Team createTeam(TeamInformationRequest request) {
        logger.info("Creating team with request: {}", request);
        if(request == null) {
            logger.error("Request cannot be null");
            throw new NullableRequestException("Request cannot be null");
        }

        Team existingTeam = teamRepository.findByName(request.getName());

        if(existingTeam != null) {
            logger.error("Team already exists with name: {}", request.getName());
            throw new EntityExistsException("Team already exists");
        }

        Team team = new Team();
        team.setName(request.getName());
        team.setCountry(request.getCountry());
        team.setBalance(request.getBalance());
        team.setCommissionRate(request.getCommissionRate());
        Team savedTeam = teamRepository.save(team);
        logger.info("Successfully created team with ID: {}", savedTeam.getId());
        return savedTeam;
    }

    /**
     * Updates an existing team with the provided information
     * @param id the ID of the team to update
     * @param request the team information request containing updated details
     * @return the updated Team entity
     * @throws NullableRequestException if the request is null
     * @throws EntityNotFoundException if the team with the provided ID is not found
     * @throws EntityExistsException if a team with the same name already exists
     */
    @Transactional
    @Override
    public Team updateTeam(Long id, TeamInformationRequest request) {
        logger.info("Updating team with ID: {} using request: {}", id, request);
        if(request == null) {
            logger.error("Request cannot be null");
            throw new NullableRequestException("Request cannot be null");
        }
        Team teamForUpdate = readTeam(id);

        if(request.getName() != null && !request.getName().equals(teamForUpdate.getName())) {
            if(teamRepository.existsByNameAndIdNot(request.getName(), id)) {
                logger.error("Team with this name already exists: {}", request.getName());
                throw new EntityExistsException("Team with this name already exists");
            }
            teamForUpdate.setName(request.getName());
        }
        if(request.getCountry() != null) teamForUpdate.setCountry(request.getCountry());
        if(request.getBalance() != null) teamForUpdate.setBalance(request.getBalance());
        if(request.getCommissionRate() != null) teamForUpdate.setCommissionRate(request.getCommissionRate());
        Team updatedTeam = teamRepository.save(teamForUpdate);
        logger.info("Successfully updated team with ID: {}", updatedTeam.getId());
        return updatedTeam;
    }

    /**
     * Retrieves a team by ID
     * @param id the ID of the team to retrieve
     * @return the Team entity with the specified ID
     * @throws EntityNotFoundException if the team with the provided ID is not found
     */
    @Override
    public Team readTeam(Long id) {
        logger.info("Fetching team with ID: {}", id);
        Team team = teamRepository.findById(id).orElseThrow(() -> {
            logger.error("Team with id: {} not found", id);
            return new EntityNotFoundException("Team with id: " + id + " not found");
        });
        logger.debug("Successfully fetched team: {}", team);
        return team;
    }
    /**
     * Deletes a team by ID
     * @param id the ID of the team to delete
     * @throws EntityNotFoundException if the team with the provided ID is not found
     */
    @Override
    public void deleteTeam(Long id) {
        logger.info("Deleting team with ID: {}", id);
        Team team = readTeam(id);
        teamRepository.delete(team);
        logger.info("Successfully deleted team with ID: {}", id);
    }

    /**
     * Retrieves all teams
     * @return a list of all Team entities
     */
    @Override
    public List<Team> getAllTeams() {
        logger.info("Fetching all teams");
        List<Team> teams = teamRepository.findAllTeams();
        logger.debug("Successfully fetched {} teams", teams.size());
        return teams;
    }
}
