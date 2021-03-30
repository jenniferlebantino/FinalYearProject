package com.example.finalyearproject.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.repository.TripRepository;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;
    private LiveData<List<Trip>> allTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
    }

    public void insert(Trip trip) {
        tripRepository.insert(trip);
    }

    public void update(Trip trip) {
        tripRepository.update(trip);
    }

    public void delete(Trip trip) {
        tripRepository.delete(trip);
    }

//    public void deleteAll(User user) {
//        userRepository.delete(user);
//    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    public Trip getTripById(int tripID) {
        return tripRepository.getTripById(tripID);
    }

}
