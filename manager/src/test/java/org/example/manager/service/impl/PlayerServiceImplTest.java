package org.example.manager.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.manager.dto.request.PlayerInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.example.manager.repository.PlayerRepository;
import org.example.manager.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void createPlayer_success() {
        PlayerInformationRequest request = createRequest("Leo", "Messi", 38, 380, 1L);
        Team team = new Team();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        Player player = new Player();
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        Player result = playerService.createPlayer(request);
        assertNotNull(result);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void createPlayer_nullRequest() {
        assertThrows(NullableRequestException.class, () -> playerService.createPlayer(null));
    }

    @Test
    void createPlayer_tooMuchExperience() {
        PlayerInformationRequest request = createRequest("Leo", "Messi", 38, 500, 1L);
        assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer(request));
    }

    @Test
    void createPlayer_teamNotFound() {
        PlayerInformationRequest request = createRequest("Leo", "Messi", 38, 380, 1L);
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> playerService.createPlayer(request));
    }

    @Test
    void updatePlayer_success() {
        PlayerInformationRequest request = createRequest("Cristiano", "Ronaldo", 41, 400, 2L);
        Player player = new Player();
        player.setAge(20);
        player.setMonthsOfExperience(48);
        Team team = new Team();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(team));
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        Player result = playerService.updatePlayer(1L, request);
        assertNotNull(result);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void updatePlayer_nullRequest() {
        assertThrows(NullableRequestException.class, () -> playerService.updatePlayer(1L, null));
    }

    @Test
    void updatePlayer_tooMuchExperience() {
        PlayerInformationRequest request = createRequest("Cristiano", "Ronaldo", 41, 500, 2L);
        Player player = new Player();
        player.setAge(22);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayer(1L, request));
    }

    @Test
    void updatePlayer_teamNotFound() {
        PlayerInformationRequest request = createRequest("Cristiano", "Ronaldo", 41, 400, 2L);
        Player player = new Player();
        player.setAge(22);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> playerService.updatePlayer(1L, request));
    }

    @Test
    void readPlayer_success() {
        Player player = new Player();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        Player result = playerService.readPlayer(1L);
        assertNotNull(result);
    }

    @Test
    void readPlayer_notFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> playerService.readPlayer(1L));
    }

    @Test
    void deletePlayer_success() {
        Player player = new Player();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        doNothing().when(playerRepository).delete(player);
        playerService.deletePlayer(1L);
        verify(playerRepository).delete(player);
    }

    @Test
    void getAllPlayers_success() {
        Player player1 = new Player();
        Player player2 = new Player();
        when(playerRepository.findAllPlayers()).thenReturn(Arrays.asList(player1, player2));
        List<Player> players = playerService.getAllPlayers();
        assertEquals(2, players.size());
    }

    private PlayerInformationRequest createRequest(String firstName, String lastName, Integer age, Integer monthsOfExperience, Long teamId) {
        PlayerInformationRequest req = new PlayerInformationRequest();
        req.setFirstName(firstName);
        req.setLastName(lastName);
        req.setAge(age);
        req.setMonthsOfExperience(monthsOfExperience);
        req.setTeamId(teamId);
        return req;
    }
} 