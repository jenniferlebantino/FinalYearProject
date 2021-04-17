package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddEditContactActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACTID = "com.example.finalyearproject.EXTRA_CONTACTID";
    public static final String EXTRA_FIRSTNAME = "com.example.finalyearproject.EXTRA_FIRSTNAME";
    public static final String EXTRA_LASTNAME = "com.example.finalyearproject.EXTRA_LASTNAME";
    public static final String EXTRA_EMAILADDRESS = "com.example.finalyearproject.EXTRA_EMAILADDRESS";
    public static final String EXTRA_PHONENUMBER = "com.example.finalyearproject.EXTRA_PHONENUMBER";
    public static final String EXTRA_IMAGEURL = "com.example.finalyearproject.EXTRA_IMAGEURL";
    public static final int PICK_IMAGE_REQUEST = 1;

    private EditText firstNameTxtBox;
    private EditText lastNameTxtBox;
    private EditText emailAddressTxtBox;
    private EditText phoneNumberTxtBox;
    private ImageView contactImageView;
    private Button chooseImageBtn;
    private FloatingActionButton saveBtn;

    private Uri imageUri;
    private StorageReference reference;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getSupportActionBar().setIcon(R.drawable.ic_close);

        root = FirebaseDatabase.getInstance().getReference("Images");
        reference = FirebaseStorage.getInstance().getReference("uploads/");

        firstNameTxtBox = findViewById(R.id.addContact_firstName);
        lastNameTxtBox = findViewById(R.id.addContact_lastName);
        emailAddressTxtBox = findViewById(R.id.addContact_emailAddress);
        phoneNumberTxtBox = findViewById(R.id.addContact_phoneNumber);
        contactImageView = findViewById(R.id.addContact_contactImage);
        chooseImageBtn = findViewById(R.id.addContact_chooseImageBtn);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_CONTACTID)) {
            setTitle("Edit Contact");

            firstNameTxtBox.setText(intent.getStringExtra(EXTRA_FIRSTNAME));
            lastNameTxtBox.setText(intent.getStringExtra(EXTRA_LASTNAME));
            emailAddressTxtBox.setText(intent.getStringExtra(EXTRA_EMAILADDRESS));
            phoneNumberTxtBox.setText(intent.getStringExtra(EXTRA_PHONENUMBER));
            loadImage(EXTRA_IMAGEURL);
        } else {
            setTitle("Add Contact");
        }

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageFileSelector();
            }
        });

        saveBtn = findViewById(R.id.addTrip_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveContact();
            }
        });

    }

    private void OpenImageFileSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void loadImage(String imageUrl) {
        if (imageUrl.equals("") || imageUrl == null) {
            contactImageView.setImageResource(R.drawable.im_no_image);
        } else {
            Picasso.get().load(imageUrl).into(contactImageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(contactImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void SaveContact() {
        String firstName = firstNameTxtBox.getText().toString();
        String lastName = lastNameTxtBox.getText().toString();
        String emailAddress = emailAddressTxtBox.getText().toString();
        String phoneNumber = phoneNumberTxtBox.getText().toString() == null ? "" : phoneNumberTxtBox.getText().toString();

        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || emailAddress.trim().isEmpty() || phoneNumber.trim().isEmpty()) {
            Toast.makeText(AddEditContactActivity.this, "Please add a first name and last name", Toast.LENGTH_LONG).show();
            return;
        }

        if (imageUri != null) {
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

                                            Intent contactData = new Intent();
                                            contactData.putExtra(EXTRA_FIRSTNAME, firstName);
                                            contactData.putExtra(EXTRA_LASTNAME, lastName);
                                            contactData.putExtra(EXTRA_EMAILADDRESS, emailAddress);
                                            contactData.putExtra(EXTRA_PHONENUMBER, phoneNumber);
                                            contactData.putExtra(EXTRA_IMAGEURL, uri.toString());

                                            int id = getIntent().getIntExtra(EXTRA_CONTACTID, -1);
                                            if (id != -1) {
                                                contactData.putExtra(EXTRA_CONTACTID, id);
                                            }
                                            List<String> recipients = new ArrayList<>();
                                            recipients.add(emailAddress);
                                            new MailAsyncTask(AddEditContactActivity.this, EmailTypeEnum.ContactVerification, recipients, firstName).execute();

                                            setResult(RESULT_OK, contactData);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("ImageUpload", "onFailure: getDownloadUrl()");
                                            Toast.makeText(AddEditContactActivity.this, "Didn't work.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ImageUpload", "onFailure: getPutFile()");
                            Toast.makeText(AddEditContactActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Intent contactData = new Intent();
            contactData.putExtra(EXTRA_FIRSTNAME, firstName);
            contactData.putExtra(EXTRA_LASTNAME, lastName);
            contactData.putExtra(EXTRA_EMAILADDRESS, emailAddress);
            contactData.putExtra(EXTRA_PHONENUMBER, phoneNumber);
            contactData.putExtra(EXTRA_IMAGEURL,"");

            int id = getIntent().getIntExtra(EXTRA_CONTACTID, -1);
            if (id != -1) {
                contactData.putExtra(EXTRA_CONTACTID, id);
            }
            List<String> recipients = new ArrayList<>();
            recipients.add(emailAddress);
            new MailAsyncTask(AddEditContactActivity.this, EmailTypeEnum.ContactVerification, recipients, firstName).execute();

            setResult(RESULT_OK, contactData);
            finish();
        }
    }
}