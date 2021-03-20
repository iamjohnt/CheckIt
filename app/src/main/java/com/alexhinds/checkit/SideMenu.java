package com.alexhinds.checkit;

import android.nfc.Tag;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SideMenu {

    public Menu menu;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("test/lists");;
    public FirebaseUser firebaseUser;
    // final instances to hold id of the menu groups
    private final int yourListsID = R.id.your_lists;
    private final int sharedListID = R.id.shared_lists;
    public ArrayList<Object> listsArrayList;
    private final String TAG = "side Menu array";



    public SideMenu(Menu menu){
        this.menu = menu;

    }

    public SideMenu(Menu menu, FirebaseAuth firebaseAuth){
        listsArrayList = new ArrayList<Object>();
        this.menu = menu;
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(TAG, "SideMenu: current user" + firebaseUser.getUid());



    }

    public void populateMenu(){
        // adding menu items using a string,  mark item as checked
        for (int i =0; i<listsArrayList.size(); i++){
            menu.add(yourListsID,501+i,101+i,listsArrayList.get(i).toString());
           // menu.add(sharedListID,1001+i,1001+i,"sharedList");
        }

        MenuItem menuItem = menu.findItem(501);
        menuItem.setCheckable(true);
    }

    public void addList(DataSnapshot dataSnapshot, String previousChildName){
        listsArrayList.add(dataSnapshot.child("category").getValue());
        Log.d(TAG, "addList: " + dataSnapshot.child("category"));
        populateMenu();


    }





}
