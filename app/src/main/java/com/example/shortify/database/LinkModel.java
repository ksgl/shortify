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
    private boolean starred;

    public LinkModel(String originalURL, String shortURL, boolean starred, String date) {
        this.originalURL = originalURL;
        this.shortURL = shortURL;
        this.starred = starred;
        this.date = date;
    }

    public String getOriginalURL() {
        return this.originalURL;
    }

    public String getShortURL() {
        return this.shortURL;
    }

    public String getDate() {
        return date;
    }

    public boolean getStarred() {
        return this.starred;
    }

    public void setStarred(boolean s) {
        this.starred = s;}
}