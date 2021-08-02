package com.arnold.ambulances.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.ambulances.dao.AmbulanceDao;
import com.arnold.ambulances.entities.Ambulance;

@Database(entities = Ambulance.class, version = 1,exportSchema = false)
public abstract class AmbulanceDatabase extends RoomDatabase {
    private static AmbulanceDatabase ambulanceDatabase;

    public static synchronized AmbulanceDatabase getAmbulanceDatabase(Context context)
    {
        if (ambulanceDatabase == null)
        {
            ambulanceDatabase = Room.databaseBuilder(
                    context, AmbulanceDatabase.class,"ambulance_db"
            ).build();
        }
        return ambulanceDatabase;
    }

    public abstract AmbulanceDao ambulanceDao();
}
