package org.example.manager.service;

/**
 * Service interface for player transfer operations
 * Provides method for transferring players between teams
 */
public interface TransferService {
    /**
     * Transfers a player from their current team to a new team
     * @param playerId the ID of the player to transfer
     * @param newTeamId the ID of the team to transfer the player to
     */
    void transferPlayer(Long playerId, Long newTeamId);
}
