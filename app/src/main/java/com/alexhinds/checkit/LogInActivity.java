package com.alexhinds.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    // Define Resources
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    private TextView notRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Get Resources
        loginUsername = (EditText) findViewById(R.id.login_user_name);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        notRegistered = (TextView) findViewById(R.id.new_user);
        // Login Button OnClick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
//                attemptLogin(loginUsername.getText().toString(), loginPassword.getText().toString());
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
        // TODO: connect to database & validate info
    }
}