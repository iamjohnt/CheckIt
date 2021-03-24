package com.alexhinds.checkit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/*
* TODO: create a custom toolbar
* */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private final String TAG = "MAIN_ACTIVITY";
    private FirebaseAuth auth;

    private boolean isNightTheme = false;

    //fields for the radio button for dark/light themes.
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // firebase authentication
        auth = FirebaseAuth.getInstance();

        // set the toolbar as the new actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Attach drawer variable to drawer_layout in activity_main.xml file
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.menu_view);

        // get the menu header, display Firebase User's display name
        View menuHeader = navigationView.getHeaderView(0);
        TextView username = menuHeader.findViewById(R.id.username);
        username.setText(auth.getCurrentUser().getDisplayName());

        navigationView.setNavigationItemSelectedListener(this);

        // menu/hamburger icon at the top to control opening/closing of menu (in addition to click and drag)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); // rotates the hamburger icon as the menu opens and closes


        // TODO: this requires an if statement (this is opened only if no lists exist for the user, else open the last list viewed by the user
        //       (if list exists also need to include the following: navigationView.setCheckedItem(R.id.<list-id>))
        // TODO: Also read about savedInstanceState (if user rotates the device or anything else that recreates the activity)

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateFragment()).commit();



    }

    @Override
    public void onBackPressed() {
        // close navigation menu when back button is presses (if menu is open) rather than leaving the activity
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // switch between fragments (this will control which fragment is placed inside the fragment container (in the activity_main.xml
        switch  (item.getItemId()) {
            case R.id.create_list:
                changeStatusBarColor("#000000");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateFragment()).commit();
                break;
            case R.id.Example1_list: //***TODO: THIS MUST BE HANDLED BY A VARIABLE AND BE HANDLED DYNAMICALLY BECAUSE LISTS ARE ADDED AND DELETED DYNAMICALLY BY THE USER
                changeStatusBarColor("#031006");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentListFragment()).commit();
                break;
            case R.id.logout: // TODO: I may need to exit using authentication and ensuring that user can't use the back arrow after logging out (read about this)
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.light_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.dark_theme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeStatusBarColor(String color){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor(color));
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        auth.signOut();
    }

}