package com.adminsurfacetech.my_learn_hub.Models;

public class Ranking {
    private String username;
    private long score;

    public Ranking() {
    }

    public Ranking(String username, long score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
