package com.alexhinds.checkit;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {
    private ArrayList<ListItem> mListItemArray;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.itemCheckBox);
        }
    }

    // ListAdapter Constructor
    public ListAdapter(ArrayList<ListItem> itemListArray) {
        mListItemArray = itemListArray;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ListItem currentItem = mListItemArray.get(position);

        // get information from the currentItem to pass it into the view holder (layout)
        holder.mCheckBox.setText(currentItem.getData());

        // TODO/////////////////////////////////////////////////////////////////////////////////// Check if this if statement creates any Errors when user is checking and unchecking an item**********************************
        // if item is set to checked in the database, then set the UI to be checked
        if(currentItem.isMarkedDone()) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mListItemArray.size();
    }


}
