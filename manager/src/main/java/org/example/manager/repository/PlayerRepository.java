package org.example.manager.repository;

import org.example.manager.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Player entity
 * Provides CRUD operations and custom queries for Player entities
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /**
     * Custom query to find all players
     * @return List of all players in the database
     */
    @Query("SELECT p FROM Player p")
    List<Player> findAllPlayers();
}
