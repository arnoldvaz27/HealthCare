package com.arnold.patients.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "patients")
public class Patient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "patient_number")
    private String PatientNumber;

    @ColumnInfo(name = "patient_name")
    private String PatientName;

    @ColumnInfo(name = "disease")
    private String Disease;

    @ColumnInfo(name = "appointed_doctor")
    private String AppointedDoctor;

    @ColumnInfo(name = "status")
    private String Status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientNumber() {
        return PatientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        PatientNumber = patientNumber;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getAppointedDoctor() {
        return AppointedDoctor;
    }

    public void setAppointedDoctor(String appointedDoctor) {
        AppointedDoctor = appointedDoctor;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return PatientName + " : " + Disease;
    }
}
