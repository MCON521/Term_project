package com.example.touchchallenge.classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touchchallenge.R;


public class ScoreboardListHolder extends RecyclerView.ViewHolder {
    public final TextView scoreboard_time, list_number;

    public ScoreboardListHolder(@NonNull View itemView) {
        super(itemView);
        scoreboard_time = itemView.findViewById(R.id.scoreboard_timer);
        list_number = itemView.findViewById(R.id.list_number);
    }
}
