package com.example.shortify.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String originalURL;
    private String shortURL;
    private String date;
    boolean starred;

    public LinkModel(String originalURL, String shortURL, String date, boolean starred) {
        this.originalURL = originalURL;
        this.shortURL = shortURL;
        this.date = date;
        this.starred = starred;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public String getDate() {
        return date;
    }

    public boolean getStarred() {
        return starred;
    }
}