package org.example.manager.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.manager.dto.request.TeamInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Team;
import org.example.manager.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void createTeam_success() {
        TeamInformationRequest request = createRequest("Barcelona", "Spain", new BigDecimal("1000"), 5.0);
        when(teamRepository.findByName("Barcelona")).thenReturn(null);
        Team team = new Team();
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        Team result = teamService.createTeam(request);
        assertNotNull(result);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void createTeam_nullRequest() {
        assertThrows(NullableRequestException.class, () -> teamService.createTeam(null));
    }

    @Test
    void createTeam_exists() {
        TeamInformationRequest request = createRequest("Barcelona", "Spain", new BigDecimal("1000"), 5.0);
        when(teamRepository.findByName("Barcelona")).thenReturn(new Team());
        assertThrows(EntityExistsException.class, () -> teamService.createTeam(request));
    }

    @Test
    void updateTeam_success() {
        TeamInformationRequest request = createRequest("Lazio", "Italy", new BigDecimal("2000"), 6.0);
        Team team = new Team();
        team.setName("Juventus");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.existsByNameAndIdNot("Lazio", 1L)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        Team result = teamService.updateTeam(1L, request);
        assertNotNull(result);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void updateTeam_nullRequest() {
        assertThrows(NullableRequestException.class, () -> teamService.updateTeam(1L, null));
    }

    @Test
    void updateTeam_exists() {
        TeamInformationRequest request = createRequest("Lazio", "Italy", new BigDecimal("2000"), 6.0);
        Team team = new Team();
        team.setName("Juventus");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.existsByNameAndIdNot("Lazio", 1L)).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> teamService.updateTeam(1L, request));
    }

    @Test
    void readTeam_success() {
        Team team = new Team();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        Team result = teamService.readTeam(1L);
        assertNotNull(result);
    }

    @Test
    void readTeam_notFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> teamService.readTeam(1L));
    }

    @Test
    void deleteTeam_success() {
        Team team = new Team();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        doNothing().when(teamRepository).delete(team);
        teamService.deleteTeam(1L);
        verify(teamRepository).delete(team);
    }

    @Test
    void getAllTeams_success() {
        Team team1 = new Team();
        Team team2 = new Team();
        when(teamRepository.findAllTeams()).thenReturn(Arrays.asList(team1, team2));
        List<Team> teams = teamService.getAllTeams();
        assertEquals(2, teams.size());
    }

    private TeamInformationRequest createRequest(String name, String country, BigDecimal balance, double commissionRate) {
        TeamInformationRequest req = new TeamInformationRequest();
        req.setName(name);
        req.setCountry(country);
        req.setBalance(balance);
        req.setCommissionRate(commissionRate);
        return req;
    }
} 