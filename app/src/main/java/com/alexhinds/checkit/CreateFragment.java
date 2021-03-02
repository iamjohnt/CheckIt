package com.alexhinds.checkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateFragment extends Fragment {

    // Define Fields
    private EditText editText_category;
    private EditText editText_share_with;
    private CheckBox checkBox_time_specific;
    private EditText editText_date;
    private EditText editText_time;
    private Button button_create;

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



        // TODO
        button_create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        System.out.println("Create Button Clicked");
                    }
                }
        );


        // Return View
       return view;
    }
}
