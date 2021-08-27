package com.example.touchchallenge.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touchchallenge.R;


public class ScoreboardListAdapter extends RecyclerView.Adapter<ScoreboardListHolder> {

    private String[] items;

    public ScoreboardListAdapter(String[] list){
        items = list;
    }

    

    @NonNull
    @Override
    public ScoreboardListHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_list, parent, false);
        return new ScoreboardListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreboardListHolder holder, int position) {
        holder.list_number.setText(Integer.toString(position + 1));
        holder.scoreboard_time.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
