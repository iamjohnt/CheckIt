package com.alexhinds.checkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    // Define Resources
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Get Resources
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);

        // Login Button OnClick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(loginUsername.getText().toString(), loginPassword.getText().toString());
            }
        });
    }

    // Login Function
    private void attemptLogin(String loginUsername, String loginPassword) {
        // TODO: connect to database & validate info
    }
}