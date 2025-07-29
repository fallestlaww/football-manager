package org.example.manager.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.manager.dto.request.PlayerInformationRequest;
import org.example.manager.exception.custom.NullableRequestException;
import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.example.manager.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerService playerService;

    private Player createPlayerWithTeam(Long playerId, String firstName, String lastName, Long teamId, String teamName) {
        Player player = new Player();
        player.setId(playerId);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        player.setTeam(team);
        return player;
    }

    @Test
    void createPlayer() throws Exception {
        Player player = createPlayerWithTeam(1L, "Lionel", "Messi", 1L, "FC Barcelona");
        Mockito.when(playerService.createPlayer(any(PlayerInformationRequest.class))).thenReturn(player);
        mockMvc.perform(post("/player/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\":\"Lionel\",\"last_name\":\"Messi\",\"age\":36,\"months_of_experience\":240,\"team_id\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createPlayer_invalidRequest() throws Exception {
        Mockito.when(playerService.createPlayer(any(PlayerInformationRequest.class)))
                .thenThrow(new NullableRequestException("Invalid request"));
        mockMvc.perform(post("/player/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPlayer_illegalArgument() throws Exception {
        Mockito.when(playerService.createPlayer(any(PlayerInformationRequest.class)))
                .thenThrow(new IllegalArgumentException("Months of experience cannot be greater than max experience"));
        mockMvc.perform(post("/player/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\":\"Cristiano\",\"last_name\":\"Ronaldo\",\"age\":20,\"months_of_experience\":500,\"team_id\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPlayer_entityExists() throws Exception {
        Mockito.when(playerService.createPlayer(any(PlayerInformationRequest.class)))
                .thenThrow(new jakarta.persistence.EntityExistsException("Player already exists"));
        mockMvc.perform(post("/player/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\":\"Erling\",\"last_name\":\"Haaland\",\"age\":25,\"months_of_experience\":84,\"team_id\":1}"))
                .andExpect(status().isConflict());
    }

    @Test
    void readPlayer() throws Exception {
        Player player = createPlayerWithTeam(1L, "Lionel", "Messi", 1L, "FC Barcelona");
        Mockito.when(playerService.readPlayer(1L)).thenReturn(player);
        mockMvc.perform(get("/player/1"))
                .andExpect(status().isFound());
    }

    @Test
    void readPlayer_notFound() throws Exception {
        Mockito.when(playerService.readPlayer(99L)).thenThrow(new EntityNotFoundException("Player not found"));
        mockMvc.perform(get("/player/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePlayer() throws Exception {
        Player player = createPlayerWithTeam(1L, "Cristiano", "Ronaldo", 2L, "Real Madrid");
        Mockito.when(playerService.updatePlayer(eq(1L), any(PlayerInformationRequest.class))).thenReturn(player);
        mockMvc.perform(put("/player/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\":\"Cristiano\",\"last_name\":\"Ronaldo\",\"age\":38,\"months_of_experience\":252,\"team_id\":2}"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePlayer_notFound() throws Exception {
        Mockito.when(playerService.updatePlayer(eq(99L), any(PlayerInformationRequest.class))).thenThrow(new EntityNotFoundException("Player not found"));
        mockMvc.perform(put("/player/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\":\"Cristiano\",\"last_name\":\"Ronaldo\",\"age\":38,\"months_of_experience\":252,\"team_id\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlayer() throws Exception {
        Mockito.doNothing().when(playerService).deletePlayer(1L);
        mockMvc.perform(delete("/player/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePlayer_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Player not found")).when(playerService).deletePlayer(99L);
        mockMvc.perform(delete("/player/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listPlayers() throws Exception {
        Player player1 = createPlayerWithTeam(1L, "Lionel", "Messi", 1L, "FC Barcelona");
        Player player2 = createPlayerWithTeam(2L, "Cristiano", "Ronaldo", 2L, "Real Madrid");
        Mockito.when(playerService.getAllPlayers()).thenReturn(Arrays.asList(player1, player2));
        mockMvc.perform(get("/player/list"))
                .andExpect(status().isOk());
    }
} 