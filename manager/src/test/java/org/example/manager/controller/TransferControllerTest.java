package org.example.manager.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.manager.exception.custom.LowBalanceException;
import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.example.manager.service.PlayerService;
import org.example.manager.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransferService transferService;
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
    void transferPlayer_success() throws Exception {
        Player player = createPlayerWithTeam(1L, "Lionel", "Messi", 2L, "Real Madrid");
        Mockito.doNothing().when(transferService).transferPlayer(1L, 2L);
        when(playerService.readPlayer(1L)).thenReturn(player);
        mockMvc.perform(post("/transfer/1/to/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void transferPlayer_playerNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Player not found")).when(transferService).transferPlayer(99L, 2L);
        mockMvc.perform(post("/transfer/99/to/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void transferPlayer_teamNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Team not found")).when(transferService).transferPlayer(1L, 99L);
        mockMvc.perform(post("/transfer/1/to/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void transferPlayer_lowBalance() throws Exception {
        doThrow(new LowBalanceException("Low balance")).when(transferService).transferPlayer(1L, 2L);
        mockMvc.perform(post("/transfer/1/to/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
} 