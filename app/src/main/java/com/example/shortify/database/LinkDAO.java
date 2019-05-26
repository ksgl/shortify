package com.example.shortify.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface LinkDAO {

    @Query("SELECT * FROM LinkModel ORDER BY date")
    LiveData<List<LinkModel>> getAll();

    @Query("SELECT * FROM LinkModel WHERE starred=1 ORDER BY date")
    LiveData<List<LinkModel>> getFavourites();

    @Insert(onConflict = REPLACE)
    void addLink(LinkModel link);

    @Update
    void updateStarred(LinkModel link);

    @Query("DELETE FROM LinkModel")
    void deleteAll();
}