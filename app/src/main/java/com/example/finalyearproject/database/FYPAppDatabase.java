package com.example.finalyearproject.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.finalyearproject.dao.ContactDao;
import com.example.finalyearproject.dao.TripContactsDao;
import com.example.finalyearproject.dao.TripDao;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.TripContacts;

@Database(entities = {Trip.class, Contact.class, TripContacts.class}, version = 1, exportSchema = false)
public abstract class FYPAppDatabase extends RoomDatabase {

    //Singleton pattern
    private static FYPAppDatabase instance;

    public abstract TripDao tripDao();
    public abstract ContactDao contactDao();
    public abstract TripContactsDao tripContactsDao();

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
        private TripDao tripDao;
        private ContactDao contactDao;
        private TripContactsDao tripContactsDao;

        private PopulateDbAsyncTask(FYPAppDatabase db) {
            tripDao = db.tripDao();
            contactDao = db.contactDao();
            tripContactsDao = db.tripContactsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
