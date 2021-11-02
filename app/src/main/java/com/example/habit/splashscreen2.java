package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class splashscreen2 extends AppCompatActivity {
private ProgressBar progress;
private int from=0;
private int to=100;
private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen2);
        progress=findViewById(R.id.STARTBAR);
        progressbaranimation anim = new progressbaranimation(progress, from, to);
        anim.setDuration(1000);
        progress.startAnimation(anim);
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(splashscreen2.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}