package com.alexhinds.checkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CurrentListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //testing to catch the listPath from the main activity

//        String listName = getArguments().getString("LIST");
//        Log.d("List Fragment", " list fragment path to list: " + listName);


        return inflater.inflate(R.layout.fragment_current_list, container, false);


    }
}
