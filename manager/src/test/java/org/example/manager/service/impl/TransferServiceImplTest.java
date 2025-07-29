package org.example.manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.manager.exception.custom.LowBalanceException;
import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.example.manager.repository.PlayerRepository;
import org.example.manager.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void transferPlayer_success() {
        Player player = createPlayer(1L, 25, 60, 1L, "1000000", 5.0);
        Team newTeam = createTeam(2L, "10000000", 4.0);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        when(teamRepository.save(any(Team.class))).thenReturn(player.getTeam());

        transferService.transferPlayer(1L, 2L);

        assertEquals(newTeam, player.getTeam());
        verify(playerRepository).save(player);
        verify(teamRepository, times(2)).save(any(Team.class));
    }

    @Test
    void transferPlayer_playerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                transferService.transferPlayer(1L, 2L));
        assertTrue(ex.getMessage().contains("Player with this id not found"));
    }

    @Test
    void transferPlayer_teamNotFound() {
        Player player = createPlayer(1L, 25, 60, 1L, "1000000", 5.0);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                transferService.transferPlayer(1L, 2L));
        assertTrue(ex.getMessage().contains("Team with this id not found"));
    }

    @Test
    void transferPlayer_lowBalance() {
        Player player = createPlayer(1L, 25, 60, 1L, "1000000", 5.0);
        Team newTeam = createTeam(2L, "1", 4.0);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        LowBalanceException ex = assertThrows(LowBalanceException.class, () ->
                transferService.transferPlayer(1L, 2L));
        assertTrue(ex.getMessage().contains("not have enough money"));
    }

    private Player createPlayer(Long id, int age, int monthsOfExperience, Long teamId, String balance, double commissionRate) {
        Player player = new Player();
        player.setId(id);
        player.setAge(age);
        player.setMonthsOfExperience(monthsOfExperience);
        Team team = createTeam(teamId, balance, commissionRate);
        player.setTeam(team);
        return player;
    }

    private Team createTeam(Long id, String balance, double commissionRate) {
        Team team = new Team();
        team.setId(id);
        team.setBalance(new BigDecimal(balance));
        team.setCommissionRate(commissionRate);
        return team;
    }
} 