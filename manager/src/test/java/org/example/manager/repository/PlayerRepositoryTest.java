package org.example.manager.repository;

import org.example.manager.model.Player;
import org.example.manager.model.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    void findAllPlayers() {
        Team team = createAndSaveTeam();
        Player player1 = new Player();
        player1.setFirstName("Leo");
        player1.setLastName("Messi");
        player1.setAge(38);
        player1.setMonthsOfExperience(384);
        player1.setTeam(team);
        Player player2 = new Player();
        player2.setFirstName("Cristiano");
        player2.setLastName("Ronaldo");
        player2.setAge(41);
        player2.setMonthsOfExperience(420);
        player2.setTeam(team);
        playerRepository.save(player1);
        playerRepository.save(player2);
        List<Player> players = playerRepository.findAllPlayers();
        assertEquals(2, players.size());
    }

    private Team createAndSaveTeam() {
        Team team = new Team();
        team.setName("Barcelona");
        team.setCountry("Spain");
        team.setBalance(BigDecimal.TEN);
        team.setCommissionRate(1.0);
        return teamRepository.save(team);
    }
} 