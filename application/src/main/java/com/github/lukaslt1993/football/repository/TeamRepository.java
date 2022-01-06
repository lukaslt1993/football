package com.github.lukaslt1993.football.repository;

import com.github.lukaslt1993.football.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
}
