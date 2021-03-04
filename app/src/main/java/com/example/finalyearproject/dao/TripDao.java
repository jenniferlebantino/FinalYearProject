package com.example.finalyearproject.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalyearproject.entities.Trip;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    void insert(Trip trip);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("SELECT * FROM trips_table ORDER BY title DESC")
    LiveData<List<Trip>> getAllTrips();

}
