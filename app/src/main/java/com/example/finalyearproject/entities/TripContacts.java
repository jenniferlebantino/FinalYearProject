package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tripContacts_table")
public class TripContacts {

    @PrimaryKey(autoGenerate = true)
    private int tripContactId;

    private int tripId;
    private int contactId;

    public TripContacts(int tripId, int contactId) {
        this.tripId = tripId;
        this.contactId = contactId;
    }

    public int getTripContactId() {
        return tripContactId;
    }

    public void setTripContactId(int tripContactId) {
        this.tripContactId = tripContactId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
