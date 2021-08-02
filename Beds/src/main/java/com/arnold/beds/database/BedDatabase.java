package com.arnold.beds.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.beds.dao.BedDao;
import com.arnold.beds.entities.Bed;

@Database(entities = Bed.class, version = 1,exportSchema = false)
public abstract class BedDatabase extends RoomDatabase {
    private static BedDatabase bedDatabase;

    public static synchronized BedDatabase getBedDatabase(Context context)
    {
        if (bedDatabase == null)
        {
            bedDatabase = Room.databaseBuilder(
                    context, BedDatabase.class,"bed_db"
            ).build();
        }
        return bedDatabase;
    }

    public abstract BedDao bedDao();
}
