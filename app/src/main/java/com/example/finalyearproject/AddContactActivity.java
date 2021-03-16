package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    public static final String EXTRA_FIRSTNAME = "com.example.finalyearproject.EXTRA_FIRSTNAME";
    public static final String EXTRA_LASTNAME = "com.example.finalyearproject.EXTRA_LASTNAME";
    public static final String EXTRA_EMAILADDRESS = "com.example.finalyearproject.EXTRA_EMAILADDRESS";
    public static final String EXTRA_PHONENUMBER = "com.example.finalyearproject.EXTRA_PHONENUMBER";

    private EditText firstNameTxtBox;
    private EditText lastNameTxtBox;
    private EditText emailAddressTxtBox;
    private EditText phoneNumberTxtBox;
    private Button saveBtn;
    private Button closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        firstNameTxtBox = findViewById(R.id.addContact_firstName);
        lastNameTxtBox = findViewById(R.id.addContact_lastName);
        emailAddressTxtBox = findViewById(R.id.addContact_emailAddress);
        phoneNumberTxtBox = findViewById(R.id.addContact_phoneNumber);

        setTitle("Add Contact");

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

    private void SaveContact() {
        String firstName = firstNameTxtBox.getText().toString();
        String lastName = lastNameTxtBox.getText().toString();
        String emailAddress = lastNameTxtBox.getText().toString();
        String phoneNumber = phoneNumberTxtBox.getText().toString();

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

        setResult(RESULT_OK, contactData);
        finish();
    }

    private void launchContacts() {
        Intent launchTripsFragment= new Intent(AddContactActivity.this, ContactsFragment.class);
        startActivity(launchTripsFragment);
    }

}