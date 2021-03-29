package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.repository.PlayerRepository;
import com.github.lukaslt1993.football.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        int transferFee = player.getExperienceMonths() * MULTIPLIER / player.getAge();
        double contractFee = transferFee + (currTeam.getCommissionPercent() / 100 * transferFee);
        double priceInOtherTeamCurrency = convert(currTeam.getCurrency(), contractFee, team.getCurrency());
        double moneyRemains = team.getMoney() - priceInOtherTeamCurrency;

        if (moneyRemains < 0) {
            throw new RuntimeException("Team hasn't got enough of money for the transfer");
        }

        team.setMoney(moneyRemains);
        currTeam.setMoney(currTeam.getMoney() + contractFee);
        currTeam.removePlayer(player);
        player.setTeam(team);
        teamRepo.save(team);
        playerRepo.save(player);
    }

    private double convert(String fromCurrency, Double amount, String toCurrency) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(CONVERTER_ADDRESS);
        builder.queryParam("fromCurrency", fromCurrency);
        builder.queryParam("amount", amount);
        builder.queryParam("toCurrency", toCurrency);
        return client.getForObject(builder.build().toUri(), Double.class);
    }

}
