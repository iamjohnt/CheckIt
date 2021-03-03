package com.alexhinds.checkit;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    private final String TAG = "LOGIN_ACTIVITIY"; // for logging


    // Define Resources
    private EditText loginUserEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView notRegistered;




    private AwesomeValidation awesomeValidation;
    private DatabaseReference database;
    private FirebaseAuth auth;


    // ANDROID LIFE CYCLE
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else if (currentUser == null) {
            Toast.makeText(getApplicationContext(),
                    "currentUser is null",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Firebase Authentication Instance
        auth = FirebaseAuth.getInstance();

        // Get Resources
        loginUserEmail = (EditText) findViewById(R.id.login_user_email);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        notRegistered = (TextView) findViewById(R.id.new_user);

        // validation configuration
        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        String regexPassword = "[a-zA-Z0-9]{6,20}";

        awesomeValidation.addValidation(loginUserEmail, Patterns.EMAIL_ADDRESS, "Enter a valid email address");
        awesomeValidation.addValidation(loginPassword, regexPassword , "Enter valid password (alphanumeric only, length 6-20)");


        // Login Button OnClick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate user login credentials and attempt log in, redirects to MainActivity if successful
                if (awesomeValidation.validate()) {
                    attemptLogin(loginUserEmail.getText().toString(), loginPassword.getText().toString());
                }
            }

        });

        // RegisterActivity
        notRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // Login Function
    private void attemptLogin(String loginUserEmail, String loginPassword) {


        auth.signInWithEmailAndPassword(loginUserEmail, loginPassword)
                .addOnSuccessListener(authResult -> {
                    Log.d(TAG, "attemptLogin: Successful");

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "attemptLogin: Failed: " + e.getMessage());
                });
    }

}