package com.github.lukaslt1993.football.repository;

import com.github.lukaslt1993.football.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
