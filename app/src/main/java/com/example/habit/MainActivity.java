package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

   // String uniqueID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart= prefs.getBoolean("firstStart",true);

Toast.makeText(MainActivity.this,"firststart?"+String.valueOf(firstStart),Toast.LENGTH_LONG).show();
    if (firstStart==true || 1==1){

        opentutorial();
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
       // editor.putString("UID",uniqueID);
        editor.apply();
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
}