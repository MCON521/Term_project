package com.example.touchchallenge;

import android.os.Bundle;

import com.example.touchchallenge.classes.ScoreboardListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreboardActivity extends AppCompatActivity {

    private ScoreboardListAdapter mAdapter;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        list = getIntent().getStringArrayExtra("list");
        setupAdapter(list);
        setupToolbar();
        setupFab();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> handleFABClick());
    }

    private void handleFABClick() {

    }

    public void setupAdapter(String[] list){
        mAdapter = new ScoreboardListAdapter(list);
        RecyclerView Scoreboard = findViewById(R.id.scoreboard);
        Scoreboard.setAdapter(mAdapter);
    }
}