package com.arnold.canteen.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnold.canteen.entities.Canteens;

import java.util.List;

@Dao
public interface CanteenDao {

    @Query("SELECT * FROM canteen ORDER BY id DESC")
    List<Canteens> getAllFood();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCanteens(Canteens ambulance);

    @Delete
    void deleteCanteens(Canteens ambulance);
}
