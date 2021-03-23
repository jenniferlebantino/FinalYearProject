package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddContactActivity extends AppCompatActivity {
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
    private ImageView addContactImage;
    private Button chooseImageBtn;
    private Button saveBtn;
    private Button closeBtn;

    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        setTitle("Add Contact");

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        dbReference = FirebaseDatabase.getInstance().getReference("uploads");

        firstNameTxtBox = findViewById(R.id.addContact_firstName);
        lastNameTxtBox = findViewById(R.id.addContact_lastName);
        emailAddressTxtBox = findViewById(R.id.addContact_emailAddress);
        phoneNumberTxtBox = findViewById(R.id.addContact_phoneNumber);
        addContactImage = findViewById(R.id.addContact_contactImage);
        chooseImageBtn = findViewById(R.id.addContact_chooseImageBtn);

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageFileSelector();
            }
        });

        saveBtn = (Button)findViewById(R.id.addTrip_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveContact();
            }
        });

        closeBtn = (Button)findViewById(R.id.addTrip_closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchContacts();
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
            Picasso.get().load(imageUri).into(addContactImage);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void SaveContact() {
        String firstName = firstNameTxtBox.getText().toString();
        String lastName = lastNameTxtBox.getText().toString();
        String emailAddress = lastNameTxtBox.getText().toString();
        String phoneNumber = phoneNumberTxtBox.getText().toString();
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
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {

                        Uri downloadUri = task.getResult();
                        Upload upload = new Upload("", downloadUri.toString());

                        Log.e("AddContactActivity_Saving", "then: " + downloadUri.toString());
                        dbReference.push().setValue(upload);
                        Toast.makeText(AddContactActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddContactActivity.this, "Upload Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || emailAddress.trim().isEmpty() || phoneNumber.trim().isEmpty())
        {
            Toast.makeText(this, "Please add a title and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent contactData = new Intent();
        contactData.putExtra(EXTRA_FIRSTNAME, firstName);
        contactData.putExtra(EXTRA_LASTNAME, lastName);
        contactData.putExtra(EXTRA_EMAILADDRESS, emailAddress);
        contactData.putExtra(EXTRA_EMAILADDRESS, phoneNumber);
        contactData.putExtra(EXTRA_IMAGEURL, imageUrl);

        setResult(RESULT_OK, contactData);
        finish();
        new MailAsyncTask(AddContactActivity.this, EmailTypeEnum.ContactVerification, emailAddress, firstName).execute();
    }

    private void launchContacts() {
        Intent launchTripsFragment= new Intent(AddContactActivity.this, ContactsFragment.class);
        startActivity(launchTripsFragment);
    }
}