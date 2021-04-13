package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.ArrayList;
import java.util.List;
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
        authenticate.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Log.d("onComplete: ", e.getMessage());
                                registrationErrorTxtView.setVisibility(VISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            List<String> recipients = new ArrayList<>();
                            recipients.add(emailAddress);
                            new MailAsyncTask(RegistrationActivity.this, EmailTypeEnum.WelcomeEmail, recipients, "");
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
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
            //First name cannot be empty.
            firstNameText.setError("First name required.");
            valid = false;
        } else if (emailAddress.isEmpty()) {
            //Email cannot be empty.
            emailAddressText.setError("Email address required.");
            valid = false;
        } else if (confirmEmailAddress.isEmpty()) {
            //Confirm email cannot be empty.
            confirmEmailAddressText.setError("Email address required.");
            valid = false;
        } else if (!emailAddress.equals(confirmEmailAddress)) {
            //Email addresses must match.
            confirmEmailAddressText.setError("Email addresses do not match.");
            valid = false;
        } else if (!password.isEmpty() && password.length() >= 8 && !isValidPassword(password)) {
            //Password must match criteria.
            passwordText.setError("Password must contain 8 characters, 1 capital letter, and 1 special character.");
            valid = false;
        } else if (password.isEmpty()) {
            //Password cannot be empty.
            passwordText.setError("Please enter a password.");
            valid = false;
        } else if (confirmPassword.isEmpty()) {
            //Confirm password cannot be empty.
            confirmPasswordText.setError("Please enter a password.");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            //Passwords must match.
            confirmPasswordText.setError("Passwords do not match.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            //Email must be a valid structure.
            emailAddressText.setError("Your email must contain a valid domain.");
            valid = false;
        }

        return valid;
    }

    public boolean isValidPassword(final String password) {

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+*=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}