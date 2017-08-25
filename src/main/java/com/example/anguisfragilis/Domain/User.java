package com.example.anguisfragilis.Domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private List<Score> scoreList;
    private int userId;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.scoreList = new ArrayList<>();
    }

    public User(String userName, String password, int userId) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<Score> getScoreList() {
        return scoreList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
