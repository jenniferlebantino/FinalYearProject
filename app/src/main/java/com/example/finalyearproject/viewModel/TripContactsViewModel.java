package com.example.finalyearproject.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.TripContacts;
import com.example.finalyearproject.repository.TripContactsRepository;

import java.util.List;

public class TripContactsViewModel extends AndroidViewModel {
    private TripContactsRepository repository;
    private LiveData<List<TripContacts>> allTripContacts;

    public TripContactsViewModel(@NonNull Application application) {
        super(application);
        repository = new TripContactsRepository(application);
        allTripContacts = repository.getAllTripContacts();
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

    public LiveData<List<TripContacts>> getAllTripContacts() {
        return allTripContacts;
    }
}
