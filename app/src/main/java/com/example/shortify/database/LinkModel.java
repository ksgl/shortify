package com.example.shortify.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"short_url"},
        unique = true)})
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "original_url")
    private String originalURL;

    @ColumnInfo(name = "short_url")
    private String shortURL;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "starred")
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
        return this.date;
    }

    public boolean getStarred() {
        return this.starred;
    }

    public void setStarred(boolean s) {
        this.starred = s;}
}