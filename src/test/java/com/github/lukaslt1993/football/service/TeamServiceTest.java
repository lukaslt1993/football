package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class TeamServiceTest {

    @Autowired
    TeamService service;

    @Autowired
    PlayerService players;

    @Test
    void insertSynchronizesWithPlayer() {
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(100000000);
        team.setName("team1");
        service.createTeam(team);
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        player.setTeamId(1L);
        players.createPlayer(player);
        // Entity ids are generated sequentially, starting from 1
        assertTrue(service.getTeam(1L).getPlayers().stream().anyMatch(p -> p.getId() == 2));
    }

    @Test
    void updateSynchronizesWithTeam() {
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(100000000);
        team.setName("team1");
        service.createTeam(team);
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        player.setTeamId(1L);
        players.createPlayer(player);

        PlayerControllerRequestBody player2 = new PlayerControllerRequestBody();
        player2.setName("player2");
        player2.setAge(20);
        player2.setExperienceMonths(10);
        players.createPlayer(player2);
        service.getTeam(1L).setPlayers(new ArrayList<>(){{this.add(players.getPlayer(3L));}});
        assertEquals(3, service.getTeam(1L).getPlayers().get(0).getId());
    }

}