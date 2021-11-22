package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FeedActivity extends AppCompatActivity {

    // Buttons
    ImageButton feedButton;
    ImageButton homeButton;
    ImageButton friendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize buttons
        feedButton = findViewById(R.id.feedButton);
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);


        // TODO: Feed stuff here


        /* Button listeners */

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriends();
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }

    private void openFriends() {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }
}