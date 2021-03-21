package com.alexhinds.checkit;

import android.util.Log;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SideMenu {


    // instances of objects to be manipulated
    public Menu sMenu;
    private String userUID;
    // Database refrence path
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("test/lists");
    // final instances to hold id of the menu groups
    private final int yourListsID = R.id.your_lists;
    private final int sharedListID = R.id.shared_lists;


    //TODO is this the best place for the map or maybe better in the main activity??
    public HashMap<String, List> listsDictionary;


    private final String TAG = "side menu population";


    /***
     * Constructor of sideMenu
     * @param sMenu Menu which will be manipulated
     */
    public SideMenu(Menu sMenu) {
        this.sMenu = sMenu;

    }


    /***
     * populate side menu will populate the side menu given by mapping the children of the snapshot
     * which are List class with key value pair, the key will be the key in the database and
     * the value will be the list
     * @param firebaseAuth FirbaseAuth to get the user uuid
     * @param listsSnapShot DataSnapShot of the lists in the database
     */
    public void populateSideMenu(FirebaseAuth firebaseAuth, DataSnapshot listsSnapShot) {
        // these two are ranges of the list id, and will be the same as the MenuItem id
        // your lists will be decreasing from 1000, and shared lists increasing from 1001
        int genIDYourLists = 1000;
        int genIDSharedLists = 1001;
        // hash map that will hold the lists belonging or shared with user
        listsDictionary = new HashMap<>();
        //get the user uid to check if the list belong to the user
        userUID = firebaseAuth.getCurrentUser().getUid();
        Log.d("fire base user id", "SideMenu: current user" + userUID);
        Log.d(TAG, "createSideMenu: the size of the menu is " + sMenu.size());


        // check if the snap shot has anything
        if (listsSnapShot.exists()) {
            // for every child in the snapshot which in this case are the lists
            for (DataSnapshot child : listsSnapShot.getChildren()) {
                // for every child get the value into a List class
                List list = child.getValue(List.class);
                // check if the user id matches the list owner or shareable #TODO maybe we should add the shared with UUID in this field
                if (list.getOwner().equals(userUID) || list.getShareWith().equals(userUID)) {
                    Log.d("child of snapshot", "SideMenu: " + list.getCategory());
                    if (list.isShareable()) {
                        list.menuListID = ++genIDSharedLists;
                    } else {
                        list.menuListID = --genIDYourLists;
                    }
                    listsDictionary.put(child.getKey(), list);
                    Log.d(TAG, "createSideMenu: the size of the hash map is " + listsDictionary.size());
                }
            }
            populateMenu();

        }


    }

    /***
     * populateMenu will add MenuItems from the listsDictionary map to the smenu if they don't exist
     */
    public void populateMenu() {

        // for every entry in the map check if the menu already has this item, if not added to the menu
        for (Map.Entry<String, List> entry : listsDictionary.entrySet()) {
            //map list is not in the menu
            if (sMenu.findItem(entry.getValue().menuListID) == null) {
                // list is shareable
                if (entry.getValue().isShareable()) {
                    //add menu item to shared list group
                    sMenu.add(sharedListID, entry.getValue().menuListID, entry.getValue().menuListID, entry.getValue().getCategory());
                    Log.d(TAG, "populateMenu: " + sMenu.findItem(entry.getValue().menuListID));
                }
                    //add to yourLists group
                else {
                    sMenu.add(yourListsID, entry.getValue().menuListID, entry.getValue().menuListID, entry.getValue().getCategory());
                }
            } else {
                Log.d(TAG, "populateMenu: " + sMenu.findItem(entry.getValue().menuListID));
            }
        }
    }
}

        // adding menu items using a string,  mark item as checked
//        for (int i =0; i<listsArrayList.size(); i++){
//            menu.add(yourListsID,501+i,101+i,listsArrayList.get(i).toString());
//           // menu.add(sharedListID,1001+i,1001+i,"sharedList");
//        }
//
//        MenuItem menuItem = menu.findItem(501);
//        menuItem.setCheckable(true);
//}

//    public void addList(DataSnapshot dataSnapshot, String previousChildName){
//
//        listsArrayList.add(dataSnapshot.child("category").getValue());
//        Log.d(TAG, "addList: " + dataSnapshot.child("category"));
//        populateMenu();
//
//
//    }
