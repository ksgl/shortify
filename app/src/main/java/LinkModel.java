import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
@Entity
public class LinkModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String originalURL;
    private String shortURL;
    @TypeConverters(DateConverter.class)
    private Date date;
    boolean starred;

    public LinkModel(String originalURL, String shortURL, Date date, boolean starred) {
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

    public Date getDate() {
        return date;
    }

    public boolean getStarred() {
        return starred;
    }
}