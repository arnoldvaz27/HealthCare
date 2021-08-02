package com.arnold.medicines.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.medicines.dao.MedicinesDao;
import com.arnold.medicines.entities.Medicine;

@Database(entities = Medicine.class, version = 1,exportSchema = false)
public abstract class MedicinesDatabase extends RoomDatabase {
    private static MedicinesDatabase medicinesDatabase;

    public static synchronized MedicinesDatabase getMedicinesDatabase(Context context)
    {
        if (medicinesDatabase == null)
        {
            medicinesDatabase = Room.databaseBuilder(
                    context, MedicinesDatabase.class,"medicines_db"
            ).build();
        }
        return medicinesDatabase;
    }

    public abstract MedicinesDao medicinesDao();
}
