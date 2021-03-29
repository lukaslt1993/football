package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;

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
    void insertSynchronizesWithTeam() {
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        service.createPlayer(player);
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(100000000);
        team.setName("team1");
        team.setPlayerIds(new ArrayList<>(){{this.add(1);}});
        teams.createTeam(team);
        // Entity ids are generated sequentially, starting from 1
        assertEquals(2, service.getPlayer(1L).getTeam().getId());
    }

    @Test
    void updateSynchronizesWithTeam() {
        PlayerControllerRequestBody player = new PlayerControllerRequestBody();
        player.setName("player1");
        player.setAge(20);
        player.setExperienceMonths(10);
        service.createPlayer(player);
        TeamControllerRequestBody team = new TeamControllerRequestBody();
        team.setCommissionPercent(10);
        team.setCurrency("USD");
        team.setMoney(100000000);
        team.setName("team1");
        team.setPlayerIds(new ArrayList<>(){{this.add(1);}});
        teams.createTeam(team);

        TeamControllerRequestBody team2 = new TeamControllerRequestBody();
        team2.setCurrency("EUR");
        team2.setName("team2");
        team2.setMoney(100000000);
        team2.setCommissionPercent(10);
        teams.createTeam(team2);
        service.getPlayer(1L).setTeam(teams.getTeam(3L));
        assertEquals(3, service.getPlayer(1L).getTeam().getId());
    }

    @Test
    void twoTeamsCantHaveSamePlayer() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            PlayerControllerRequestBody player = new PlayerControllerRequestBody();
            player.setName("player1");
            player.setAge(20);
            player.setExperienceMonths(10);
            service.createPlayer(player);
            TeamControllerRequestBody team = new TeamControllerRequestBody();
            team.setCommissionPercent(10);
            team.setCurrency("USD");
            team.setMoney(100000000);
            team.setName("team1");
            team.setPlayerIds(new ArrayList<>(){{this.add(1);}});
            teams.createTeam(team);

            TeamControllerRequestBody team2 = new TeamControllerRequestBody();
            team2.setCurrency("EUR");
            team2.setName("team2");
            team2.setMoney(100000000);
            team2.setCommissionPercent(10);
            team2.setPlayerIds(new ArrayList<>(){{this.add(1);}});
            teams.createTeam(team2);
            service.getAllPlayers();
        });
    }

}