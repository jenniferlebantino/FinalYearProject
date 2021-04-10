package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.VISIBLE;

public class RegistrationActivity extends AppCompatActivity {
    private Button submitBtn;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailAddressText;
    private EditText confirmEmailAddressText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private TextView registrationErrorTxtView;

    private FirebaseAuth authenticate;
    private String emailAddress;
    private String confirmEmailAddress;
    private String password;
    private String confirmPassword;
    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialise();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            authenticate.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            registrationErrorTxtView.setVisibility(VISIBLE);
        }
    }

    private void initialise() {
        authenticate = FirebaseAuth.getInstance();
        firstNameText = findViewById(R.id.registration_firstNameInput);
        lastNameText = findViewById(R.id.registration_lastNameInput);
        emailAddressText = findViewById(R.id.registration_emailInput);
        confirmEmailAddressText = findViewById(R.id.registration_confirmEmailInput);
        passwordText = findViewById(R.id.registration_passwordInput);
        registrationErrorTxtView = findViewById(R.id.registration_error);
        confirmPasswordText = findViewById(R.id.registration_confirmPasswordInput);
        submitBtn = (Button) findViewById(R.id.registration_submitBtn);
    }

    private boolean isValid() {

        firstName = firstNameText.getText().toString();
        emailAddress = emailAddressText.getText().toString();
        confirmEmailAddress = confirmEmailAddressText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();

        boolean valid = true;

        if (firstName.isEmpty()) {
            firstNameText.setError("First name required.");
            valid = false;
        } else if (emailAddress.isEmpty()) {
            emailAddressText.setError("Email address required.");
            valid = false;
        } else if (confirmEmailAddress.isEmpty()) {
            confirmEmailAddressText.setError("Email address required.");
            valid = false;
        } else if (!emailAddress.equals(confirmEmailAddress)) {
            confirmEmailAddressText.setError("Email addresses do not match.");
            valid = false;
        } else if (!password.isEmpty() && password.length() >= 8 && isValidPassword(password)) {
            passwordText.setError("Password must contain 8 characters, 1 capital letter, and 1 special character.");
            valid = false;
        } else if (password.isEmpty()) {
            passwordText.setError("Please enter a password.");
            valid = false;
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordText.setError("Please enter a password.");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordText.setError("Passwords do not match.");
            valid = false;
        }
        return valid;
    }

    public boolean isValidPassword(final String password) {
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}