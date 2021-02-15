package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button registrationBtn = (Button)findViewById(R.id.login_registerBtn);
        registrationBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchRegistration();
            }
        });

    }

    private void launchRegistration() {
        Intent launchRegistrationActivity= new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(launchRegistrationActivity);
    }
}