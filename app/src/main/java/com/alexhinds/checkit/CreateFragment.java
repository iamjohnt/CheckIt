package com.alexhinds.checkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class CreateFragment extends Fragment {
    private FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        View createInflatedView =  inflater.inflate(R.layout.fragment_create, container, false);

        // add username to welcome
        TextView welcome = (TextView) createInflatedView.findViewById(R.id.textview_welcome);
        welcome.setText("Welcome "+ auth.getCurrentUser().getDisplayName());

        return createInflatedView;
    }
}
