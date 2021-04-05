package com.alexhinds.checkit;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class CreateFragment extends Fragment {

    // Define Fields
    private TextView textView_welcome;
    private EditText editText_category;
    private EditText editText_share_with;
    private CheckBox checkBox_time_specific;
    private EditText editText_date;
    private EditText editText_time;
    private Button button_create;
    private DatabaseReference rootRef;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        // TODO Check if currentUser is null, redirect if true?
        // Get View
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        // Link Fields
        textView_welcome = (TextView) view.findViewById(R.id.textview_welcome);
        editText_category = (EditText) view.findViewById(R.id.editText_category);
        editText_share_with = (EditText) view.findViewById(R.id.editText_share_with);
        checkBox_time_specific = (CheckBox) view.findViewById(R.id.checkbox_time_specific);
        editText_date = (EditText) view.findViewById(R.id.editText_date);
        editText_time = (EditText) view.findViewById(R.id.editText_time);
        button_create = (Button) view.findViewById(R.id.button_create_frag);

        textView_welcome.setText("Welcome " + auth.getCurrentUser().getDisplayName());

        // TODO: Additional Error Handling (Verify Inputs, Address Duplicates)
        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        // ################### CATEGORY ###################
                        String category = editText_category.getText().toString();
                        if (TextUtils.isEmpty(category)) {
                            Toast.makeText(getActivity(),
                                    "Must include category!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // ################### DATE CREATED ###################
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                        String dateCreated = formatter.format(date);

                        // ################### SHARE WITH ###################
                        //TODO: based on username? multiple inputs allowed? need to parse w/ delimiter?
                        String shareWith = editText_share_with.getText().toString();
                        boolean isShareable = true;
                        if (TextUtils.isEmpty(shareWith)) {
                            isShareable = false;
                        }

                        // ################### DEADLINE ###################
                        boolean hasDeadline = false;
                        String deadline = "";
                        if (checkBox_time_specific.isChecked()) {
                            hasDeadline = true;

                            // ################### DEADLINE DATE ###################
                            String deadlineDate = editText_date.getText().toString();
                            if (TextUtils.isEmpty(deadlineDate)) {
                                Toast.makeText(getActivity(),
                                        "Must include date!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                deadline += deadlineDate;
                            }

                            // ################### DEADLINE TIME ###################
                            String deadlineTime = editText_time.getText().toString();
                            if (TextUtils.isEmpty(deadlineTime)) {
                                Toast.makeText(getActivity(),
                                        "Must include time!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                deadline = deadline + " " + deadlineTime;
                            }
                        }

                        // ################### POPULATE DATABASE ###################
                        rootRef = FirebaseDatabase.getInstance().getReference("test");


                        final String userEmail = auth.getCurrentUser().getEmail();
                        final String userId = auth.getCurrentUser().getUid();

                        DatabaseReference listsReference = rootRef.child("lists");
                        // create new list object to send to db - list id is creating using .push()
                        DatabaseReference listEntry = listsReference.push();
                        final String listId = listEntry.getKey();
                        Log.d("TEST", "onClick: " + listId);
                        List newList = new List(listId, category, dateCreated, hasDeadline, deadline, isShareable, shareWith, userId);

                    /*
                       LIST DATA CODE - TODO TO BE REMOVED LATER - also need to remove it from childUpdates
                     */
                        DatabaseReference listData = rootRef.child("listData/" + listId);
                        listData.push();
                        String listItemKey = listData.getKey();
                        java.util.List<ListItem> items = new LinkedList<>();
                        ListItem test = new ListItem("test", userId, true);
                        ListItem test2 = new ListItem("test", userId, true);
                        items.add(test);
                        items.add(test2);

                    /* ############################# PUSHING UPDATES TO DB ############################

                        update db in 3 places:
                        1) lists/$listId (add list metadata)
                        2) listMembers/$listId/$userid (add user to created Lists' members)
                        3) userLists/$userId/$listId (inverted listMembers - stores lists for each user - to easily grab user lists)

                       TODO currently also updates ListData with test user items @ listData/userId/listId - need to remove

                        using updateChildren() allows these grouped updates to be done atomically - all succeed, or all fail
                     */

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("lists/" + listId, newList); // list metadata
                        childUpdates.put("listMembers/" + listId + "/" + userId, true); // list members
                        //                        childUpdates.put("users/"+userId+"/ownedLists/"+listId, true); // updating ownedLists in User entry
                        // changing db structure slightly - moving userlists into its own structure; true - owner, false - member with access
                        childUpdates.put("userLists/" + userId + "/" + listId, true);
                        childUpdates.put("listData/" + listId, items);

                        rootRef.updateChildren(childUpdates)
                                .addOnSuccessListener(command -> {
                            /*
                                This query is only for testing, you can check the log for results
                             */
                                    Query x = rootRef.child("userLists/" + userId);

                                    x.get().addOnCompleteListener(task -> {
                                        //                           DataSnapshot z =  task.getResult();
                                        //                            Log.d("query", "onClick: " + z.getValue());
                                        Iterable<DataSnapshot> y = task.getResult().getChildren();
                                        for (DataSnapshot dataSnapshot : y) {
                                            if (dataSnapshot == null) {
                                                Log.d("queryqueryquery", "onClick: query is null");
                                            }
                                            java.util.List<String> ownedListIds = new LinkedList<String>();
                                            java.util.List<String> accessibleListIds = new LinkedList<String>();

                                            if (dataSnapshot.getValue().equals(true)) { // list owner
                                                ownedListIds.add(dataSnapshot.getKey());
                                            } else if (dataSnapshot.getValue().equals(false)) { // list member
                                                accessibleListIds.add(dataSnapshot.getKey());
                                            }

                                            for (String ownedListId : ownedListIds) {
                                                Log.d("datasnapshots", "queryDump ownedLists: " + ownedListId);
                                            }
                                            for (String accessibleListId : accessibleListIds) {
                                                Log.d("datasnapshots", "queryDump accessibleLists: " + accessibleListId);
                                            }
                                            //Log.d("DATASNAPSHOT: ", "onClick: " + dataSnapshot.getKey() +" = "  + dataSnapshot.getValue());
                                        }
                                    });


                                    //TODO: handle sharing (how to get user IDs from shareWith string? use this to update listMembers)

                                    // Go to Current List Fragment
                                    goToCurrentListFragment();
                                });
                    }
                }
        );

        // Return View
        return view;
    }

    private void clearAllLists() {
        rootRef.setValue(null);
    }

    private void goToCurrentListFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                        new CurrentListFragment()).commit();
    }

}