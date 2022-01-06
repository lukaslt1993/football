package com.github.lukaslt1993.football.service;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.repository.PlayerRepository;
import com.github.lukaslt1993.football.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerService {

    private PlayerRepository playerRepo;
    private TeamRepository teamRepo;

    public PlayerService(PlayerRepository playerRepo, TeamRepository teamRepo) {
        this.playerRepo = playerRepo;
        this.teamRepo = teamRepo;
    }

    public Player createPlayer(PlayerControllerRequestBody player) {
        Player entity = new Player();
        entity.setName(player.getName());
        entity.setAge(player.getAge());
        entity.setExperienceMonths(player.getExperienceMonths());

        if (player.getTeamId() != null)
            teamRepo.findById(player.getTeamId()).get().addPlayer(entity);

        return playerRepo.save(entity);
    }

    public List<Player> getAllPlayers() {
        return StreamSupport.stream(playerRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Player getPlayer(Long id) {
        return playerRepo.findById(id).orElse(null);
    }

    public void updatePlayer(PlayerControllerRequestBody updated, Long id) {
        Player old = playerRepo.findById(id).get();
        playerRepo.save(copy(updated, old));
    }

    public void deletePlayer(Long id) {
        Player player = playerRepo.findById(id).get();
        player.getTeam().removePlayer(player);
        teamRepo.save(player.getTeam());
        playerRepo.delete(player);
    }

    private Player copy(PlayerControllerRequestBody from, Player to) {
        to.setAge(from.getAge());
        to.setExperienceMonths(from.getExperienceMonths());
        to.setName(from.getName());

        if (from.getTeamId() != null) {
            Team team = teamRepo.findById(from.getTeamId()).get();
            to.setTeam(team);
            teamRepo.save(team);
        } else {
            to.setTeam(null);
        }

        return to;
    }

}
