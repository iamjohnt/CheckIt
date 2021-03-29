package com.alexhinds.checkit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
* TODO: check how to expand recyclerview in a constrain Layout all the way to the bottom view
*
*
* */

public class CurrentListFragment extends Fragment {

    private DatabaseReference rootRef;
    private EditText inputListItem;
    private Button addItem;
    private ArrayList<ListItem> itemsArray;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // the bridge between the array list and the recycler view
    private RecyclerView.LayoutManager mLayoutManager; // responsible for aligning the single items in the list



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // get View
        View view = inflater.inflate(R.layout.fragment_current_list, container, false);

        // initialize rootRef
        rootRef = FirebaseDatabase.getInstance().getReference("test");

        // initialize itesmArray for user to add items to
        itemsArray = new ArrayList<>();

        inputListItem = view.findViewById(R.id.inputListItem);
        addItem = view.findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: get the current list name (UID if exists)
                //      then use the name to populate the listData

                Log.d("input message", inputListItem.getText().toString());
//                getItemListFromDatabase();
//                addItemToDatabase(inputListItem.getText().toString());

                // testing
                addItemFromLocalArrayList(inputListItem.getText().toString());

                // insert items into recyclerview
                mRecyclerView = view.findViewById(R.id.recycler_menu);
                mLayoutManager = new LinearLayoutManager(getActivity());
                mAdapter = new ListAdapter(itemsArray);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


                inputListItem.getText().clear();

            }
        });

        return view;
    }

    private void getItemListFromDatabase() {
        DatabaseReference listDataRef = rootRef.child("listData/" + "-MWz93HpHQfXzlGi8f4p"); // TODO: change hardcoded listID with a variaable

        listDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getValue(); // returns an object of all of the ListItem objects in the list --- need to decouple this
                Log.d("ITEMS IN LIST", snapshot.getValue().toString());

                // iterate through the items in the list
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("THIS IS AN ITEM IN LIST", ds.child("data").getValue().toString());

                    // TODO: add ListItem objects from database to an arraylist ----> HOW DO I CONVERT SNAPSHOT TO ListItem TYPE ???
//                    itemsArray.add(ds);

                    // insert the item name into the recyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Getting Value Failed", "loadListItems:onCancelled", error.toException());
            }
        });

    }


    private void addItemToDatabase(String item) {

        // TODO:
        //      Step 1: get current list UID
        //      Step 2: find current list UID in listData
        //      Step 3: change the values of item 0 (also do: delete the auto created item1)
        //      Step 4: add another ListItem object for every item added
        //      Note: step 3 and 4 will be an if else statement (if data attribute in the database is equal to test change value, else add a new ListItem object)
//        mDatabase = FirebaseDatabase.getInstance().getReference("test/listData");

//        mDatabase.child("testingThisShite").child(inputListItem.getText().toString()).setValue(inputListItem.getText().toString());
        ///////////////////////////////////////////////

        // get current list reference
        DatabaseReference listDataRef = rootRef.child("listData/" + "-MWz93HpHQfXzlGi8f4p"); // TODO: change hardcoded listID with a variaable

        // create a ListItem object, add it to the itemsArray ArrayList and push the ListItem object to firebase
        ListItem itemObject = new ListItem(item, "YMAC3TRNJPVECXYhwmq87UCrSQk2", false); // TODO: change hardcoded listID with a variaable

        for(int i = 0; i < itemsArray.size(); i++) {
            // TODO: first I need to pull (read) all of the items in the list, then create a if statement:
            //       if (item exists in list) update attributes
            //       else (add item to end of list)
            listDataRef.child(String.valueOf(i)).setValue(itemObject);

        }
    }

    private void addItemFromLocalArrayList(String item) {
        ListItem newItem = new ListItem(item, "Erez", false);

        itemsArray.add(newItem);
    }
}
