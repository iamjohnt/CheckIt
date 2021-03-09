package com.alexhinds.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


public class CreateFragment extends Fragment {

    // Define Fields
    private EditText editText_category;
    private EditText editText_share_with;
    private CheckBox checkBox_time_specific;
    private EditText editText_date;
    private EditText editText_time;
    private Button button_create;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get View
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        // Link Fields
        editText_category = (EditText) view.findViewById(R.id.editText_category);
        editText_share_with = (EditText) view.findViewById(R.id.editText_share_with);
        checkBox_time_specific = (CheckBox) view.findViewById(R.id.checkbox_time_specific);
        editText_date = (EditText) view.findViewById(R.id.editText_date);
        editText_time = (EditText) view.findViewById(R.id.editText_time);
        button_create = (Button) view.findViewById(R.id.button_create);

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
                            }
                            else {
                                deadline += deadlineDate;
                            }

                            // ################### DEADLINE TIME ###################
                            String deadlineTime = editText_time.getText().toString();
                            if (TextUtils.isEmpty(deadlineTime)) {
                                Toast.makeText(getActivity(),
                                        "Must include time!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                deadline = deadline + " " + deadlineTime;
                            }
                        }

                        // ################### POPULATE DATABASE ###################
                        // TODO: connect to owner (how to get current user ID?)
                        databaseReference = FirebaseDatabase.getInstance().getReference("test").child("lists");
                        List newList = new List(category, dateCreated, hasDeadline, deadline, isShareable, shareWith, "");
                        databaseReference.child(category).setValue(newList);

                        //TODO: handle sharing (how to get user IDs from shareWith string? use this to update listMembers)

                        // Go to Current List Fragment
                        goToCurrentListFragment();
                    }
                }
        );

        // Return View
        return view;
    }

    private void clearAllLists() {
        databaseReference.setValue(null);
    }

    private void goToCurrentListFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                        new CurrentListFragment()).commit();
    }

}