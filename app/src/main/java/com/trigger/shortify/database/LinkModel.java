package com.trigger.shortify.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(indices = {@Index(value = {"short_url"},
        unique = true)})
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "original_url")
    private String originalURL;

    @ColumnInfo(name = "original_url_ellipsize")
    private String originalURLEllipsize;

    @ColumnInfo(name = "short_url")
    private String shortURL;

    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "starred")
    private boolean starred;

    public LinkModel(String originalURL, String originalURLEllipsize, String shortURL, boolean starred, Date date) {
        this.originalURL = originalURL;
        this.originalURLEllipsize = originalURLEllipsize;
        this.shortURL = shortURL;
        this.starred = starred;
        this.date = date;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public String getOriginalURLEllipsize() {return originalURLEllipsize;}

    public String getShortURL() {
        return shortURL;
    }

    public Integer getId() {
        return id;
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