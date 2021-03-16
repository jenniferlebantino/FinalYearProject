package com.example.finalyearproject.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "images_table")
public class Image {
    @PrimaryKey(autoGenerate = true)
    private int imageId;

    private String imageDescription;
}
