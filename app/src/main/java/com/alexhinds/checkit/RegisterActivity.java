package com.alexhinds.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    // Define Resources
    private EditText registerUserName;
    private EditText registerPassword;
    private Button registerButton;
    private TextView alreadyRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Resources
        registerUserName = (EditText) findViewById(R.id.register_user_name);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerButton = (Button) findViewById(R.id.register_button);
        alreadyRegistered = (TextView) findViewById(R.id.already_registered);

        //alreadyRegistered Onclick
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                finish();
            }
        });

        // registerButton onClick event
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send data to database
            }
        });
    }
}