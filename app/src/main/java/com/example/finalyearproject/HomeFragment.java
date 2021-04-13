package com.example.finalyearproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private Button sosBtn;
    private TextView emergencyServiceText;

    private GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initialise
        sosBtn = v.findViewById(R.id.home_sosBtn);
        emergencyServiceText = v.findViewById(R.id.home_emergencyServiceText);
        final ContactAdapter adapter = new ContactAdapter();
        ContactViewModel contactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };
        contactVM.getAllContacts().observe(getViewLifecycleOwner(), observer);

/*
        sosBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MailAsyncTask mail = new MailAsyncTask(getContext(), EmailTypeEnum.SafetyAlert, "", "");
                mail.setSafetyInformation("", adapter.getContactEmails());
                Toast.makeText(getContext(), "SOS Alert Sent", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
*/

        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailAsyncTask mail = new MailAsyncTask(getContext(), EmailTypeEnum.SafetyAlert, adapter.getContactEmails(), "");
                mail.setSafetyInformation("Jennifer", "");
                mail.execute();
                Toast.makeText(getContext(), "SOS Alert Sent", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng DefaultLocation = new LatLng(53.40873146283257, -3.004984979067241);
        map.addMarker(new MarkerOptions().position(DefaultLocation).title("Liverpool"));
        map.moveCamera(CameraUpdateFactory.newLatLng(DefaultLocation));
    }
}