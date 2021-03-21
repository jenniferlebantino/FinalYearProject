package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.TripAdapter;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.entities.User;
import com.example.finalyearproject.viewModel.TripViewModel;
import com.example.finalyearproject.viewModel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TripsFragment extends Fragment {
    public static final int ADD_TRIP_REQUEST = 1;
    private TripViewModel tripViewModel;
    private FloatingActionButton addTripBtn;

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

        RecyclerView recyclerView = v.findViewById(R.id.trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);

        final TripAdapter adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);

        tripViewModel = new ViewModelProvider(requireActivity()).get(TripViewModel.class);

        final Observer<List<Trip>> observer = new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
            }
        };

        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), observer);
        return v;
    }

    private void launchAddTrip() {
        Intent launchAddTripsActivity = new Intent(getActivity(), AddTripActivity.class);
        startActivityForResult(launchAddTripsActivity, ADD_TRIP_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TRIP_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddTripActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddTripActivity.EXTRA_DESCRIPTION);
            String startDate = data.getStringExtra(AddTripActivity.EXTRA_STARTDATE);
            String endDate = data.getStringExtra(AddTripActivity.EXTRA_ENDDATE);
            String imageUrl = data.getStringExtra(AddTripActivity.EXTRA_IMAGEURL);

            Trip trip = new Trip(title, description, startDate, endDate, imageUrl);
            tripViewModel.insert(trip);
            Toast.makeText(getActivity(), "Trip Saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Cannot Save Trip", Toast.LENGTH_SHORT).show();
        }
    }
}
