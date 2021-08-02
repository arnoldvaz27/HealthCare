package com.arnold.patients.listeners;


import com.arnold.patients.entities.Patient;

public interface PatientListeners {

    void onPatientClicked(Patient note, int position);

}
