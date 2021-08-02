package com.arnold.doctors.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.doctors.dao.DoctorsDao;
import com.arnold.doctors.entities.Doctors;

@Database(entities = Doctors.class, version = 1,exportSchema = false)
public abstract class DoctorsDatabase extends RoomDatabase {
    private static DoctorsDatabase doctorsDatabase;

    public static synchronized DoctorsDatabase getDoctorsDatabase(Context context)
    {
        if (doctorsDatabase == null)
        {
            doctorsDatabase = Room.databaseBuilder(
                    context,DoctorsDatabase.class,"doctors_db"
            ).build();
        }
        return doctorsDatabase;
    }

    public abstract DoctorsDao doctorsDao();
}
