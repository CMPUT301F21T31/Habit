package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class tutorial_2usern extends AppCompatActivity {
TextView t_2;
    EditText t_2t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2usern);
        t_2=findViewById(R.id.t_2);
        Intent intent = getIntent();
        String fname = intent.getExtras().getString("fname");
        t_2.setText("WELCOME TO HABIT  "+fname+"     Please also choose a user name,which will uniquely identify you to others");
        String username = t_2t.getText().toString();
    }
}