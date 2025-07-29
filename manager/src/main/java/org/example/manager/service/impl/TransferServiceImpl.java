package org.example.manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.manager.exception.custom.LowBalanceException;
import org.example.manager.exception.custom.WrongTeamException;
import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.example.manager.repository.PlayerRepository;
import org.example.manager.repository.TeamRepository;
import org.example.manager.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of TransferService interface
 * Provides business logic for player transfer operations between teams
 */
@Service
public class TransferServiceImpl implements TransferService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    /**
     * Constructor for TransferServiceImpl
     * @param playerRepository repository for player data access
     * @param teamRepository repository for team data access
     */
    public TransferServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Transfers a player from their current team to a new team
     * Handles financial transactions including transfer amount and commission
     * @param playerId the ID of the player to transfer
     * @param newTeamId the ID of the team to transfer the player to
     * @throws EntityNotFoundException if the player or team with the provided ID is not found
     * @throws LowBalanceException if the new team does not have sufficient funds for the transfer
     */
    @Transactional
    @Override
    public void transferPlayer(Long playerId, Long newTeamId) {
        logger.info("Transferring player with ID: {} to team with ID: {}", playerId, newTeamId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> {
                    logger.error("Player with this id not found: {}", playerId);
                    return new EntityNotFoundException("Player with this id not found");
                });
        Team newTeam = teamRepository.findById(newTeamId)
                .orElseThrow(() -> {
                    logger.error("Team with this id not found: {}", newTeamId);
                    return new EntityNotFoundException("Team with this id not found");
                });

        if(player.getTeam().getName().equals(newTeam.getName())) {
            logger.error("Player already in the team: {}", newTeam.getName());
            throw new WrongTeamException("Player already in the team");
        }

        BigDecimal transferAmount = calculateTransferAmount(player);
        BigDecimal commission = calculateCommission(transferAmount, player.getTeam());
        BigDecimal fullAmount = transferAmount.add(commission);

        if(newTeam.getBalance().compareTo(fullAmount) < 0) {
            logger.error("New team does not have enough money. Team balance: {}, Required amount: {}",
                    newTeam.getBalance(), fullAmount);
            throw new LowBalanceException("New team does not have enough money");
        }

        player.getTeam().setBalance(player.getTeam().getBalance().add(fullAmount));
        newTeam.setBalance(newTeam.getBalance().subtract(fullAmount));
        player.setTeam(newTeam);
        teamRepository.save(player.getTeam());
        teamRepository.save(newTeam);
        playerRepository.save(player);
        logger.info("Successfully transferred player with ID: {} to team: {}",
                playerId, newTeam.getName());
    }

    /**
     * Calculates the transfer amount for a player based on their experience and age
     * Formula: (months of experience * 100,000) / age
     * @param player the player for which to calculate the transfer amount
     * @return the calculated transfer amount as BigDecimal with 2 decimal places
     */
    private BigDecimal calculateTransferAmount(Player player) {
        logger.debug("Calculating transfer amount for player: {} with {} months experience and age {}",
                player.getFirstName() + " " + player.getLastName(),
                player.getMonthsOfExperience(), player.getAge());
        BigDecimal amount = BigDecimal.valueOf(player.getMonthsOfExperience() * 100_000L)
                .divide(BigDecimal.valueOf(player.getAge()), 2, RoundingMode.HALF_UP);
        logger.debug("Transfer amount calculated: {}", amount);
        return amount;
    }

    /**
     * Calculates the commission amount for a transfer based on the transfer amount and team commission rate
     * Formula: (transfer amount * team commission rate) / 100
     * @param transferAmount the base transfer amount
     * @param team the team from which the player is being transferred (source team)
     * @return the calculated commission amount as BigDecimal with 2 decimal places
     */
    private BigDecimal calculateCommission(BigDecimal transferAmount, Team team) {
        logger.debug("Calculating commission for transfer amount: {} and team commission rate: {}",
                transferAmount, team.getCommissionRate());
        BigDecimal commission = transferAmount.multiply(BigDecimal.valueOf(team.getCommissionRate()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        logger.debug("Commission calculated: {}", commission);
        return commission;
    }
}
