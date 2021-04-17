package com.example.finalyearproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final int GPS_REQUEST = 9001;

    private FloatingActionButton sosBtn;
    private TextView emergencyServiceText;
    private String locationCoordinates = "Location Unavailable";

    private GoogleMap map;
    private boolean isLocationPermissionGranted;
    private FusedLocationProviderClient locationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initialise
        sosBtn = v.findViewById(R.id.home_sosBtn);
        emergencyServiceText = v.findViewById(R.id.home_emergencyContact);
        checkPermission();
        initMap();
        locationProviderClient = new FusedLocationProviderClient(getActivity());
        getCurrentLocation();

        final ContactAdapter adapter = new ContactAdapter();
        ContactViewModel contactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };
        contactVM.getAllContacts().observe(getViewLifecycleOwner(), observer);

        sosBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!adapter.getContactEmails().isEmpty()) {
                    MailAsyncTask mail = new MailAsyncTask(getContext(), EmailTypeEnum.SafetyAlert, adapter.getContactEmails(), "");
                    getLocationString();
                    mail.setSafetyInformation("Jennifer", locationCoordinates);
                    mail.execute();
                    Toast.makeText(getContext(), "SOS Alert Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No contacts to alert.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        /*showLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });*/

/*
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailAsyncTask mail = new MailAsyncTask(getContext(), EmailTypeEnum.SafetyAlert, adapter.getContactEmails(), "");
                mail.setSafetyInformation("Jennifer", "");
                mail.execute();
                Toast.makeText(getContext(), "SOS Alert Sent", Toast.LENGTH_SHORT).show();
            }
        });
*/

        return v;
    }

    private void initMap() {
        if (isLocationPermissionGranted) {
            if (isGPSEnabled()) {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_mapFragment);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                    //mapFragment.onCreate(savedInstanceState);
                }
            }
        }
    }

    private boolean isGPSEnabled() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean providerEnable = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnable) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("GPS Permission")
                    .setMessage("Enable GPS to use this app.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST);
                    }))
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        showCurrentLocation(location.getLatitude(), location.getLongitude());
                        locationCoordinates = location.getLatitude() + ", " + location.getLongitude();
                    } else {
                        Toast.makeText(getContext(), "Could Not Show Location", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        map.moveCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @SuppressLint("MissingPermission")
    private void getLocationString() {
        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Location currentLocation = task.getResult();
                        locationCoordinates = currentLocation.getLatitude() + ", " + currentLocation.getLongitude();
                    }
                }
        );
    }

    private void checkPermission() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isLocationPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
/*
        LatLng DefaultLocation = new LatLng(53.40873146283257, -3.004984979067241);
        map.addMarker(new MarkerOptions().position(DefaultLocation).title("Liverpool"));
        map.moveCamera(CameraUpdateFactory.newLatLng(DefaultLocation));
*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST) {
            LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            boolean providerEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (providerEnabled) {
                Toast.makeText(getActivity(), "GPS is Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Cannot log your location. GPS is disabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
