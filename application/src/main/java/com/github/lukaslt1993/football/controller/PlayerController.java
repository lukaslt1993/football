package com.github.lukaslt1993.football.controller;

import com.github.lukaslt1993.football.controller.data.PlayerControllerRequestBody;
import com.github.lukaslt1993.football.entity.Player;
import com.github.lukaslt1993.football.service.PlayerService;
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
@RequestMapping("/players")
public class PlayerController {

    private PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @PostMapping
    public String createPlayer(@RequestBody PlayerControllerRequestBody player) {
        service.createPlayer(player);
        return "Player created";
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return service.getAllPlayers();
    }

    @GetMapping(path = "/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return service.getPlayer(id);
    }

    @PutMapping(path = "/{id}")
    public String updatePlayer(@RequestBody PlayerControllerRequestBody updated, @PathVariable Long id) {
        service.updatePlayer(updated, id);
        return "Player updated";
    }

    @DeleteMapping(path = "/{id}")
    public String deletePlayer(@PathVariable Long id) {
        service.deletePlayer(id);
        return "Player deleted";
    }

}
