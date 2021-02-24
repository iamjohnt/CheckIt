package com.alexhinds.checkit;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MAIN_ACTIVITY";
    private FirebaseAuth auth;
    private TextView mainActivityUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        mainActivityUserName = findViewById(R.id.displayName);
        String displayName = auth.getCurrentUser().getDisplayName();
        Log.d(TAG, "onCreate: displayName = " + displayName);
        mainActivityUserName.setText("Welcome "+displayName + ",");
    }


    @Override
    protected void onStop() {
        super.onStop();
        auth.signOut();
    }

}