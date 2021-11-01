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


    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5time);
        t5b=findViewById(R.id.t5b);
        textView2=findViewById(R.id.textView2);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        String firstdate= prefs.getString("firstdate","");
        textView2.setText(firstdate);
        t5b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomain();
            }
        });
    }

    private void tomain() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}