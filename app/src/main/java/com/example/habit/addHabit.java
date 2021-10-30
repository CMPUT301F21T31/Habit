package com.example.habit;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


/**
 * US 01.01.01
 * US 01.02.01
 * US 01.06.01
 * Create the add habit interface
 */
public class addHabit extends AppCompatActivity {

    ImageButton back;
    ImageButton confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit);

        back = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);

        // Back to the main screen
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
