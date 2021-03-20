package com.alexhinds.checkit;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class SideMenu {

    public Menu menu;
    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("test/lists");;
    public FirebaseUser firebaseUser;
    // final instances to hold id of the menu groups
    private final int yourListsID = R.id.your_lists;
    private final int sharedListID = R.id.shared_lists;
    public ArrayList<String> listsArrayList;



    public SideMenu(Menu menu){
        this.menu = menu;

    }

    public SideMenu(Menu menu, FirebaseAuth firebaseAuth){
        this.menu = menu;
        firebaseUser = firebaseAuth.getCurrentUser();
        listsArrayList = new ArrayList<>();
        listsArrayList.add(databaseReference.child("lists").toString());

    }

    public void populateMenu(){
        // adding menu items using a string,  mark item as checked
        for (int i =0; i<1; i++){
            menu.add(yourListsID,501+i,101+i,listsArrayList.get(0).toString());
            menu.add(sharedListID,1001+i,1001+i,"sharedList");
        }

        MenuItem menuItem = menu.findItem(501);
        menuItem.setCheckable(true);
    }



}
