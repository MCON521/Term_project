package com.example.touchchallenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.touchchallenge.activities.SettingsActivity;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private TextView timer;
    private String time;
    private int seconds = 0;
    private boolean check = true;
    private String time_list[];
    private boolean mUseAutoSave;
    private final String mKEY_TIME = "Time";
    private String mKEY_AUTO_SAVE;

    @Override
    protected void onStop() {
        super.onStop();
        saveOrDeleteGameInSharedPrefs();
    }

    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        // Save current game or remove any prior game to/from default shared preferences
        if (mUseAutoSave)
            for(int i = 0; i < 10; i++){
                editor.putString(mKEY_TIME + (i + 1), time_list[i]);
            }
        else
            for(int i = 0; i < 10; i++){
                editor.remove(mKEY_TIME + (i + 1));
            }
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        restoreFromPreferences_SavedGameIfAutoSaveWasSetOn();
        restoreOrSetFromPreferences_AllAppAndGameSettings();
    }
    private void restoreFromPreferences_SavedGameIfAutoSaveWasSetOn() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        if (defaultSharedPreferences.getBoolean(mKEY_AUTO_SAVE,true)) {
            for(int i = 0; i < 10; i++){
                String gameString = defaultSharedPreferences.getString(mKEY_TIME + (i + 1), null);

                if (gameString != null) {
                    time_list[i] = gameString;
                }
            }
        }
    }

    private void restoreOrSetFromPreferences_AllAppAndGameSettings() {
        SharedPreferences sp = getDefaultSharedPreferences(this);
        mUseAutoSave = sp.getBoolean(mKEY_AUTO_SAVE, true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for(int i = 0; i < 10; i++){
            outState.putString(mKEY_TIME + (i + 1), time_list[i]);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        for(int i = 0; i < 10; i++){
            String gameString = savedInstanceState.getString(mKEY_TIME + (i + 1));
            if (gameString != String.valueOf(R.string.time)) {
                time_list[i] = gameString;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        time = this.getString(R.string.time);
        startNewGame();
        mKEY_AUTO_SAVE = getString(R.string.auto_save_key);
        resetScoreboard();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                if(Integer.parseInt(time.replaceAll(":", "")) >
                        Integer.parseInt(time_list[i].replaceAll(":", ""))){
                    String temp = time_list[i];
                    time_list[i] = time;
                    for(int j = i; j < 10; j++){
                        if(Integer.parseInt(temp.replaceAll(":", "")) >
                                Integer.parseInt(time_list[j].replaceAll(":", ""))){
                            String temp2 = time_list[j];
                            time_list[j] = temp;
                            temp = temp2;
                        }
                    }
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
            showSettings();
            return true;
        }
        else if(id == R.id.action_about){
            showAbout();
            return true;
        }
        else if(id == R.id.action_reset){
            resetScoreboard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetScoreboard() {
        time_list = new String[10];
        for(int i = 0; i < 10; i++){
            time_list[i] = time;
        }
    }

    private void showSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.about)).setTitle(R.string.app_name)
                .setIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher)).setCancelable(true);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
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

    public void goToScoreboard(View view) {
        Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        intent.putExtra("list", time_list);
        startActivity(intent);
    }
}