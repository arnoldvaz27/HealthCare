package com.arnold.patients.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnold.patients.dao.PatientDao;
import com.arnold.patients.entities.Patient;

@Database(entities = Patient.class, version = 1,exportSchema = false)
public abstract class PatientsDatabase extends RoomDatabase {
    private static PatientsDatabase patientsDatabase;

    public static synchronized PatientsDatabase getPatientsDatabase(Context context)
    {
        if (patientsDatabase == null)
        {
            patientsDatabase = Room.databaseBuilder(
                    context, PatientsDatabase.class,"patients_db"
            ).build();
        }
        return patientsDatabase;
    }

    public abstract PatientDao patientDao();
}
