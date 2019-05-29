package com.trigger.shortify.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface LinkDAO {

    @Query("SELECT * FROM LinkModel ORDER BY date DESC")
    LiveData<List<LinkModel>> getAll();

    @Query("SELECT * FROM LinkModel WHERE starred=1 ORDER BY starred, date")
    LiveData<List<LinkModel>> getFavourites();

    @Insert(onConflict = IGNORE)
    void addLink(LinkModel link);

    @Update
    void updateStarred(LinkModel link);

    @Delete
    void deleteLink(LinkModel link);

    @Query("DELETE FROM LinkModel")
    void deleteAll();
}