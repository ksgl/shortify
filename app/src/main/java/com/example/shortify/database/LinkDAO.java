package com.example.shortify.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(DateConverter.class)
public interface LinkDAO {

    @Query("SELECT * FROM LinkModel ORDER BY starred, date")
    LiveData<List<LinkModel>> getAll();

    @Insert(onConflict = REPLACE)
    void addLink(LinkModel link);

    @Query("DELETE FROM LinkModel")
    void deleteAll();
}