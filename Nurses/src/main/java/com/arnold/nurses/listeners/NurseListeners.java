package com.arnold.nurses.listeners;


import com.arnold.doctors.entities.Doctors;
import com.arnold.nurses.entities.Nurse;

public interface NurseListeners {

    void onNurseClicked(Nurse note, int position);

}
