package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "contacts_table")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int contactId;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    public Contact(String firstName, String lastName, String emailAddress, String phoneNumber)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getContactId() {
        return contactId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
