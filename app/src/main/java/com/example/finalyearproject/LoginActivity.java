package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailAddressText;
    private EditText passwordText;
    private Button registerBtn;
    private Button loginBtn;

    private FirebaseAuth authenticate;
    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialise();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegistration();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginDetailsValid()) {
                    loginVerification();
                }
            }
        });
    }

    private void initialise() {
        authenticate = FirebaseAuth.getInstance();
        emailAddressText = findViewById(R.id.login_emailAddress);
        passwordText = findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_loginBtn);
        registerBtn = (Button) findViewById(R.id.login_registerBtn);
    }

    private boolean isLoginDetailsValid() {
        email = emailAddressText.getText().toString();
        password = passwordText.getText().toString();

        boolean valid = true;

        if (email.isEmpty()) {
            emailAddressText.setError("Please enter a valid email address.");
            valid = false;
        } else if (password.isEmpty()) {
            passwordText.setError("Please enter a valid password.");
            valid = false;
        }
        return valid;
    }

    private void loginVerification() {

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!password.isEmpty()) {
                authenticate.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Failed - Check Email/Password", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

    }

    private void launchRegistration() {
        Intent launchRegistrationActivity = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(launchRegistrationActivity);
    }

    private void launchMain() {
        Intent launchMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(launchMainActivity);
    }

}