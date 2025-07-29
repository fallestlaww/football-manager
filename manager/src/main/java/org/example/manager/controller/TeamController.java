package org.example.manager.controller;

import jakarta.validation.Valid;
import org.example.manager.dto.request.TeamInformationRequest;
import org.example.manager.dto.response.TeamInformationResponse;
import org.example.manager.model.Team;
import org.example.manager.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing teams
 * Provides API for creating, retrieving, updating and deleting team information
 */
@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    /**
     * Constructor for TeamController
     * @param teamService service for team operations
     */
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Get team by ID
     * @param id team identifier
     * @return team information response with HTTP status FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> readTeam(@PathVariable Long id) {
        logger.info("Fetching team with ID: {}", id);
        Team team = teamService.readTeam(id);
        logger.debug("Successfully fetched team: {}", team);
        return ResponseEntity.status(HttpStatus.FOUND).body(new TeamInformationResponse(team));
    }

    /**
     * Create a new team
     * @param request team information request with validation
     * @return created team information response with HTTP status CREATED
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody @Valid TeamInformationRequest request) {
        logger.info("Creating new team with request: {}", request);
        Team team = teamService.createTeam(request);
        logger.info("Successfully created team with ID: {}", team.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TeamInformationResponse(team));
    }

    /**
     * Update existing team information
     * @param id team identifier
     * @param request team information request with validation
     * @return updated team information response with HTTP status OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @RequestBody @Valid TeamInformationRequest request) {
        logger.info("Updating team with ID: {} using request: {}", id, request);
        Team team = teamService.updateTeam(id, request);
        logger.info("Successfully updated team with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(new TeamInformationResponse(team));
    }

    /**
     * Delete team by ID
     * @param id team identifier
     * @return HTTP status OK with no body
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        logger.info("Deleting team with ID: {}", id);
        teamService.deleteTeam(id);
        logger.info("Successfully deleted team with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Get list of all teams
     * @return list of team information responses with HTTP status OK
     */
    @GetMapping("/list")
    public ResponseEntity<?> listTeams() {
        logger.info("Fetching list of all teams");
        List<TeamInformationResponse> teams = teamService.getAllTeams().stream()
                .map(TeamInformationResponse::new)
                .toList();
        logger.debug("Successfully fetched {} teams", teams.size());
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }
}
