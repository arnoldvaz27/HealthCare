package com.arnold.beds.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.beds.entities.Bed;

import java.util.List;

@Dao
public interface BedDao {

    @Query("SELECT * FROM beds ORDER BY id DESC")
    List<Bed> getAllBed();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBed(Bed ambulance);

    @Delete
    void deleteBed(Bed ambulance);
}
