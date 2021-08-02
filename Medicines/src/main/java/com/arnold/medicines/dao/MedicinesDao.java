package com.arnold.medicines.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.medicines.entities.Medicine;

import java.util.List;

@Dao
public interface MedicinesDao {

    @Query("SELECT * FROM medicines ORDER BY id DESC")
    List<Medicine> getAllMedicine();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedicine(Medicine ambulance);

    @Delete
    void deleteMedicine(Medicine ambulance);
}
