package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_TRIPID = "com.example.finalyearproject.EXTRA_TRIPID";
    public static final String EXTRA_TITLE = "com.example.finalyearproject.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.finalyearproject.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARTDATE = "com.example.finalyearproject.EXTRA_STARTDATE";
    public static final String EXTRA_ENDDATE = "com.example.finalyearproject.EXTRA_ENDDATE";
    public static final String EXTRA_IMAGEURL = "com.example.finalyearproject.EXTRA_IMAGEURL";
    public static final String EXTRA_ITINERARY = "com.example.finalyearproject.EXTRA_ITINERARY";
    public static final String EXTRA_ADDEDITTRIP_SELECTEDCONTACTS = "com.example.finalyearproject.EXTRA_SELECTEDCONTACTS";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int SELECT_CONTACTS_REQUEST = 2;

    private EditText titleTxtBox;
    private EditText descriptionTxtBox;
    private FloatingActionButton saveBtn;
    private Button chooseImageBtn;
    private Button startDateBtn;
    private TextView startDateTxtView;
    private Button endDateBtn;
    private Button selectContactBtn;
    private boolean startDateBtnClicked;
    private TextView endDateTxtView;
    private ImageView tripImageView;
    private TextView example;
    private EditText itineraryTxtBox;

    private Uri imageUri;
    private List<Integer> selectedContacts = new ArrayList<>();
    private String selectedContactsString = "";

    private StorageReference reference;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        initialise();

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageFileSelector();
            }
        });

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                startDateBtnClicked = true;
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        selectContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditTripActivity.this, SelectContactsActivity.class);
                intent.putExtra(SelectContactsActivity.EXTRA_SELECTEDCONTACTS_CONTACTS, "");
                intent.putExtra(SelectContactsActivity.EXTRA_SELECTCONTACTS_NAMES, "");
                startActivityForResult(intent, SELECT_CONTACTS_REQUEST);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveTrip();
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_TRIPID)) {
            setTitle("Edit Trip");
            titleTxtBox.setText(intent.getStringExtra(EXTRA_TITLE));
            descriptionTxtBox.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            startDateTxtView.setText(intent.getStringExtra(EXTRA_STARTDATE));
            endDateTxtView.setText(intent.getStringExtra(EXTRA_ENDDATE));
            itineraryTxtBox.setText(intent.getStringExtra(EXTRA_ITINERARY));
            loadImage(EXTRA_IMAGEURL);
        }
        else {
            setTitle("Add New Trip");
        }
    }

    private void loadImage(String imageUrl) {
        if(imageUrl.equals("") || imageUrl == null) {
            tripImageView.setImageResource(R.drawable.im_no_image);
        }
        else {
            Picasso.get().load(imageUrl).into(tripImageView);
        }
    }

    private void initialise() {
        reference = FirebaseStorage.getInstance().getReference("uploads/");
        root = FirebaseDatabase.getInstance().getReference("uploads");

        tripImageView = findViewById(R.id.addTrip_tripImage);
        chooseImageBtn = findViewById(R.id.addTrip_chooseImageBtn);

        titleTxtBox = findViewById(R.id.addTrip_title);
        descriptionTxtBox = findViewById(R.id.addTrip_description);

        startDateBtn = findViewById(R.id.addTrip_startDateBtn);
        startDateTxtView = findViewById(R.id.addTrip_startDateTxt);

        endDateBtn = findViewById(R.id.addTrip_endDateBtn);
        endDateTxtView = findViewById(R.id.addTrip_endDateTxt);

        selectContactBtn = findViewById(R.id.addTrip_addContact);
        example = findViewById(R.id.example);

        itineraryTxtBox = findViewById(R.id.addTrip_addItinerary);

        saveBtn = findViewById(R.id.addTrip_saveBtn);
    }

    private void OpenImageFileSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null)
            {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(tripImageView);
            }
            else if (requestCode == SELECT_CONTACTS_REQUEST && resultCode == RESULT_OK) {
                selectedContactsString = data.getStringExtra(SelectContactsActivity.EXTRA_SELECTEDCONTACTS_CONTACTS);
                String contactNames = data.getStringExtra(SelectContactsActivity.EXTRA_SELECTCONTACTS_NAMES);

                String[] tokens = selectedContactsString.split(",");

                for(int i = 0; i<tokens.length; i++) {
                    selectedContacts.add(Integer.parseInt(tokens[i]));
                }

                if(!contactNames.isEmpty()) {
                    example.setText(contactNames);
                }
                else {
                    example.setText("No users selected.");
                }

            }
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void SaveTrip() {
        String title = titleTxtBox.getText().toString();
        String description = descriptionTxtBox.getText().toString();
        String startDate = startDateTxtView.getText().toString();
        String endDate = endDateTxtView.getText().toString();
        String itinerary = itineraryTxtBox.getText().toString();

        if(startDate.equals("Start Date") || endDate.equals("End Date"))
        {
            Toast.makeText(AddEditTripActivity.this, "Please select a start date and end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(title.trim().isEmpty())
        {
            titleTxtBox.setError("Please add a title.");
            return;
        }
        else if (description.trim().isEmpty()) {
            descriptionTxtBox.setError("Please add a description.");
            return;
        }

        if (imageUri != null)
        {
            final StorageReference fileReference = reference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Upload upload = new Upload("", uri.toString());
                                            String modelId = root.push().getKey();
                                            root.child(modelId).setValue(upload);

                                            Intent tripData = new Intent();
                                            tripData.putExtra(EXTRA_TITLE, title);
                                            tripData.putExtra(EXTRA_DESCRIPTION, description);
                                            tripData.putExtra(EXTRA_STARTDATE, startDate);
                                            tripData.putExtra(EXTRA_ENDDATE, endDate);
                                            tripData.putExtra(EXTRA_IMAGEURL, uri.toString());
                                            tripData.putExtra(EXTRA_ITINERARY, itinerary);
                                            tripData.putExtra(EXTRA_ADDEDITTRIP_SELECTEDCONTACTS, selectedContactsString);

                                            int id = getIntent().getIntExtra(EXTRA_TRIPID, -1);
                                            if (id != -1) {
                                                tripData.putExtra(EXTRA_TRIPID, id);
                                            }

                                            setResult(RESULT_OK, tripData);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("ImageUpload", "onFailure: getDownloadUrl()");
                                            Toast.makeText(AddEditTripActivity.this, "Could not retrieve image url.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ImageUpload", "onFailure: getPutFile()");
                            Toast.makeText(AddEditTripActivity.this, "Image upload failed. Could not save contact.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Intent tripData = new Intent();
            tripData.putExtra(EXTRA_TITLE, title);
            tripData.putExtra(EXTRA_DESCRIPTION, description);
            tripData.putExtra(EXTRA_STARTDATE, startDate);
            tripData.putExtra(EXTRA_ENDDATE, endDate);
            tripData.putExtra(EXTRA_IMAGEURL, "");
            tripData.putExtra(EXTRA_ITINERARY, itinerary);
            tripData.putExtra(EXTRA_ADDEDITTRIP_SELECTEDCONTACTS, selectedContactsString);

            int id = getIntent().getIntExtra(EXTRA_TRIPID, -1);
            if (id != -1) {
                tripData.putExtra(EXTRA_TRIPID, id);
            }

            setResult(RESULT_OK, tripData);
            finish();        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
        if(startDateBtnClicked)
        {
            startDateTxtView.setText(selectedDate);
            startDateBtnClicked = false;
        }
        else
        {
            endDateTxtView.setText(selectedDate);
        }
    }
}