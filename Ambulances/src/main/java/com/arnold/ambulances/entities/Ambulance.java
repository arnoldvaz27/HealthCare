package com.arnold.ambulances.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ambulance")
public class Ambulance implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "number_plate")
    private String NumberPlate;

    @ColumnInfo(name = "status")
    private String Status;

    @ColumnInfo(name = "persons")
    private String Persons;

    @ColumnInfo(name = "email")
    private String Email;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "phone_number")
    private String PhoneNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return NumberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        NumberPlate = numberPlate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPersons() {
        return Persons;
    }

    public void setPersons(String persons) {
        Persons = persons;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return NumberPlate + " : " + Status;
    }
}
