package org.example.manager.repository;

import org.example.manager.model.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TeamRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;

    @Test
    void findAllTeams() {
        Team team1 = createTeam("Barcelona", "Spain");
        Team team2 = createTeam("Chelsea", "England");
        teamRepository.save(team1);
        teamRepository.save(team2);
        List<Team> teams = teamRepository.findAllTeams();
        assertEquals(2, teams.size());
    }

    @Test
    void findByName() {
        Team team = createTeam("PSG", "France");
        teamRepository.save(team);
        Team found = teamRepository.findByName("PSG");
        assertNotNull(found);
        assertEquals("PSG", found.getName());
    }

    @Test
    void existsByNameAndIdNot() {
        Team team1 = createTeam("Barcelona", "Spain");
        Team team2 = createTeam("Chelsea", "England");
        team1 = teamRepository.save(team1);
        team2 = teamRepository.save(team2);
        boolean exists = teamRepository.existsByNameAndIdNot("Barcelona", team2.getId());
        assertTrue(exists);
        boolean notExists = teamRepository.existsByNameAndIdNot("Barcelona", team1.getId());
        assertFalse(notExists);
    }

    private Team createTeam(String name, String country) {
        Team team = new Team();
        team.setName(name);
        team.setCountry(country);
        team.setBalance(BigDecimal.TEN);
        team.setCommissionRate(1.0);
        return team;
    }
} 