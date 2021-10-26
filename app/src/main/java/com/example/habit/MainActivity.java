package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        Toast.makeText(MainActivity.this, String.valueOf(firstStart), Toast.LENGTH_LONG).show();
        if (firstStart == true) {

            opentutorial();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

        } else {
            openHabitList();
        }
    }

    private void opentutorial() {
        Intent intent=new Intent(this,tutorial_1.class);
        startActivity(intent);
       // SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
       // SharedPreferences.Editor editor=prefs.edit();
       // editor.putBoolean("firstStart",true);
      //  editor.apply();

    }

    private void openHabitList() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }
}