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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_TITLE = "com.example.finalyearproject.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.finalyearproject.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARTDATE = "com.example.finalyearproject.EXTRA_STARTDATE";
    public static final String EXTRA_ENDDATE = "com.example.finalyearproject.EXTRA_ENDDATE";
    public static final String EXTRA_IMAGEURL = "com.example.finalyearproject.EXTRA_IMAGEURL";
    public static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleTxtBox;
    private EditText descriptionTxtBox;
    private Button saveBtn;
    private Button closeBtn;
    private Button chooseImageBtn;
    private Button startDateBtn;
    private TextView startDateTxtView;
    private Button endDateBtn;
    private boolean startDateBtnClicked;
    private TextView endDateTxtView;
    private ImageView tripImageView;
    private ProgressBar progressBar;

    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        setTitle("Add Trip");
        progressBar = findViewById(R.id.addTrip_progressBar);
        tripImageView = findViewById(R.id.addTrip_tripImage);
        chooseImageBtn = findViewById(R.id.addTrip_chooseImageBtn);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        dbReference = FirebaseDatabase.getInstance().getReference("uploads");

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageFileSelector();
            }
        });

        titleTxtBox = findViewById(R.id.addTrip_title);
        descriptionTxtBox = findViewById(R.id.addTrip_description);

        startDateBtn = findViewById(R.id.addTrip_startDateBtn);
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                startDateBtnClicked = true;
            }
        });
        startDateTxtView = findViewById(R.id.addTrip_startDateTxt);

        endDateBtn = findViewById(R.id.addTrip_endDateBtn);
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        endDateTxtView = findViewById(R.id.addTrip_endDateTxt);

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

    private void OpenImageFileSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(tripImageView);
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
        String imageUrl = imageUri == null ? "": imageUri.toString();

        if (imageUri != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                        Uri downloadUri = task.getResult();
                        Upload upload = new Upload(titleTxtBox.getText().toString().trim(), downloadUri.toString());

                        Log.e("AddTripActivity_Saving", "then: " + downloadUri.toString());
                        dbReference.push().setValue(upload);
                        Toast.makeText(AddTripActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddTripActivity.this, "Upload Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(startDate.equals("Start Date") || endDate.equals("End Date"))
        {
            Toast.makeText(AddTripActivity.this, "Please select a start date and end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(title.trim().isEmpty() || description.trim().isEmpty())
        {
            Toast.makeText(AddTripActivity.this, "Please add a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent tripData = new Intent();
        tripData.putExtra(EXTRA_TITLE, title);
        tripData.putExtra(EXTRA_DESCRIPTION, description);
        tripData.putExtra(EXTRA_STARTDATE, startDate);
        tripData.putExtra(EXTRA_ENDDATE, endDate);
        tripData.putExtra(EXTRA_IMAGEURL, imageUrl);

        setResult(RESULT_OK, tripData);
        finish();
    }

    private void launchTrips() {
        Intent launchTripsFragment= new Intent(AddTripActivity.this, TripsFragment.class);
        startActivity(launchTripsFragment);
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