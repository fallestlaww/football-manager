package org.example.manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.manager.dto.request.PlayerInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Player;
import org.example.manager.repository.PlayerRepository;
import org.example.manager.repository.TeamRepository;
import org.example.manager.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of PlayerService interface
 * Provides business logic for player management operations
 */
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final static int MIN_AGE = 6; //minimal age in football academys
    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    /**
     * Constructor for PlayerServiceImpl
     * @param playerRepository repository for player data access
     * @param teamRepository repository for team data access
     */
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Creates a new player based on the provided request
     * @param request the player information request containing player details
     * @return the created Player entity
     * @throws NullableRequestException if the request is null
     * @throws IllegalArgumentException if months of experience exceed maximum possible
     * @throws EntityNotFoundException if the team with the provided ID is not found
     */
    @Override
    public Player createPlayer(PlayerInformationRequest request) {
        logger.info("Creating player with request: {}", request);
        if(request == null) {
            logger.error("Request cannot be null");
            throw new NullableRequestException("Request cannot be null");
        }

        Player player = new Player();
        //to avoid cases where experience is greater than the player's age
        int maxPossibleExperience = (request.getAge() - MIN_AGE) * 12;
        if(request.getMonthsOfExperience() > maxPossibleExperience) {
            logger.error("Months of experience cannot be greater than max experience");
            throw new IllegalArgumentException("Months of experience cannot be greater than max experience");
        }
        player.setFirstName(request.getFirstName());
        player.setLastName(request.getLastName());
        player.setAge(request.getAge());
        player.setMonthsOfExperience(request.getMonthsOfExperience());
        player.setTeam(teamRepository.findById(request.getTeamId()).orElseThrow(() -> {
            logger.error("Team with this id not found: {}", request.getTeamId());
            return new EntityNotFoundException("Team with this id not found");
        }));
        Player savedPlayer = playerRepository.save(player);
        logger.info("Successfully created player with ID: {}", savedPlayer.getId());
        return savedPlayer;
    }

    /**
     * Updates an existing player with the provided information
     * @param id the ID of the player to update
     * @param request the player information request containing updated details
     * @return the updated Player entity
     * @throws NullableRequestException if the request is null
     * @throws EntityNotFoundException if the player or team with the provided ID is not found
     * @throws IllegalArgumentException if months of experience exceed maximum possible
     */
    @Override
    public Player updatePlayer(Long id, PlayerInformationRequest request) {
        logger.info("Updating player with ID: {} using request: {}", id, request);
        if(request == null) {
            logger.error("Request cannot be null");
            throw new NullableRequestException("Request cannot be null");
        }

        Player playerForUpdate = readPlayer(id);

        if(playerForUpdate == null) {
            logger.error("Player not found with ID: {}", id);
            throw new EntityNotFoundException("Player not found");
        }
        if(request.getFirstName() != null) playerForUpdate.setFirstName(request.getFirstName());
        if(request.getLastName() != null) playerForUpdate.setLastName(request.getLastName());
        if(request.getAge() != null) playerForUpdate.setAge(request.getAge());
        if(request.getMonthsOfExperience() != null) {
            int currentAge = request.getAge() != null ? request.getAge() : playerForUpdate.getAge();
            //to avoid cases where experience is greater than the player's age
            int maxPossibleExperience = (currentAge - MIN_AGE) * 12;

            if(request.getMonthsOfExperience() > maxPossibleExperience) {
                logger.error("Months of experience cannot be greater than max experience");
                throw new IllegalArgumentException("Months of experience cannot be greater than max experience");
            }
            playerForUpdate.setMonthsOfExperience(request.getMonthsOfExperience());
        }
        if(request.getTeamId() !=null) {
            playerForUpdate.setTeam(teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> {
                        logger.error("Team with this id not found: {}", request.getTeamId());
                        return new EntityNotFoundException("Team with this id not found");
                    }));
        }
        Player updatedPlayer = playerRepository.save(playerForUpdate);
        logger.info("Successfully updated player with ID: {}", updatedPlayer.getId());
        return updatedPlayer;
    }

    /**
     * Retrieves a player by ID
     * @param playerId the ID of the player to retrieve
     * @return the Player entity with the specified ID
     * @throws EntityNotFoundException if the player with the provided ID is not found
     */
    @Override
    public Player readPlayer(Long playerId) {
        logger.info("Fetching player with ID: {}", playerId);
        Player player = playerRepository.findById(playerId).orElseThrow(() -> {
            logger.error("Player with id {} not found", playerId);
            return new EntityNotFoundException("Player with id " + playerId + " not found");
        });
        logger.debug("Successfully fetched player: {}", player);
        return player;
    }

    /**
     * Deletes a player by ID
     * @param playerId the ID of the player to delete
     * @throws EntityNotFoundException if the player with the provided ID is not found
     */
    @Override
    public void deletePlayer(Long playerId) {
        logger.info("Deleting player with ID: {}", playerId);
        Player player = readPlayer(playerId);
        playerRepository.delete(player);
        logger.info("Successfully deleted player with ID: {}", playerId);
    }

    /**
     * Retrieves all players
     * @return a list of all Player entities
     */
    @Override
    public List<Player> getAllPlayers() {
        logger.info("Fetching all players");
        List<Player> players = playerRepository.findAllPlayers();
        logger.debug("Successfully fetched {} players", players.size());
        return players;
    }
}
