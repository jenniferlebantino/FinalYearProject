package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTripActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "com.example.finalyearproject.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION=
            "com.example.finalyearproject.EXTRA_DESCRIPTION";

    private EditText titleTxtBox;
    private EditText descriptionTxtBox;
    private Button saveBtn;
    private Button closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        titleTxtBox = findViewById(R.id.addTrip_title);
        descriptionTxtBox = findViewById(R.id.addTrip_description);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Trip");

        saveBtn = (Button)findViewById(R.id.addTrip_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveTrip();
            }
        });

        closeBtn = (Button)findViewById(R.id.addTrip_closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchTrips();
            }
        });
    }

    private void SaveTrip() {
        String title = titleTxtBox.getText().toString();
        String description = descriptionTxtBox.getText().toString();

        if(title.trim().isEmpty() || description.trim().isEmpty())
        {
            Toast.makeText(this, "Please add a title and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent tripData = new Intent();
        tripData.putExtra(EXTRA_TITLE, title);
        tripData.putExtra(EXTRA_DESCRIPTION, description);

        setResult(RESULT_OK, tripData);
        finish();
    }

    private void launchTrips() {
        Intent launchTripsFragment= new Intent(AddTripActivity.this, TripsFragment.class);
        startActivity(launchTripsFragment);
    }




}