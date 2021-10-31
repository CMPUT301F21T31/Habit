package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class tutorial_5freq extends AppCompatActivity {
    TextView t4t;
    TextView t4t_2;
    EditText t4et;
    Button t4b;
    CheckBox M;
    CheckBox TUES;
    CheckBox WED;
    CheckBox THUR;
    CheckBox FRI;
    CheckBox SAT;
    CheckBox SUN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial4freq);
        t4t=findViewById(R.id.t4t);

        t4b=findViewById(R.id.t4b);
        Intent intent = getIntent();
        String habit = intent.getExtras().getString("habit");
        t4t.setText(habit+" COOL!");
        t4b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opent5();
            }
        });
    }

    private void opent5() {
        Intent intent=new Intent(this, tutorial_6time.class);
        startActivity(intent);
    }
}