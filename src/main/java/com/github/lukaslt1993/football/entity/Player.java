package com.github.lukaslt1993.football.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Player name must be set")
    private String name;

    @Min(value = 16, message = "Player age >= 16 must be set")
    private int age;

    private int experienceMonths;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    private Team team;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExperienceMonths() {
        return experienceMonths;
    }

    public void setExperienceMonths(int experienceMonths) {
        this.experienceMonths = experienceMonths;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (team != null) {
            if (this.team == null || this.team.getId() != team.getId()) {
                team.addPlayer(this);
            }
        }
        this.team = team;
    }

}
