package com.github.lukaslt1993.football.controller;

import com.github.lukaslt1993.football.controller.data.TeamControllerRequestBody;
import com.github.lukaslt1993.football.entity.Team;
import com.github.lukaslt1993.football.service.TeamService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @PostMapping
    public String createTeam(@RequestBody TeamControllerRequestBody team) {
        service.createTeam(team);
        return "Team created";
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return service.getAllTeams();
    }

    @GetMapping(path = "/{id}")
    public Team getTeam(@PathVariable Long id) {
        return service.getTeam(id);
    }

    @PutMapping(path = "/{id}")
    public String updateTeam(@RequestBody TeamControllerRequestBody updated, @PathVariable Long id) {
        service.updateTeam(updated, id);
        return "Team updated";
    }

    @DeleteMapping(path = "/{id}")
    public String deleteTeam(@PathVariable Long id) {
        service.deleteTeam(id);
        return "Team deleted";
    }

}
