package com.example.finalyearproject;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.TripAdapter;
import com.example.finalyearproject.entities.User;
import com.example.finalyearproject.viewModel.UserViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
//
//        RecyclerView recyclerView = v.findViewById(R.id.home_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        recyclerView.setHasFixedSize(true);
//
//        final TripAdapter adapter = new TripAdapter();
//        recyclerView.setAdapter(adapter);
//
//        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
//
//        final Observer<List<User>> observer = new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                adapter.setTrips(users);
//            }
//        };
//
//        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), observer);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}