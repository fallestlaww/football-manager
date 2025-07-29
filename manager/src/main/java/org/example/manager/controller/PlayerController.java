package org.example.manager.controller;

import jakarta.validation.Valid;
import org.example.manager.dto.request.PlayerInformationRequest;
import org.example.manager.dto.response.PlayerInformationResponse;
import org.example.manager.model.Player;
import org.example.manager.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing players
 * Provides API for creating, retrieving, updating and deleting player information
 */
@RestController
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    /**
     * Constructor for PlayerController
     * @param playerService service for player operations
     */
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Get player by ID
     * @param id player identifier
     * @return player information response with HTTP status FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> readPlayer(@PathVariable Long id) {
        logger.info("Fetching player with ID: {}", id);
        Player player = playerService.readPlayer(id);
        logger.debug("Successfully fetched player: {}", player);
        return ResponseEntity.status(HttpStatus.FOUND).body(new PlayerInformationResponse(player));
    }

    /**
     * Create a new player
     * @param request player information request with validation
     * @return created player information response with HTTP status CREATED
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestBody @Valid PlayerInformationRequest request) {
        logger.info("Creating new player with request: {}", request);
        Player player = playerService.createPlayer(request);
        logger.info("Successfully created player with ID: {}", player.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PlayerInformationResponse(player));
    }

    /**
     * Update existing player information
     * @param id player identifier
     * @param request player information request with validation
     * @return updated player information response with HTTP status OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @RequestBody @Valid PlayerInformationRequest request) {
        logger.info("Updating player with ID: {} using request: {}", id, request);
        Player player = playerService.updatePlayer(id, request);
        logger.info("Successfully updated player with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(new PlayerInformationResponse(player));
    }

    /**
     * Delete player by ID
     * @param id player identifier
     * @return HTTP status OK with no body
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        logger.info("Deleting player with ID: {}", id);
        playerService.deletePlayer(id);
        logger.info("Successfully deleted player with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Get list of all players
     * @return list of player information responses with HTTP status OK
     */
    @GetMapping("/list")
    public ResponseEntity<?> listPlayers() {
        logger.info("Fetching list of all players");
        List<PlayerInformationResponse> players = playerService.getAllPlayers().stream()
                .map(PlayerInformationResponse::new)
                .toList();
        logger.debug("Successfully fetched {} players", players.size());
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }
}
