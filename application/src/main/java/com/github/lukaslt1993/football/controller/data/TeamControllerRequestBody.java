package com.github.lukaslt1993.football.controller.data;

import java.util.List;

public class TeamControllerRequestBody {

    private String name;
    private String currency;
    private double money;
    private double commissionPercent;
    private List<Long> playerIds;

    public TeamControllerRequestBody() {

    }

    public TeamControllerRequestBody(
        String name,
        String currency,
        double money,
        double commissionPercent,
        List<Long> playerIds
    ) {
        this.name = name;
        this.currency = currency;
        this.money = money;
        this.commissionPercent = commissionPercent;
        this.playerIds = playerIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(double commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public List<Long> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Long> playerIds) {
        this.playerIds = playerIds;
    }

}
