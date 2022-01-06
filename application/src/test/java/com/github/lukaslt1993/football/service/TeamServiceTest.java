package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class TeamServiceTest {

    @Autowired
    TeamService service;

    @Autowired
    PlayerService players;

    @Test
    void playersGetAssignedToTeam() {
        var team = service.createTeam(new TeamControllerRequestBody("team", "USD", 1000000, 10, List.of()));
        var player1 = players.createPlayer(new PlayerControllerRequestBody("player1", 20, 10,  team.getId()));
        var player2 = players.createPlayer(new PlayerControllerRequestBody("player2", 20, 10,  team.getId()));
        assertEquals(List.of(player1, player2), team.getPlayers());
    }

}