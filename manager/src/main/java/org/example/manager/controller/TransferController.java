package org.example.manager.controller;

import org.example.manager.dto.response.PlayerInformationResponse;
import org.example.manager.model.Player;
import org.example.manager.service.PlayerService;
import org.example.manager.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing player transfers between teams
 * Provides API for transferring players from one team to another
 */
@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;
    private final PlayerService playerService;
    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    /**
     * Constructor for TransferController
     * @param transferService service for handling player transfers
     * @param playerService service for player operations
     */
    public TransferController(TransferService transferService, PlayerService playerService) {
        this.transferService = transferService;
        this.playerService = playerService;
    }

    /**
     * Transfer a player to a new team
     * @param playerId identifier of the player to transfer
     * @param newTeamId identifier of the team to transfer the player to
     * @return updated player information response with HTTP status OK
     */
    @PostMapping("{playerId}/to/{newTeamId}")
    public ResponseEntity<?> transferPlayer(@PathVariable Long playerId, @PathVariable Long newTeamId) {
        logger.info("Transferring player with ID: {} to team with ID: {}", playerId, newTeamId);
        transferService.transferPlayer(playerId, newTeamId);
        Player player = playerService.readPlayer(playerId);
        logger.info("Successfully transferred player with ID: {} to team: {}", playerId, player.getTeam().getName());
        return ResponseEntity.ok().body(new PlayerInformationResponse(player));
    }
}
