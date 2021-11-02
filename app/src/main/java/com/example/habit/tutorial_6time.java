package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class tutorial_6time extends AppCompatActivity {
Button t5b;
Button timeend;
TextView enddatet;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5time);
        t5b=findViewById(R.id.t5b);
        timeend=findViewById(R.id.timeend);
        textView2=findViewById(R.id.textView2);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        String firstdate= prefs.getString("firstdate","");
        String etest= prefs.getString("etest","");
        textView2.setText("startdate"+firstdate);
        enddatet=findViewById(R.id.enddatet);
        enddatet.setText("enddate"+etest);
        timeend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toend();
            }
        }));
        t5b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tocal();
            }
        });
    }

    private void toend() {Intent intent=new Intent(this,calendarend.class);
        startActivity(intent);
    }

    private void tocal() {
        Intent intent=new Intent(this,calendar.class);
        startActivity(intent);
    }
}