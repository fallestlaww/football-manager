package org.example.manager.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.manager.model.Player;

/**
 * Data Transfer Object for player information response
 * Used to return player information in API responses with snake_case naming
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlayerInformationResponse {
    Long id;
    String firstName;
    String lastName;
    Integer age;
    Integer monthsOfExperience;
    String teamName;

    /**
     * Constructor that creates a response object from a Player entity
     * @param player the player entity to extract information from
     */
    public PlayerInformationResponse(Player player) {
        id = player.getId();
        firstName = player.getFirstName();
        lastName = player.getLastName();
        age = player.getAge();
        monthsOfExperience = player.getMonthsOfExperience();
        teamName = player.getTeam().getName();
    }
}
