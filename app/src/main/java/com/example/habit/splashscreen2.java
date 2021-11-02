package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class splashscreen2 extends AppCompatActivity {
ProgressBar progress;
int from=0;
int to=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen2);
        progress=findViewById(R.id.STARTBAR);
        progressbaranimation anim = new progressbaranimation(progress, from, to);
        anim.setDuration(1000);
        progress.startAnimation(anim);
    }
}