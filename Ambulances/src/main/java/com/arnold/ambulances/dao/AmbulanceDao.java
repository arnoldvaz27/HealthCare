package com.arnold.ambulances.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.ambulances.entities.Ambulance;

import java.util.List;

@Dao
public interface AmbulanceDao {

    @Query("SELECT * FROM ambulance ORDER BY id DESC")
    List<Ambulance> getAllAmbulance();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAmbulance(Ambulance ambulance);

    @Delete
    void deleteAmbulance(Ambulance ambulance);
}
