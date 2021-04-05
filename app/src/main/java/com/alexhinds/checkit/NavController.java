package com.alexhinds.checkit;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class NavController {


    // Database refrence path to lists
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("test/lists");
    // final instances to hold id of the menu groups
    private final int yourListsID = R.id.your_lists;
    private final int sharedListID = R.id.shared_lists;
    private final String TAG = "side menu population";
    // instances of objects to be manipulated
    public Menu sMenu;
    private final String userUID;
    private int genIDYourLists = 1000;
    private int genIDSharedLists = 1001;


    /***
     * Constructor of sideMenu, it will also capture the user UUID
     * @param sMenu Menu which will be manipulated
     */
    public NavController(Menu sMenu) {
        this.sMenu = sMenu;
        //get the user uid to check if the list belong to the user
        this.userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.addChildEventListener(new ChildEventListener() {
            // Handle the listener when a child is added in the database or removed
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: " + snapshot.getValue().toString() + "Previous Child " + previousChildName);

                if (snapshot.child("owner").getValue().toString().equals(userUID)) {
                    List list = snapshot.getValue(List.class);
                    if (!list.isShareable()) {
                        MenuItem menuItem = sMenu.add(yourListsID, --genIDYourLists, 200, list.getCategory());
                        MainActivity.menuItemHashMap.put(menuItem.getItemId(), snapshot.getKey());
                    } else {
                        MenuItem menuItem = sMenu.add(sharedListID, ++genIDSharedLists, 1200, list.getCategory());
                        MainActivity.menuItemHashMap.put(menuItem.getItemId(), snapshot.getKey());
                    }
                } else {
                    //TODO if not owner but is shared with owner
                }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildChanged: " + snapshot.getValue().toString() + "previous child " + previousChildName);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onChildRemoved: " + snapshot.getKey());
                if (MainActivity.menuItemHashMap.containsValue(snapshot.getKey())) {
                    Integer menuItemIDKey = Integer.MAX_VALUE;
                    for (Map.Entry<Integer, String> entry : MainActivity.menuItemHashMap.entrySet()) {
                        if (entry.getValue().equals(snapshot.getKey())) {
                            menuItemIDKey = entry.getKey();
                            sMenu.removeItem(menuItemIDKey);
                            break;
                        }
                    }
                    MainActivity.menuItemHashMap.remove(menuItemIDKey);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getDetails());


            }
        });


    }


}
