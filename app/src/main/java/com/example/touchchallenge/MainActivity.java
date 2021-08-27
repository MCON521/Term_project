package com.example.touchchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touchchallenge.classes.ScoreboardListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private TextView timer;
    private String time;
    private int seconds = 0;
    private boolean check = true;
    private String time_list[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFab();
        time = this.getString(R.string.time);
        startNewGame();
        time_list = new String[10];
        for(int i = 0; i < 10; i++){
            time_list[i] = time;
        }
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

    private void startNewGame(){
        setupTimer();
        resetTimer();
    }

    private void setupTimer(){
        timer = findViewById(R.id.timer);
        Button timerButton = findViewById(R.id.touch_challenge_button);
        timerButton.setOnTouchListener((v, event) -> handleTimerClick(v, event));
    }

    private boolean handleTimerClick(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.post(timerRunnable);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.win) + " " + time).setTitle(R.string.app_name)
                    .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher)).setCancelable(true);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    resetTimer();
                    check = true;
                }
            });
            for(int i = 0; i < 10; i++){
                for(int j = i + 1; j < 10; j++){
                    if(Integer.valueOf(time_list[i]) < Integer.valueOf(time_list[j])) {
                        String temp = time_list[i];
                        time_list[i] = time_list[j];
                        time_list[j] = temp;
                    }
                }
            }
            for(int i = 0; i < 10; i++){
                if(Integer.valueOf(time) > Integer.valueOf(time_list[i])){
                    time_list[i] = time;
                    break;
                }
            }
            AlertDialog dialog = builder.create();
            dialog.show();
            check = false;
            seconds = 0;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetTimer(){
        timer.setText(time);
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            int hrs = seconds / 3600;
            int mins = (seconds % 3600) / 60;
            int secs = seconds % 60;

            time = String.format("%02d:%02d:%02d", hrs, mins, secs);
            timer.setText(time);
            if(check){
                seconds++;
                handler.postDelayed(timerRunnable,1000);
            }
        }
    };

    public void goToDashboard(View view) {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.putExtra("list", time_list);
        startActivity(intent);
    }
}