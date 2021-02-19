package com.alexhinds.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_ACTIVITY"; // for logging
    // Define Resources
    private EditText registerUserName;
    private EditText registerPassword;
    private Button registerButton;
    private TextView alreadyRegistered;

     private DatabaseReference mDatabase;


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
                // get registration info in string format
                String username = registerUserName.getText().toString();
                String password = registerPassword.getText().toString();

                // create account with User object to store associated info
                User user = new User(username, password);
                createAccount(user);

                // continue to login activity
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();

            }


        });
    }


    private void createAccount(User user) {
        /*
         *     Eventually will use Firebase Authentication for login. It generates a UID, which we can use
         *     to link the authenticated user to the user in the database
         *
         */

        // TODO implement registration with Firebase Authentication and link with UID
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // write object to database - it handles conversion to JSON
        mDatabase.child("test").child("users").child(UUID.randomUUID().toString()).setValue(user);  // UUID just a placeholder for the generated UID for now
        Log.d(TAG, "createAccount: successful");
    }
}