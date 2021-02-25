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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_ACTIVITY"; // for logging
    // Define Resources
    private EditText registerUserEmail;
    private EditText registerUserName;
    private EditText registerPassword;
    private Button registerButton;
    private TextView alreadyRegistered;

     private DatabaseReference database;
     private AwesomeValidation awesomeValidation;
     private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        //Get Resources
        registerUserEmail = (EditText) findViewById(R.id.register_user_email);
        registerUserName = (EditText) findViewById(R.id.register_user_name);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerButton = (Button) findViewById(R.id.register_button);
        alreadyRegistered = (TextView) findViewById(R.id.already_registered);

        //
        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        String regexUserName = "^[a-zA-Z0-9]{2,20}$";
        String regexPassword =  "^[a-zA-Z0-9]{6,}$";
        awesomeValidation.addValidation(registerUserName, regexUserName, "Enter valid username (alphanumeric only, req length 2-20");
        awesomeValidation.addValidation(registerUserEmail, Patterns.EMAIL_ADDRESS, "Enter valid email address");
        awesomeValidation.addValidation(registerPassword, regexPassword, "Enter valid password (alphanumeric only) req length 6+");

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
                String userEmail = registerUserEmail.getText().toString();
                String userName = registerUserName.getText().toString();
                String password = registerPassword.getText().toString();

                // if valid input, create account with User object to store associated info
                if (awesomeValidation.validate()) {
                    createAccount(userEmail, userName, password);
                    // continue to login activity
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                    finish();

                }

            }


        });
    }

    /** createAccount                   creates Firebase Auth User and user entry
     *                                  in the database
     *
     * @param userEmail email used to create account
     * @param username username used for displayName in the app
     * @param password user account password
     */

    private void createAccount(String userEmail, String username, String password) {

        final String email = userEmail;
        final String userName = username;
        final String userPassword = password;


        final String USER_PROFILE_TAG = "USER_PROFILE_UPDATE";

        // sign up
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                       FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser(); // get the newly created user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder() // create profile update to set username as display name
                                .setDisplayName(userName).build();

                        Log.d(TAG, "createUserWithEmail:success ");

                        Toast.makeText(getApplicationContext(), "Account Created Successfully...",
                                Toast.LENGTH_SHORT);


                        currUser.updateProfile(profileUpdates) // update profile
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(USER_PROFILE_TAG, "Name updated to \'"
                                                    + userName
                                                    + "\'.");
                                            Toast.makeText(getApplicationContext(),
                                                            "Name updated to " + userName,
                                                            Toast.LENGTH_LONG).show();
                                        }

                                    }
                        });


                    } else {
                        // If sign up fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Account Creation Failed.\n" + task.getException() ,
                                Toast.LENGTH_SHORT).show();
                    }
                    database = FirebaseDatabase.getInstance().getReference("test/users"); // test db


                    // set up entry for user in db
                    User user = new User(userName, userPassword);
                    // write object to database, using UID from signing up as the unique/primary key - it handles conversion to JSON
                    database.child(auth.getCurrentUser().getUid()).setValue(user)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "createAccount::onSuccess: Account Created");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "createAccount::onFailure: Account Creation Failed");
                                }
                            });

                });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getInstance().getCurrentUser();
        if (currentUser != null) {
            auth.getInstance().signOut();
            finish();
        }
        else if (currentUser == null) {
            Toast.makeText(getApplicationContext(),
                    "currentUser is null",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }
}