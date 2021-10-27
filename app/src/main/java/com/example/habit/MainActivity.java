package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button fisrtstartrest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

Toast.makeText(MainActivity.this,"firststart?"+String.valueOf(firstStart),Toast.LENGTH_LONG).show();
    if (firstStart==true ){

            opentutorial();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

        fisrtstartrest=findViewById(R.id.fisrtstartrest);
        fisrtstartrest.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                  SharedPreferences.Editor editor=prefs.edit();
                  editor.putBoolean("firstStart",true);
                  editor.apply();
              }
          });
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