package org.example.manager.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.manager.dto.request.TeamInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Team;
import org.example.manager.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamService teamService;

    private Team createTeam(Long id, String name, String country) {
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        team.setCountry(country);
        team.setBalance(BigDecimal.valueOf(1000000));
        team.setCommissionRate(5.0);
        return team;
    }

    @Test
    void createTeam() throws Exception {
        Team team = createTeam(1L, "FC Barcelona", "Spain");
        Mockito.when(teamService.createTeam(any(TeamInformationRequest.class))).thenReturn(team);
        mockMvc.perform(post("/team/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FC Barcelona\",\"country\":\"Spain\",\"balance\":1000000,\"commission_rate\":5}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createTeam_invalidRequest() throws Exception {
        Mockito.when(teamService.createTeam(any(TeamInformationRequest.class)))
                .thenThrow(new NullableRequestException("Invalid request"));
        mockMvc.perform(post("/team/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTeam_entityExists() throws Exception {
        Mockito.when(teamService.createTeam(any(TeamInformationRequest.class)))
                .thenThrow(new EntityExistsException("Team already exists"));
        mockMvc.perform(post("/team/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FC Barcelona\",\"country\":\"Spain\",\"balance\":1000000,\"commission_rate\":5}"))
                .andExpect(status().isConflict());
    }

    @Test
    void readTeam() throws Exception {
        Team team = createTeam(1L, "FC Barcelona", "Spain");
        Mockito.when(teamService.readTeam(1L)).thenReturn(team);
        mockMvc.perform(get("/team/1"))
                .andExpect(status().isFound());
    }

    @Test
    void readTeam_notFound() throws Exception {
        Mockito.when(teamService.readTeam(99L)).thenThrow(new EntityNotFoundException("Team not found"));
        mockMvc.perform(get("/team/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTeam() throws Exception {
        Team team = createTeam(1L, "FC Barcelona", "Spain");
        Mockito.when(teamService.updateTeam(eq(1L), any(TeamInformationRequest.class))).thenReturn(team);
        mockMvc.perform(put("/team/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FC Barcelona\",\"country\":\"Spain\",\"balance\":2000000,\"commission_rate\":4.5}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTeam_notFound() throws Exception {
        Mockito.when(teamService.updateTeam(eq(99L), any(TeamInformationRequest.class))).thenThrow(new EntityNotFoundException("Team not found"));
        mockMvc.perform(put("/team/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FC Barcelona\",\"country\":\"Spain\",\"balance\":2000000,\"commission_rate\":4.5}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTeam_entityExists() throws Exception {
        Mockito.when(teamService.updateTeam(eq(1L), any(TeamInformationRequest.class)))
                .thenThrow(new EntityExistsException("Team already exists"));
        mockMvc.perform(put("/team/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FC Barcelona\",\"country\":\"Spain\",\"balance\":2000000,\"commission_rate\":4.5}"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteTeam() throws Exception {
        Mockito.doNothing().when(teamService).deleteTeam(1L);
        mockMvc.perform(delete("/team/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTeam_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Team not found")).when(teamService).deleteTeam(99L);
        mockMvc.perform(delete("/team/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listTeams() throws Exception {
        Team team1 = createTeam(1L, "FC Barcelona", "Spain");
        Team team2 = createTeam(2L, "Real Madrid", "Spain");
        Mockito.when(teamService.getAllTeams()).thenReturn(Arrays.asList(team1, team2));
        mockMvc.perform(get("/team/list"))
                .andExpect(status().isOk());
    }
} 