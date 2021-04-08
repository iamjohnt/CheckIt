package com.alexhinds.checkit;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/*
 * TODO: create a custom toolbar
 * */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // the static map that will hold the menuItemID as key and the string is the listID also the name of the list on the database
    public  HashMap<Integer, String> menuItemHashMap = new HashMap<>();
    private final String TAG = "MAIN_ACTIVITY";
    private DrawerLayout drawer;
    private FirebaseAuth auth;
    private NavController navController;
    private final DatabaseReference databaseReferenceToLists = FirebaseDatabase.getInstance().getReference("test/lists");


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






        /*
        menu instance passed to a new NavController class
         */
        navController = new NavController(navigationView.getMenu(),menuItemHashMap);


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


        int itemMenuId = item.getItemId();
        int currentMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Log.d(TAG, "onNavigationItemSelected: current mode" + currentMode);

        if (itemMenuId == R.id.create_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateFragment()).commit();
        } else if (itemMenuId == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        } else if (itemMenuId == R.id.delete_all_lists) {
            Log.d(TAG, "onNavigationItemSelected: delete all list");
            deleteAllLists();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateFragment()).commit();

        } else if (itemMenuId == R.id.your_lists_item || itemMenuId == R.id.shared_lists_item) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }





        else if (itemMenuId == R.id.light_theme) {
            drawer.closeDrawer(GravityCompat.START);
            if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(this, "Light Theme", Toast.LENGTH_SHORT).show();
                return refresgNavController();
                }
        } else if (itemMenuId == R.id.dark_theme) {
            drawer.closeDrawer(GravityCompat.START);
            if (currentMode == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(this, "Dark Theme", Toast.LENGTH_SHORT).show();
                return refresgNavController();
            }
        } else {
            CurrentListFragment currentListFragment = new CurrentListFragment();
            Bundle data = new Bundle();
            data.putString("LIST", menuItemHashMap.get(item.getItemId())); //TODO path to list replaced with value of the menuItemHashMap
            currentListFragment.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentListFragment).commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void deleteAllLists() {
        // traverse the map, use the value to delete the list in the database
        Map<String, Object> childUpdates = new HashMap<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("test");
        for (Map.Entry<Integer, String> entry : this.menuItemHashMap.entrySet()) {

            childUpdates.put("lists/" + entry.getValue(), null); // list metadata
            childUpdates.put("listMembers/" + entry.getValue(), null); // list members
            childUpdates.put("userLists/" + auth.getCurrentUser().getUid() + "/" + entry.getValue(), null);
            childUpdates.put("listData/" + entry.getValue(), null);
        }
        rootRef.updateChildren(childUpdates).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: ALL CHILDREN DELETED"));
        refresgNavController();






        //                        childUpdates.put("users/"+userId+"/ownedLists/"+listId, true); // updating ownedLists in User entry
        // changing db structure slightly - moving userlists into its own structure; true - owner, false - member with access


    }


    public boolean refresgNavController() {
        NavigationView navigationView = findViewById(R.id.menu_view);
        if (navigationView.getMenu() != null) {
            navController = new NavController(navigationView.getMenu(),menuItemHashMap);
            return true;
        }
        return false;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}