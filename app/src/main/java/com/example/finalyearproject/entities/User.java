package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.time.OffsetDateTime;


@Entity(tableName = "users_table")
public class User {

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String firstName;
    private String lastName;
    private String emailAddress;
//    private boolean verified;
    //private OffsetDateTime dateJoined;
//    private Date dateVerified;

    private static final boolean DEFAULT_VERIFIED_STATUS = false;

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
//        verified = DEFAULT_VERIFIED_STATUS; //default value.
//        this.dateJoined = dateJoined;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

//    public boolean isVerified() {
//        return verified;
//    }
//
//    public Date getDateJoined() {
//        return dateJoined;
//    }
//
//    public Date getDateVerified() {
//        return dateVerified;
//    }
}
