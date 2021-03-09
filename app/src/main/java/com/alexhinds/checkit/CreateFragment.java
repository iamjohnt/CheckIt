package com.alexhinds.checkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.firebase.auth.FirebaseAuth;


public class CreateFragment extends Fragment {

    // Define Fields
    private EditText editText_category;
    private EditText editText_share_with;
    private CheckBox checkBox_time_specific;
    private EditText editText_date;
    private EditText editText_time;
    private Button button_create;

    private FirebaseAuth auth;
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



        // TODO: Populate Database
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
                            System.out.println("Date: " + date);

                            String time = editText_time.getText().toString();
                            System.out.println("Time: " + time);

                        }


                    }
                }
        );


        // Return View
       return view;

    }
}
