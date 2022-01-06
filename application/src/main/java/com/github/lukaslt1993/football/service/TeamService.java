package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.repository.PlayerRepository;
import com.github.lukaslt1993.football.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeamService {

    private TeamRepository teamRepo;
    private PlayerRepository playerRepo;

    public TeamService(TeamRepository teamRepo, PlayerRepository playerRepo) {
        this.teamRepo = teamRepo;
        this.playerRepo = playerRepo;
    }

    public Team createTeam(TeamControllerRequestBody team) {
        Team entity = new Team();
        return teamRepo.save(copy(team, entity));
    }

    public List<Team> getAllTeams() {
        return StreamSupport.stream(teamRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Team getTeam(Long id) {
        return teamRepo.findById(id).orElse(null);
    }

    public void updateTeam(TeamControllerRequestBody updated, Long id) {
        Team old = teamRepo.findById(id).get();
        teamRepo.save(copy(updated, old));
    }

    public void deleteTeam(Long id) {
        Team team = teamRepo.findById(id).get();

        for (Player player : team.getPlayers()) {
            player.setTeam(null);
            playerRepo.save(player);
        }

        teamRepo.delete(team);
    }

    private Team copy(TeamControllerRequestBody from, Team to) {
        to.setCommissionPercent(from.getCommissionPercent());
        to.setCurrency(from.getCurrency());
        to.setMoney(new BigDecimal(from.getMoney()));
        to.setName(from.getName());

        if (from.getPlayerIds() != null && !from.getPlayerIds().isEmpty()) {

            for (long id : from.getPlayerIds()) {
                Player player = playerRepo.findById(id).get();
                to.addPlayer(player);
                playerRepo.save(player);
            }

        } else {
            to.setPlayers(new ArrayList<>());
        }

        return to;
    }

}
