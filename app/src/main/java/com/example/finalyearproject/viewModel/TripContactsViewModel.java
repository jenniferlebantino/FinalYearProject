package com.example.finalyearproject.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.TripContacts;
import com.example.finalyearproject.repository.TripContactsRepository;

import java.util.List;

public class TripContactsViewModel extends AndroidViewModel {
    private TripContactsRepository repository;

    public TripContactsViewModel(@NonNull Application application) {
        super(application);
        repository = new TripContactsRepository(application);
    }

    public void insert(TripContacts contact) {
        repository.insert(contact);
    }

    public void update(TripContacts contact) {
        repository.update(contact);
    }

    public void delete(TripContacts contact) {
        repository.delete(contact);
    }

    public LiveData<List<TripContacts>> getAllTripContactsByTripID(int tripId) {
        return repository.getAllTripContactTrips(tripId);
    }
}
