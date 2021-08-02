package com.arnold.canteen.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.canteen.dao.CanteenDao;
import com.arnold.canteen.entities.Canteens;

@Database(entities = Canteens.class, version = 1,exportSchema = false)
public abstract class CanteenDatabase extends RoomDatabase {
    private static CanteenDatabase canteenDatabase;

    public static synchronized CanteenDatabase getCanteenDatabase(Context context)
    {
        if (canteenDatabase == null)
        {
            canteenDatabase = Room.databaseBuilder(
                    context, CanteenDatabase.class,"canteen_db"
            ).build();
        }
        return canteenDatabase;
    }

    public abstract CanteenDao canteenDao();
}
