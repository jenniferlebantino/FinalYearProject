package com.example.finalyearproject.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.finalyearproject.dao.TripDao;
import com.example.finalyearproject.database.FYPAppDatabase;
import com.example.finalyearproject.entities.Trip;

import java.util.List;

public class TripRepository {
    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;

    public TripRepository(Application application) {
        FYPAppDatabase database = FYPAppDatabase.getInstance(application);
        tripDao = database.tripDao();
        allTrips = tripDao.getAllTrips();
    }

    public void insert(Trip trip) {
        new InsertTripAsyncTask(tripDao).execute(trip);
    }

    public void update(Trip trip) {
        new UpdateTripAsyncTask(tripDao).execute(trip);
    }

    public void delete(Trip trip) {
        new DeleteTripAsyncTask(tripDao).execute(trip);
    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    public Trip getTripById(int tripId) {
        String queryString = "SELECT * FROM trip_table WHERE tripId LIKE '" + Integer.toString(tripId) + "'";
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString);

        return tripDao.getTripById(query);
    }

    //Async tasks
    private static class InsertTripAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao tripDao;

        private InsertTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.insert(trips[0]);
            return null;
        }
    }

    private static class UpdateTripAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao tripDao;

        private UpdateTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.update(trips[0]);
            return null;
        }
    }

    private static class DeleteTripAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao tripDao;

        private DeleteTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.delete(trips[0]);
            return null;
        }
    }

}
