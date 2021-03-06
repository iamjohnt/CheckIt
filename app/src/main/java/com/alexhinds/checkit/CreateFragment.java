package com.alexhinds.checkit;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateFragment extends Fragment {

    // Define Fields
    private EditText editText_category;
    private EditText editText_share_with;
    private CheckBox checkBox_time_specific;
    private EditText editText_date;
    private EditText editText_time;
    private Button button_create;
    private DatabaseReference mDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // TODO: Error Handling (Verify Inputs)
        // TODO: Populate Database
        // TODO: Navigate to CurrentListFragment if list created successfully
        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        System.out.println("Create Button Clicked");

                        String category = editText_category.getText().toString();
                        System.out.println("Category: " + category);

                        String shareWith = editText_share_with.getText().toString();
                        System.out.println("Share With: " + shareWith);

                        if (checkBox_time_specific.isChecked()) {
                            System.out.println("Time Specific");

                            String date = editText_date.getText().toString();
                            if (TextUtils.isEmpty(date)) {
                                Toast.makeText(getActivity(),
                                        "Must include date!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                System.out.println("Date: " + date);
                            }

                            String time = editText_time.getText().toString();
                            if (TextUtils.isEmpty(time)) {
                                Toast.makeText(getActivity(),
                                        "Must include time!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                System.out.println("Time: " + time);
                            }

                        }

                    }
                }
        );

        // Return View
       return view;
    }
}