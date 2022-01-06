package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @MockBean
    ConverterService converter;

    @Test
    void transferSameCurrency() {
        var player = service.createPlayer(new PlayerControllerRequestBody("player", 20, 10,  null));

        var team1 = teams.createTeam(new TeamControllerRequestBody("team1", "USD", 1000000, 10, List.of(player.getId())));
        var team2 = teams.createTeam(new TeamControllerRequestBody("team2", "USD", 100000000, 10, List.of()));
        var team2InitialMoney = team2.getMoney();

        var contractFee = transferService.getContractFee(player);

        Mockito.when(converter.convert(team1.getCurrency(), contractFee.toPlainString(), team2.getCurrency()))
                .thenReturn(contractFee);

        transferService.transfer(player, team2);

        assertEquals(team2.getId(), player.getTeam().getId());
        assertEquals(team2InitialMoney.subtract(contractFee), team2.getMoney());
    }

    @Test
    void transferDifferentCurrency() {
        var player = service.createPlayer(new PlayerControllerRequestBody("player", 20, 10,  null));

        var team1 = teams.createTeam(new TeamControllerRequestBody("team1", "USD", 1000000, 10, List.of(player.getId())));
        var team2 = teams.createTeam(new TeamControllerRequestBody("team2", "RUB", 100000000, 10, List.of()));
        var team2InitialMoney = team2.getMoney();

        var contractFee = transferService.getContractFee(player);
        var contractFeeInOtherTeamCurrency = contractFee.multiply(BigDecimal.valueOf(76));

        Mockito.when(converter.convert(team1.getCurrency(), contractFee.toPlainString(), team2.getCurrency()))
                .thenReturn(contractFeeInOtherTeamCurrency);

        transferService.transfer(player, team2);

        assertEquals(team2.getId(), player.getTeam().getId());
        assertEquals(team2InitialMoney.subtract(contractFeeInOtherTeamCurrency), team2.getMoney());
    }

}