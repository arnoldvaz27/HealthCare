package com.arnold.nurses.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "nurses")
public class Nurse implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "designation")
    private String Designation;

    @ColumnInfo(name = "phone_number")
    private String PhoneNumber;

    @ColumnInfo(name = "email")
    private String Email;

    @ColumnInfo(name = "age")
    private String Age;

    @ColumnInfo(name = "gender")
    private String Gender;

    @ColumnInfo(name = "date_joining")
    private String DateJoining;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDateJoining() {
        return DateJoining;
    }

    public void setDateJoining(String dateJoining) {
        DateJoining = dateJoining;
    }
    @NonNull
    @Override
    public String toString() {
        return Name + " : " + Designation;
    }
}
