package com.example.finalyearproject.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.finalyearproject.dao.TripDao;
import com.example.finalyearproject.dao.UserDao;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.User;

import java.util.Date;

@Database(entities = {User.class, Trip.class}, version = 3, exportSchema = false)
public abstract class FYPAppDatabase extends RoomDatabase {

    //Singleton pattern
    private static FYPAppDatabase instance;

    public abstract UserDao userDao();
    public abstract TripDao tripDao();

    //synchronised ensures that only one instance of this can be accessed even with multiple threads ongoing.
    public static synchronized FYPAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FYPAppDatabase.class, "fyp_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private UserDao userDao;
        private TripDao tripDao;

        private PopulateDbAsyncTask(FYPAppDatabase db) {
            userDao = db.userDao();
            tripDao = db.tripDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("Jennifer", "Lebantino", "test@example.com"));
            userDao.insert(new User("Jennifer2", "Lebantino2", "test2@example.com"));

            tripDao.insert(new Trip("Safari Adventure", "Safari with Rhea and Josh."));
            tripDao.insert(new Trip("Cottage Getaway", "Quiet retreat in Cornwall."));
            return null;
        }
    }

}
