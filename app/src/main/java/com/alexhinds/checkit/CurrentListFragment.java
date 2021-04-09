package com.alexhinds.checkit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * TODO: check how to expand recyclerview in a constrain Layout all the way to the bottom view
 *
 *
 * */

public class CurrentListFragment extends Fragment {

    private DatabaseReference rootRef;
    private EditText inputListItem;
    private Button addItem;
    private CheckBox itemCheckBox;
    private ArrayList<ListItem> itemsArray;
    private FirebaseAuth auth;
    private String currUserId;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // the bridge between the array list and the recycler view
    private RecyclerView.LayoutManager mLayoutManager; // responsible for aligning the single items in the list


    // header private data fields
    private TextView userName;
    private TextView categoryName;
    private TextView privateHeader;
    private TextView finishBy;
    private Task<DataSnapshot> currentList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        currUserId = auth.getCurrentUser().getUid();

        // get View
        View view = inflater.inflate(R.layout.fragment_current_list, container, false);


        // initialize rootRef
        rootRef = FirebaseDatabase.getInstance().getReference("test");

        // initialize itesmArray for user to add items to
        itemsArray = new ArrayList<>();

        inputListItem = view.findViewById(R.id.inputListItem);
        addItem = view.findViewById(R.id.addItem);
        itemCheckBox = view.findViewById(R.id.itemCheckBox);

        // get items of list from database
        String listID = getArguments().get("LIST").toString();
        getItemListFromDatabase(listID);
        Log.d("Getting items...", "successful");
        // insert items into recyclerview
        mRecyclerView = view.findViewById(R.id.recycler_menu);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ListAdapter(itemsArray);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputListItem.isFocused()) {
                    // add item to database (listData) when user clicks on the "Add" button
                    addItemToDatabase(inputListItem.getText().toString(), listID);

                    // clear the text box to allow user to input new item
                    inputListItem.getText().clear();
                }
            }
        });


        //header population
        // initialize header variables
        userName = view.findViewById(R.id.username);
        categoryName = view.findViewById(R.id.category);
        privateHeader = view.findViewById(R.id.private_header);
        finishBy = view.findViewById(R.id.finish_by_toolbar);
        TextView todaysDate = view.findViewById(R.id.date);
        //delete list bin
        ImageView deleteList = view.findViewById(R.id.delete_list);

        // setting header variables
        todaysDate.setText(MainActivity.getDateString());
        userName.setText(auth.getCurrentUser().getDisplayName());
        // getting a list snap shot and setting list specific header fields
        currentList = rootRef.child("lists").child(listID).get().addOnSuccessListener(task -> {
            List list = currentList.getResult().getValue(List.class);
            setCategory(list.getCategory());
            setPrivateheader(list.isShareable());
            setFinishBy(list.isHasDeadline(), list.getDeadline());

        }).addOnFailureListener(e -> Log.d("Current List Frag", "onFailure: Database List Snapshot Error Reading"));


        // on click listener for the delete list bin
        deleteList.setOnClickListener(v -> {
            if (deleteList(listID, currUserId, rootRef)) {
                goToCreateListFragment();
            }
        });


        return view;
    }

    //helper to set finish by field
    private void setFinishBy(boolean hasDeadline, String deadline) {
        if (hasDeadline) {
            finishBy.setText(R.string.due + deadline);
        } else {
            finishBy.setText(R.string.no_due_date);
        }
    }

    // helper to set private or shared header field
    private void setPrivateheader(boolean shareable) {
        if (shareable) {
            privateHeader.setText(R.string.shared);
        } else {
            privateHeader.setText(R.string.privatehint);
        }
    }

    //helper to set category field
    private void setCategory(String category) {
        categoryName.setText(category);
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the fragment is stopped (not in the user's view) the database is updated with the new itemsArray attributes (if any have changed)
        String listID = getArguments().get("LIST").toString();

        rootRef.child("listData/" + listID).setValue(itemsArray);

    }

    private void getItemListFromDatabase(String listId) {
        DatabaseReference listDataRef = rootRef.child("listData/" + listId);

        listDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // clear the itemsArray displayed in the UI
                itemsArray.clear();

                // get current snapshot of database and fill the items into the itemsArray
                if (snapshot.getValue() == null) { // no items in list yet

                } else {
                    snapshot.getValue(); // returns an object of all of the ListItem objects in the list --- need to decouple this
                    Log.d("ITEMS IN LIST", snapshot.getValue().toString());

                    // iterate through the items in the list
                    for (DataSnapshot databaseListItem : snapshot.getChildren()) {

//                    ListItem newItem = new ListItem(databaseListItem.child("data").getValue().toString(), databaseListItem.child("addedBy").getValue().toString(), (boolean) databaseListItem.child("markedDone").getValue());
                        ListItem newItem = databaseListItem.getValue(ListItem.class);

                        itemsArray.add(newItem);
                    }
                    // update the recyclerView
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Getting Value Failed", "loadListItems:onCancelled", error.toException());
            }
        });

    }


    private void addItemToDatabase(String item, String listId) {

        // get current list reference
        final DatabaseReference listDataRef = rootRef.child("listData/" + listId);
        final DatabaseReference listRef = rootRef.child("lists/" + listId);

        // create a ListItem object, and push it to firebase
        ListItem itemObject = new ListItem(item, currUserId, false);

        // add item to database -- get the location of the new item using the length of the current itemsArray
        listDataRef.child(String.valueOf(itemsArray.size())).setValue(itemObject);
    }


    // delete current list and return to create list fragment
    // update the database with created null map with keys listId, ownerId, database root ref
    private boolean deleteList(String listID, String userId, DatabaseReference rootRef) {

        // create a new map then push keys to it
        Map<String, Object> childUpdateMap = new HashMap<>();
        childUpdateMap.put("lists/" + listID, null); // list metadata
        childUpdateMap.put("listMembers/" + listID, null); // list members
        childUpdateMap.put("userLists/" + userId + "/" + listID, null);
        childUpdateMap.put("listData/" + listID, null);


        // update the database with created null map
        rootRef.updateChildren(childUpdateMap).addOnSuccessListener(aVoid -> {
            System.out.println(aVoid);
            Log.d("DataBaseHandler", "onSuccess: deleted");
            childUpdateMap.clear();
        });


        return true;
    }

    //helper to go to create fragment
    private void goToCreateListFragment() {
        final CreateFragment createFragment = new CreateFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                        createFragment).commit();
    }


//    // for testing purposes only
//    private void addItemFromLocalArrayList(String item) {
//        ListItem newItem = new ListItem(item, currUserId, false);
//
//        itemsArray.add(newItem);
//    }
}

