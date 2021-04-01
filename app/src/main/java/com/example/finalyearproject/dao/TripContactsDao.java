package com.example.finalyearproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.TripContacts;

import java.util.List;

@Dao
public interface TripContactsDao {
    @Insert
    void insert(TripContacts contact);

    @Update
    void update(TripContacts contact);

    @Delete
    void delete(TripContacts contact);

    @Query ("SELECT * FROM tripContacts_table ORDER BY tripContactId")
    LiveData<List<TripContacts>> getAllTripContacts();



//    @Query("SELECT contactId FROM tripContacts_table WHERE tripId LIKE '% :pTripId %' ORDER BY contactId")
//    LiveData<List<TripContacts>> getAllTripContactsByTripId(int pTripId);
    @RawQuery(observedEntities = TripContacts.class)
    LiveData<List<TripContacts>> getAllTripContactsByTripId(SupportSQLiteQuery query);
}
