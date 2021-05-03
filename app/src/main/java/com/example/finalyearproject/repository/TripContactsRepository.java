package com.example.finalyearproject.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.finalyearproject.dao.TripContactsDao;
import com.example.finalyearproject.dao.TripDao;
import com.example.finalyearproject.database.FYPAppDatabase;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.TripContacts;

import java.util.List;

public class TripContactsRepository {
    private TripContactsDao tripContactsDao;
    private LiveData<List<TripContacts>> allTripContacts;

    public TripContactsRepository(Application application) {
        FYPAppDatabase database = FYPAppDatabase.getInstance(application);
        tripContactsDao = database.tripContactsDao();
        allTripContacts = tripContactsDao.getAllTripContacts();
    }

    public void insert(TripContacts tripContact) { new InsertTripContactAsyncTask(tripContactsDao).execute(tripContact); }

    public void update(TripContacts tripContact) { new UpdateTripContactAsyncTask(tripContactsDao).execute(tripContact);}

    public void delete(TripContacts tripContact) { new DeleteTripContactAsyncTask(tripContactsDao).execute(tripContact);}

    public LiveData<List<TripContacts>> getAllTripContacts() {
        return allTripContacts;
    }

    //Async Tasks
    private static class InsertTripContactAsyncTask extends AsyncTask<TripContacts, Void, Void> {

        private TripContactsDao tripContactsDao;

        private InsertTripContactAsyncTask(TripContactsDao tripContactsDao) {
            this.tripContactsDao = tripContactsDao;
        }

        @Override
        protected Void doInBackground(TripContacts... tripContacts) {
            tripContactsDao.insert(tripContacts[0]);
            return null;
        }
    }

    private static class UpdateTripContactAsyncTask extends AsyncTask<TripContacts, Void, Void> {

        private TripContactsDao tripContactsDao;

        private UpdateTripContactAsyncTask(TripContactsDao tripContactsDao) {
            this.tripContactsDao = tripContactsDao;
        }

        @Override
        protected Void doInBackground(TripContacts... tripContacts) {
            tripContactsDao.update(tripContacts[0]);
            return null;
        }
    }

    private static class DeleteTripContactAsyncTask extends AsyncTask<TripContacts, Void, Void> {

        private TripContactsDao tripContactsDao;

        private DeleteTripContactAsyncTask(TripContactsDao tripContactsDao) {
            this.tripContactsDao = tripContactsDao;
        }

        @Override
        protected Void doInBackground(TripContacts... tripContacts) {
            tripContactsDao.delete(tripContacts[0]);
            return null;
        }
    }



}
