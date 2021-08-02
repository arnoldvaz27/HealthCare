package com.arnold.nurses.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.nurses.entities.Nurse;

import java.util.List;

@Dao
public interface NursesDao {

    @Query("SELECT * FROM nurses ORDER BY id DESC")
    List<Nurse> getAllNurses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNurse(Nurse nurse);

    @Delete
    void deleteNurse(Nurse nurse);
}
