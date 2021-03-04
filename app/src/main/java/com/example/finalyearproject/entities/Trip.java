package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity (tableName = "trips_table")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private int tripId;

    //TODO: Figure out foreign keys.
//    private int userId;
    private String title;
    private String description;
    //TODO: Create location class.
//    private String location;
//    private Date startDate;
//    private Date endDate;
//    public ArrayList<ItineraryItem> itinerary;

    public Trip(String title, String description) {
        this.title = title;
        this.description = description;
//        this.location = location;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        itinerary = new ArrayList<ItineraryItem>();
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
