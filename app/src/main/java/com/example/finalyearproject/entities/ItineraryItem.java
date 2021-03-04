package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "itineraryItem_table")
public class ItineraryItem {
    @PrimaryKey(autoGenerate = true)
    private int itineraryItemID;
    //ForeignKey
    private int tripID;

    private String itemTitle;
    private String itemDescription;
    private int position;
    private Date dateCreated;
    private Date startDate;
    private Date endDate;

    public ItineraryItem(String itemTitle, String itemDescription, Date dateCreated, Date startDate, Date endDate) {
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.dateCreated = dateCreated;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getPosition() {
        return position;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
