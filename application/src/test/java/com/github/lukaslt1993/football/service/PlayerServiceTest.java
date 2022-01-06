package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class PlayerServiceTest {

    @Autowired
    PlayerService service;

    @Autowired
    TeamService teams;

    @Test
    void teamGetsAssignedToPlayer() {
        var player = service.createPlayer(new PlayerControllerRequestBody("player", 20, 10,  null));
        var team = teams.createTeam(new TeamControllerRequestBody("team", "USD", 1000000, 10, List.of(player.getId())));
        assertEquals(team.getId(), player.getTeam().getId());
    }

    @Test
    void playerGetsAssignedToOtherTeam() {
        var player = service.createPlayer(new PlayerControllerRequestBody("player", 20, 10,  null));
        var team1 = teams.createTeam(new TeamControllerRequestBody("team1", "USD", 1000000, 10, List.of(player.getId())));
        var team2 = teams.createTeam(new TeamControllerRequestBody("team2", "EUR", 1000000, 10, List.of()));
        player.setTeam(team2);
        assertEquals(team2.getId(), player.getTeam().getId());
    }

    @Test
    void twoTeamsCantHaveSamePlayer() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            var player = service.createPlayer(new PlayerControllerRequestBody("player", 20, 10,  null));
            teams.createTeam(new TeamControllerRequestBody("team1", "USD", 1000000, 10, List.of(player.getId())));
            teams.createTeam(new TeamControllerRequestBody("team2", "EUR", 1000000, 10, List.of(player.getId())));
            service.getAllPlayers();
        });
    }

}