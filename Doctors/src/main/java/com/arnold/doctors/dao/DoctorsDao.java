package com.arnold.doctors.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.doctors.entities.Doctors;

import java.util.List;

@Dao
public interface DoctorsDao {

    @Query("SELECT * FROM doctors ORDER BY id DESC")
    List<Doctors> getAllDoctors();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDoctor(Doctors doctors);

    @Delete
    void deleteDoctor(Doctors doctors);
}
