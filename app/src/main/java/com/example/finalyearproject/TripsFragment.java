package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.adapters.TripAdapter;
import com.example.finalyearproject.adapters.TripContactsAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.TripContacts;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.example.finalyearproject.viewModel.TripContactsViewModel;
import com.example.finalyearproject.viewModel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TripsFragment extends Fragment {
    public static final int ADD_TRIP_REQUEST = 1;
    public static final int EDIT_TRIP_REQUEST = 2;

    private ContactViewModel contactViewModel;
    private TripViewModel tripViewModel;
    private TripContactsViewModel tripContactsViewModel;
    private TripAdapter adapter;
    private ContactAdapter contactAdapter;
    private TripContactsAdapter tripContactsAdapter;
    private FloatingActionButton addTripBtn;

    private List<String> selectedContacts = new ArrayList<>();
    private String selectedContactsString = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trips, container, false);

        addTripBtn = v.findViewById(R.id.trips_addBtn);
        addTripBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchAddTrip();
            }
        });

        final RecyclerView recyclerView = v.findViewById(R.id.trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);

        contactAdapter = new ContactAdapter();
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        final Observer<List<Contact>> contactObserver = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                contactAdapter.setContacts(contacts);
            }
        };
        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), contactObserver);

        tripContactsAdapter = new TripContactsAdapter();
        adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);

        tripViewModel = new ViewModelProvider(requireActivity()).get(TripViewModel.class);
        final Observer<List<Trip>> observer = new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
            }
        };
        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), observer);

        tripContactsViewModel = new ViewModelProvider(requireActivity()).get(TripContactsViewModel.class);
/*        final Observer<List<TripContacts>> tripContactsObserver = new Observer<List<TripContacts>>() {
            @Override
            public void onChanged(List<TripContacts> tripContacts) {
                //idk
            }
        };*/
/*
        tripContactsViewModel.getAllTripContacts().observe(getViewLifecycleOwner(), tripContactsObserver);
*/

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                tripViewModel.delete(adapter.getTripAt(viewHolder.getAdapterPosition()));
/*
                List<TripContacts> allTripContacts = tripContactsViewModel.getAllTripContacts().getValue();
                for(TripContacts tripContact : allTripContacts) {
                    if(tripContact.getTripId() == adapter.getTripAt(viewHolder.getAdapterPosition()).getTripId()) {
                        tripContactsViewModel.delete(tripContact);
                    }
                }
*/
                Toast.makeText(getContext(), "Trip successfully deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Intent intent = new Intent(getContext(), AddEditTripActivity.class);
                Trip trip = adapter.getTripAt(viewHolder.getAdapterPosition());
                intent.putExtra(AddEditTripActivity.EXTRA_TRIPID, trip.getTripId());
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.getTitle());
                intent.putExtra(AddEditTripActivity.EXTRA_DESCRIPTION, trip.getDescription());
                intent.putExtra(AddEditTripActivity.EXTRA_STARTDATE, trip.getStartDate());
                intent.putExtra(AddEditTripActivity.EXTRA_ENDDATE, trip.getEndDate());
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGEURL, trip.getImageUrl());
                intent.putExtra(AddEditTripActivity.EXTRA_SELECTEDCONTACTS, "");
                startActivityForResult(intent, EDIT_TRIP_REQUEST);
            }
        }).attachToRecyclerView(recyclerView);

        /*adapter.setOnItemClickListener(new TripAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trip trip) {
                Intent intent = new Intent(getContext(), AddEditTripActivity.class);
                intent.putExtra(AddEditTripActivity.EXTRA_TITLE, trip.getTitle());
                intent.putExtra(AddEditTripActivity.EXTRA_DESCRIPTION, trip.getDescription());
                intent.putExtra(AddEditTripActivity.EXTRA_STARTDATE, trip.getStartDate());
                intent.putExtra(AddEditTripActivity.EXTRA_ENDDATE, trip.getEndDate());
                intent.putExtra(AddEditTripActivity.EXTRA_IMAGEURL, trip.getImageUrl());
                intent.putExtra(AddEditTripActivity.EXTRA_SELECTEDCONTACTS, "");
                startActivityForResult(intent, EDIT_TRIP_REQUEST);
            }
        });*/

        return v;
    }

    private void launchAddTrip() {
        Intent launchAddTripsActivity = new Intent(getActivity(), AddEditTripActivity.class);
        startActivityForResult(launchAddTripsActivity, ADD_TRIP_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TRIP_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTripActivity.EXTRA_DESCRIPTION);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_STARTDATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_ENDDATE);
            String imageUrl = data.getStringExtra(AddEditTripActivity.EXTRA_IMAGEURL);

            Trip trip = new Trip(title, description, startDate, endDate, imageUrl);
            tripViewModel.insert(trip);

            selectedContactsString = data.getStringExtra(SelectContactsActivity.EXTRA_SELECTEDCONTACTS_CONTACTS);
            if(!selectedContactsString.equals("")) {
                selectedContacts = Arrays.asList(selectedContactsString.split(","));
                for (String contactId : selectedContacts) {
                    TripContacts tripContact = new TripContacts(trip.getTripId(), Integer.parseInt(contactId));
                    tripContactsViewModel.insert(tripContact);
                    Contact contact = contactAdapter.getContactById(Integer.parseInt(contactId));

                    MailAsyncTask mailTask = new MailAsyncTask(getContext(), EmailTypeEnum.TripInformation, contact.getEmailAddress(), contact.getFirstName());
                    mailTask.setTripInformation(trip);
                    mailTask.execute();
                }
            }

            Toast.makeText(getActivity(), "Trip Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_TRIP_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTripActivity.EXTRA_TRIPID, -1);
            if(id == -1) {
                Toast.makeText(getActivity(), "Trip couldn't be updated.", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditTripActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTripActivity.EXTRA_DESCRIPTION);
            String startDate = data.getStringExtra(AddEditTripActivity.EXTRA_STARTDATE);
            String endDate = data.getStringExtra(AddEditTripActivity.EXTRA_ENDDATE);
            String imageUrl = data.getStringExtra(AddEditTripActivity.EXTRA_IMAGEURL);

            Trip trip = new Trip(title, description, startDate, endDate, imageUrl);
            tripViewModel.update(trip);

            selectedContactsString = data.getStringExtra(SelectContactsActivity.EXTRA_SELECTEDCONTACTS_CONTACTS);
            if(!selectedContactsString.equals("")) {
                selectedContacts = Arrays.asList(selectedContactsString.split(","));
                for(String contactId : selectedContacts)
                {
                    TripContacts tripContact = new TripContacts(trip.getTripId(), Integer.parseInt(contactId));
                    tripContactsViewModel.update(tripContact);
                }
            }

            Toast.makeText(getActivity(), "Trip has been updated.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Cannot Save Trip", Toast.LENGTH_SHORT).show();
        }
    }
}