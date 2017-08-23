package com.example.anguisfragilis.Domain;

import java.util.Date;

public class Score {
    public final int score;
    public final Date entryDate;

    public Score(int score, Date entryDate) {
        this.score = score;
        this.entryDate = entryDate;
    }

    public int getScore() {
        return score;
    }

    public Date getEntryDate() {
        return entryDate;
    }
}
