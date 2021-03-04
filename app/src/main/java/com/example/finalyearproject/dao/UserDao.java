package com.example.finalyearproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finalyearproject.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users_table ORDER BY lastName DESC")
    LiveData<List<User>> getAllUsers();
//
//    @Query("SELECT * FROM users_table")
//    void deleteAllUsers();
}
