package com.arnold.services.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.services.entities.Service;

import java.util.List;

@Dao
public interface ServiceDao {

    @Query("SELECT * FROM services ORDER BY id DESC")
    List<Service> getAllServices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertServices(Service ambulance);

    @Delete
    void deleteServices(Service ambulance);
}
