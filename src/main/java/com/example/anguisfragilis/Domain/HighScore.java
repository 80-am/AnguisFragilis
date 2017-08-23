package com.example.anguisfragilis.Domain;

public class HighScore {
    int score;
    String username;

    public HighScore(int score, String username) {
        this.score = score;
        this.username = username;
    }

    public HighScore() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
