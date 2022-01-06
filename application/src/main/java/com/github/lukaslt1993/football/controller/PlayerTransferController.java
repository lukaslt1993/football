package com.github.lukaslt1993.football.controller;

import com.github.lukaslt1993.football.service.PlayerTransferService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class PlayerTransferController {

    private PlayerTransferService service;

    public PlayerTransferController(PlayerTransferService service) {
        this.service = service;
    }

    @PutMapping (path = "/{playerId}/{teamId}")
    public String transfer(@PathVariable long playerId, @PathVariable long teamId) {
        service.transfer(playerId, teamId);
        return "Player transferred";
    }

}
