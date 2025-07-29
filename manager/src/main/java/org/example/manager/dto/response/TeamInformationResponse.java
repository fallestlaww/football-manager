package org.example.manager.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.example.manager.model.Team;

import java.math.BigDecimal;
/**
 * Data Transfer Object for team information response
 * Used to return team information in API responses with snake_case naming
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamInformationResponse {
    Long id;
    String name;
    String country;
    /**
     * Team's financial balance formatted as string with two decimal places
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
    BigDecimal balance;
    Double commissionRate;

    /**
     * Constructor that creates a response object from a Team entity
     * @param team the team entity to extract information from
     */
    public TeamInformationResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.country = team.getCountry();
        this.balance = team.getBalance();
        this.commissionRate = team.getCommissionRate();
    }
}
