package com.example.shortify.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(indices = {@Index(value = {"shortURL"},
        unique = true)})
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String originalURL;
    private String shortURL;
//    public String date;
    @TypeConverters({TimestampConverter.class})
    public Date date;
    public boolean starred;

    public LinkModel(String originalURL, String shortURL, boolean starred, Date date) {
        this.id = id;
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

    public Integer getId() {
        return this.id;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm");
        return dateFormat.format(date);
    }

    public boolean getStarred() {
        return this.starred;
    }

    public void setStarred(boolean s) {
        this.starred = s;}
}