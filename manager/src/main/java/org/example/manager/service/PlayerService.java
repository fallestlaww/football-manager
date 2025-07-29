package org.example.manager.service;

import org.example.manager.dto.request.PlayerInformationRequest;
import org.example.manager.model.Player;

import java.util.List;

/**
 * Service interface for player management operations
 * Provides methods for creating, reading, updating, deleting and listing players
 */
public interface PlayerService {
    /**
     * Creates a new player based on the provided request
     * @param request the player information request containing player details
     * @return the created Player entity
     */
    Player createPlayer(PlayerInformationRequest request);

    /**
     * Updates an existing player with the provided information
     * @param id the ID of the player to update
     * @param request the player information request containing updated details
     * @return the updated Player entity
     */
    Player updatePlayer(Long id, PlayerInformationRequest request);

    /**
     * Retrieves a player by ID
     * @param playerId the ID of the player to retrieve
     * @return the Player entity with the specified ID
     */
    Player readPlayer(Long playerId);

    /**
     * Deletes a player by ID
     * @param playerId the ID of the player to delete
     */
    void deletePlayer(Long playerId);

    /**
     * Retrieves all players
     * @return a list of all Player entities
     */
    List<Player> getAllPlayers();
}
