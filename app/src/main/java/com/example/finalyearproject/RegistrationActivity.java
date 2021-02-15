package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RegistrationActivity extends AppCompatActivity {
private ImageButton backToLoginBtn;
private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        backToLoginBtn = (ImageButton)findViewById(R.id.registration_backBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchLogin();
            }
        });

        submitBtn = (Button)findViewById(R.id.registration_submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchMain();
            }
        });
    }

    private void launchMain() {
        Intent launchMainActivity= new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(launchMainActivity);
    }

    private void launchLogin() {
        Intent launchLoginActivity= new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(launchLoginActivity);
    }


}