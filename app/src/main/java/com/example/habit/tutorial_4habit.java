package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class tutorial_4habit extends AppCompatActivity {
    TextView t3t;
    EditText t3et;
    Button t3b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3habit);
        t3t=findViewById(R.id.t3t);
        t3et=findViewById(R.id.t3et);
        t3b=findViewById(R.id.t3b);
        t3t.setText("COOL, lets start off by adding your first habit.what habit would you like to track");
        String firshabbit = t3et.getText().toString();
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("firshabbit",firshabbit);
        editor.apply();
        //ghgh
        t3b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opent_4();
            }
        });
    }

    private void opent_4() {
        Intent intent=new Intent(this, tutorial_5freq.class);
        String firshabbit = t3et.getText().toString();
        intent.putExtra("habit", firshabbit);
        startActivity(intent);
    }
}