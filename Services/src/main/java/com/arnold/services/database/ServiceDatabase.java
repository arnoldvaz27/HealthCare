package com.arnold.services.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.services.dao.ServiceDao;
import com.arnold.services.entities.Service;

@Database(entities = Service.class, version = 1,exportSchema = false)
public abstract class ServiceDatabase extends RoomDatabase {
    private static ServiceDatabase serviceDatabase;

    public static synchronized ServiceDatabase getServiceDatabase(Context context)
    {
        if (serviceDatabase == null)
        {
            serviceDatabase = Room.databaseBuilder(
                    context, ServiceDatabase.class,"service_db"
            ).build();
        }
        return serviceDatabase;
    }

    public abstract ServiceDao serviceDao();
}
