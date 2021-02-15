package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button registerBtn;
    private Button loginBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn = (Button)findViewById(R.id.login_registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchRegistration();
            }
        });

        loginBtn = (Button)findViewById(R.id.login_loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchMain();
            }
        });
    }

    private void launchRegistration() {
        Intent launchRegistrationActivity= new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(launchRegistrationActivity);
    }

    private void launchMain() {
        Intent launchMainActivity= new Intent(LoginActivity.this, MainActivity.class);
        startActivity(launchMainActivity);
    }

}