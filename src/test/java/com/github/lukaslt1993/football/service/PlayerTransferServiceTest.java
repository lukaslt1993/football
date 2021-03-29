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
class PlayerTransferServiceTest {

    @Autowired
    PlayerTransferService transferService;

    @Autowired
    PlayerService service;

    @Autowired
    TeamService teams;

    @Test
    void transferSameCurrency() {
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        service.createPlayer(player);
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(1000000);
        team.setName("team1");
        team.setPlayerIds(new ArrayList<>(){{this.add(1);}});
        teams.createTeam(team);

        TeamControllerRequestBody team2 = new TeamControllerRequestBody();
        team2.setCurrency("USD");
        team2.setName("team2");
        team2.setMoney(100000000);
        team2.setCommissionPercent(10);
        teams.createTeam(team2);
        // Entity ids are generated sequentially, starting from 1
        transferService.transfer(1, 3);
        assertEquals(3, service.getPlayer(1L).getTeam().getId());
        assertEquals(100000000 - 55000, teams.getTeam(3L).getMoney());
    }

    @Test
    void transferDifferentCurrency() {
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        service.createPlayer(player);
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(1000000);
        team.setName("team1");
        team.setPlayerIds(new ArrayList<>(){{this.add(1);}});
        teams.createTeam(team);

        TeamControllerRequestBody team2 = new TeamControllerRequestBody();
        team2.setCurrency("RUB");
        team2.setName("team2");
        team2.setMoney(100000000);
        team2.setCommissionPercent(10);
        teams.createTeam(team2);
        // Entity ids are generated sequentially, starting from 1
        transferService.transfer(1, 3);
        assertEquals(3, service.getPlayer(1L).getTeam().getId());
        assertTrue(100000000 - 55000 > teams.getTeam(3L).getMoney());
    }

}