package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class tutorial_2usern extends AppCompatActivity {
    TextView t_2;
    EditText t_2t;
    Button t2b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2usern);
        t_2=findViewById(R.id.t_2);
        Intent intent = getIntent();
        String fname = intent.getExtras().getString("fname");
        t_2.setText("WELCOME TO HABIT  "+fname+"     Please also choose a user name,which will uniquely identify you to others");
        t_2t=findViewById(R.id.t_2t);
        t2b=findViewById(R.id.t2b);
        String username = t_2t.getText().toString();
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("username",username);
        editor.apply();
        t2b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opent_3();
            }
        });
    }

    private void opent_3() {
        Intent intent=new Intent(this, tutorial_4habit.class);
        startActivity(intent);
    }
}