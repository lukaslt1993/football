package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.repository.PlayerRepository;
import com.github.lukaslt1993.football.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PlayerTransferService {

    @Value("${application.transfer.multiplier}")
    private int multiplier;

    private PlayerRepository playerRepo;
    private TeamRepository teamRepo;
    private ConverterService converter;

    public PlayerTransferService(PlayerRepository playerRepo, TeamRepository teamRepo, ConverterService converter) {
        this.playerRepo = playerRepo;
        this.teamRepo = teamRepo;
        this.converter = converter;
    }

    public void transfer(long playerId, long teamId) {
        transfer(playerRepo.findById(playerId).get(), teamRepo.findById(teamId).get());
    }

    public void transfer(Player player, Team team) {
        BigDecimal contractFee = getContractFee(player);

        Team currTeam = player.getTeam();

        BigDecimal priceInOtherTeamCurrency =
                converter.convert(currTeam.getCurrency(), contractFee.toPlainString(), team.getCurrency());

        BigDecimal moneyRemains = team.getMoney().subtract(priceInOtherTeamCurrency);

        if (moneyRemains.intValue() < 0)
            throw new RuntimeException("Team hasn't got enough of money for the transfer");

        team.setMoney(moneyRemains);
        currTeam.setMoney(currTeam.getMoney().add(contractFee));
        currTeam.removePlayer(player);
        player.setTeam(team);
        teamRepo.save(team);
        playerRepo.save(player);
    }

    public BigDecimal getContractFee(Player player) {
        BigDecimal transferFee =
                new BigDecimal(player.getExperienceMonths() * multiplier)
                        .divide(new BigDecimal(player.getAge()), RoundingMode.HALF_UP);

        return transferFee.add(new BigDecimal(player.getTeam().getCommissionPercent())
                .divide(new BigDecimal(100))
                .multiply(transferFee));
    }

}
