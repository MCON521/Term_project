package com.example.touchchallenge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.touchchallenge.classes.ScoreboardListAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

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
        if (getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFab() {
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> handleFABClick());
    }

    private void handleFABClick() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void setupAdapter(String[] list){
        mAdapter = new ScoreboardListAdapter(list);
        RecyclerView Scoreboard = findViewById(R.id.scoreboard);
        Scoreboard.setAdapter(mAdapter);
    }
}