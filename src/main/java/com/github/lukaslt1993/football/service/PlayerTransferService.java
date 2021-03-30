package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.repository.PlayerRepository;
import com.github.lukaslt1993.football.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PlayerTransferService {

    public static final int MULTIPLIER = 100000;
    private static final String CONVERTER_ADDRESS = "http://localhost:8081";

    private PlayerRepository playerRepo;
    private TeamRepository teamRepo;
    private RestTemplate client = new RestTemplate();

    public PlayerTransferService(PlayerRepository playerRepo, TeamRepository teamRepo) {
        this.playerRepo = playerRepo;
        this.teamRepo = teamRepo;
    }

    public void transfer(long playerId, long teamId) {
        transfer(playerRepo.findById(playerId).get(), teamRepo.findById(teamId).get());
    }

    public void transfer(Player player, Team team) {
        Team currTeam = player.getTeam();
        BigDecimal transferFee =
                new BigDecimal(player.getExperienceMonths() * MULTIPLIER)
                        .divide(new BigDecimal(player.getAge()), RoundingMode.HALF_UP);
        BigDecimal contractFee =
                transferFee.add(new BigDecimal(currTeam.getCommissionPercent()).
                        divide(new BigDecimal(100)).multiply(transferFee));
        BigDecimal priceInOtherTeamCurrency =
                convert(currTeam.getCurrency(), contractFee.toPlainString(), team.getCurrency());
        BigDecimal moneyRemains = team.getMoney().subtract(priceInOtherTeamCurrency);

        if (moneyRemains.intValue() < 0) {
            throw new RuntimeException("Team hasn't got enough of money for the transfer");
        }

        team.setMoney(moneyRemains);
        currTeam.setMoney(currTeam.getMoney().add(contractFee));
        currTeam.removePlayer(player);
        player.setTeam(team);
        teamRepo.save(team);
        playerRepo.save(player);
    }

    private BigDecimal convert (String fromCurrency, String amount, String toCurrency) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(CONVERTER_ADDRESS);
        builder.queryParam("fromCurrency", fromCurrency);
        builder.queryParam("amount", amount);
        builder.queryParam("toCurrency", toCurrency);
        String response = client.getForObject(builder.build().toUri(), String.class);
        return new BigDecimal(response);
    }

}
