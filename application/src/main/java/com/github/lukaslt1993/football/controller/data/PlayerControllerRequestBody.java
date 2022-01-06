package com.github.lukaslt1993.football.controller.data;

public class PlayerControllerRequestBody {

    private String name;
    private int age;
    private int experienceMonths;
    private Long teamId;

    public PlayerControllerRequestBody() {

    }

    public PlayerControllerRequestBody(String name, int age, int experienceMonths, Long teamId) {
        this.name = name;
        this.age = age;
        this.experienceMonths = experienceMonths;
        this.teamId = teamId;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

}
