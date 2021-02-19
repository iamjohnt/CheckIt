package com.alexhinds.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    // Define Resources
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    private TextView notRegistered;

    private AwesomeValidation mAwesomeValidation;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseDatabase database = FirebaseDatabase.getInstance();



        // Get Resources
        loginUsername = (EditText) findViewById(R.id.login_user_name);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        notRegistered = (TextView) findViewById(R.id.new_user);

        // validation configuration
        final AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        String regexPassword = "[a-zA-Z0-9]+";

        mAwesomeValidation.addValidation(loginUsername, regexPassword, "Enter valid email address (alphanumeric only)");
       //mAwesomeValidation.addValidation(loginEmail, Patterns.EMAIL_ADDRESS, "Enter valid email address");

        mAwesomeValidation.addValidation(loginPassword, regexPassword , "Enter valid password (alphanumeric only)");

        // Login Button OnClick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAwesomeValidation.validate()) {
                    attemptLogin(loginUsername.getText().toString(), loginPassword.getText().toString());
                }
            }

        });

        notRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }


    // Login Function
    private void attemptLogin(String loginUsername, String loginPassword) {
        database = FirebaseDatabase.getInstance().getReference();

        // TODO: connect to database & validate info
    }
}