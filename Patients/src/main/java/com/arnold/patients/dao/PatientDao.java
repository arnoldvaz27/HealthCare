package com.arnold.patients.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.patients.entities.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Query("SELECT * FROM patients ORDER BY id DESC")
    List<Patient> getAllPatient();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPatient(Patient ambulance);

    @Delete
    void deletePatient(Patient ambulance);
}
