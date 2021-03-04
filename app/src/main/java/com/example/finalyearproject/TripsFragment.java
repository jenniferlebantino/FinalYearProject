package com.example.finalyearproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

public class TripsFragment extends Fragment {

    private TripViewModel tripViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trips, container, false);

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

}
