package com.example.shortify.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String originalURL;
    private String shortURL;
    boolean starred;

    public LinkModel(String originalURL, String shortURL, boolean starred) {
        this.originalURL = originalURL;
        this.shortURL = shortURL;
        this.starred = starred;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public boolean getStarred() {
        return starred;
    }

    public void setStarred(boolean s) {
        this.starred = s;}
}