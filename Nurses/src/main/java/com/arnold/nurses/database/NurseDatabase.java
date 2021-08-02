package com.arnold.nurses.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.nurses.dao.NursesDao;
import com.arnold.nurses.entities.Nurse;

@Database(entities = Nurse.class, version = 1,exportSchema = false)
public abstract class NurseDatabase extends RoomDatabase {
    private static NurseDatabase nurseDatabase;

    public static synchronized NurseDatabase getNurseDatabase(Context context)
    {
        if (nurseDatabase == null)
        {
            nurseDatabase = Room.databaseBuilder(
                    context,NurseDatabase.class,"nurses_db"
            ).build();
        }
        return nurseDatabase;
    }

    public abstract NursesDao nursesDao();
}
