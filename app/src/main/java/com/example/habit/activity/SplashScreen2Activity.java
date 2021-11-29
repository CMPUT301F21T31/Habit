package com.example.habit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.habit.R;
import com.example.habit.animations.ProgressBarAnimation;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen2Activity extends AppCompatActivity {
private ProgressBar progress;
private int from=0;
private int to=100;
private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen2);
        progress=findViewById(R.id.STARTBAR);
        ProgressBarAnimation anim = new ProgressBarAnimation(progress, from, to);
        anim.setDuration(1000);
        progress.startAnimation(anim);
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen2Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}