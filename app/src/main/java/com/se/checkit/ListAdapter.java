package com.se.checkit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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

        // set the checkbox to the isMarkedDone() value
        holder.mCheckBox.setChecked(currentItem.isMarkedDone());

        // set the isMarkedDone() value to the value of the checkbox
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentItem.setMarkedDone(isChecked);
                Log.d("items Array", "current item " + currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItemArray.size();
    }


}
