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
        database = FirebaseDatabase.getInstance().getReference("test/users"); // test db
        //Get Resources
        registerUserEmail = (EditText) findViewById(R.id.register_user_email);
        registerUserName = (EditText) findViewById(R.id.register_user_name);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerButton = (Button) findViewById(R.id.register_button);
        alreadyRegistered = (TextView) findViewById(R.id.already_registered);

        // validation rules
        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        String regexUserName = "^[a-zA-Z0-9]{2,20}$";
        String regexPassword =  "^[a-zA-Z0-9]{6,20}$";
        awesomeValidation.addValidation(registerUserName, regexUserName, "Enter valid username (alphanumeric only, length 2-20)");
        awesomeValidation.addValidation(registerUserEmail, Patterns.EMAIL_ADDRESS, "Enter valid email address");
        awesomeValidation.addValidation(registerPassword, regexPassword, "Enter valid password (alphanumeric only length 6-20)");

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


                // if valid input, create account with associated info
                if (awesomeValidation.validate()) {
                    // registration info
                    String userEmail = registerUserEmail.getText().toString();
                    String userName = registerUserName.getText().toString();
                    String password = registerPassword.getText().toString();

                    createAccount(userEmail, userName, password);
                }

            }


        });
    }

    /** createAccount                   creates Firebase Application User and user entry
     *                                  in the realtime database
     *
     * @param userEmail email used to create account with Firebase Authentication
     * @param username username used for displayName in the app
     * @param password password used to create account with Firebase Authentication
     */

    private void createAccount(String userEmail, String username, String password) {

        final String email = userEmail;
        final String userName = username;
        final String userPassword = password;

        final String TAG = "createAccount: ";



        // sign up
        auth.createUserWithEmailAndPassword(email, password)
                // if User creation is successful, update their display name, and add their entry in the database
                .addOnSuccessListener(RegisterActivity.this, authResult -> {

                    Log.d(TAG, "createUserWithEmail:success ");
                    Toast.makeText(getApplicationContext(), "Account Created Successfully...",
                            Toast.LENGTH_SHORT);

                    // get the newly created user
                    FirebaseUser currUser = authResult.getUser();


                    // set Firebase User's display name to the supplied username
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder() // create profile update to set username as display name
                                .setDisplayName(userName).build();

                    final String USER_PROFILE_TAG = "USER_PROFILE_UPDATE";
                    currUser.updateProfile(profileUpdates) // update profile with the change request
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(USER_PROFILE_TAG, "Name updated to \'"
                                        + userName
                                        + "\'.");
                                Toast.makeText(getApplicationContext(),
                                        "Name updated to " + userName,
                                        Toast.LENGTH_LONG).show();
                            }

                        });

                // ADDING USER TO REALTIME DB

                    User user = new User(userName, userPassword); // set up entry for user in db
                    // write object to database, using UID from signing up as the unique/primary key - it handles conversion to JSON
                    database.child(currUser.getUid()).setValue(user)

                            .addOnSuccessListener(aVoid -> Log.d(TAG, "createAccount::onSuccess: User added to DB"))
                            .addOnFailureListener(e -> Log.d(TAG, "createAccount::onFailure: User NOT added to DB"));

                    // go to login activity
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                    finish();
                })

                // account creation failed - ( FirebaseAuthUserCollisionException - email already in use)
                .addOnFailureListener(RegisterActivity.this, e -> {
                    Log.w(TAG, "createUserWithEmail:failure" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Account Creation Failed.\n" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                });

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getInstance().getCurrentUser();
        if (currentUser != null) {
            auth.getInstance().signOut();
        }
        else if (currentUser == null) {
            Toast.makeText(getApplicationContext(),
                    "currentUser is null",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }
}