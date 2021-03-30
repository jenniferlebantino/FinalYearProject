package com.example.finalyearproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalyearproject.adapters.TripAdapter;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.viewModel.TripViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ViewTripFragment extends Fragment {
    private TripViewModel tripViewModel;
    private TripAdapter adapter;
    private View v;
    private ImageView tripImageView;
    private TextView titleTxtView;
    private TextView descriptionTxtView;
    private TextView datesTxtView;
    private int tripId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tripViewModel = new ViewModelProvider(requireActivity()).get(TripViewModel.class);
        adapter = new TripAdapter();
        v = inflater.inflate(R.layout.fragment_view_trip, container, false);
        titleTxtView = v.findViewById(R.id.viewTrip_TripTitle);
        descriptionTxtView = v.findViewById(R.id.viewTrip_tripDescription);
        datesTxtView = v.findViewById(R.id.viewTrip_tripDates);
        tripImageView = v.findViewById(R.id.viewTrip_tripImage);

/*
        final Observer<List<Trip>> observer = new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter.setTrips(trips);
            }
        };

        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), observer);

*/
        Bundle b = getArguments();

        String imageUrl = b.getString("imageUrl");
        if (imageUrl.equals("")) {
            tripImageView.setImageResource(R.drawable.im_no_image);
        } else {
            Picasso.get().load(imageUrl).fit().into(tripImageView);
        }

        titleTxtView.setText(b.getString("title"));
        descriptionTxtView.setText(b.getString("description"));
        datesTxtView.setText(b.getString("dates"));

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.view_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.viewMenu_delete:
//                deleteTrip();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteTrip() {
//        //String imageUrl = adapter.getTripById(tripId).getImageUrl();
//        //TODO: Delete images from firebase.
//        tripViewModel.delete(adapter.getTripById(tripId));
//        TripsFragment fragment = new TripsFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//    }
}