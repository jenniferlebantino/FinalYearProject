package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "trips_table")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    private int tripId;

    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String imageUrl;
    private String itinerary;

    //TODO: Create location class.
//    private String location;
//    public ArrayList<ItineraryItem> itinerary;

    public Trip(String title, String description, String startDate, String endDate, String imageUrl, String itinerary) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.itinerary = itinerary;
//        this.location = location;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }
}
