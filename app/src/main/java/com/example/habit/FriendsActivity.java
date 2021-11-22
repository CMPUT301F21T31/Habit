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
    ImageButton newFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize buttons
        feedButton = findViewById(R.id.feedButton);
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);
        newFollow = findViewById(R.id.new_follow_request_button);

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

        newFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendRequest().show(getSupportFragmentManager(), "Send friend request");
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