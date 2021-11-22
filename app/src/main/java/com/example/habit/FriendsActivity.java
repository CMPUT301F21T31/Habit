package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FriendsActivity extends AppCompatActivity {

    // Buttons
    ImageButton feedButton;
    ImageButton homeButton;
    ImageButton friendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize buttons
        feedButton = findViewById(R.id.feedButton);
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);


        // TODO: Friends list stuff


        /* Button listeners */

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeed();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
    }

    private void openFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    private void openHome() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }
}