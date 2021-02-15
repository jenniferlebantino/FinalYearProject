package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Button startBtn = (Button)findViewById(R.id.splash_startBtn);
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchLogin();
            }
        });
    }

    public void launchLogin() {
        Intent launchLoginActivity= new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(launchLoginActivity);
    }
}
