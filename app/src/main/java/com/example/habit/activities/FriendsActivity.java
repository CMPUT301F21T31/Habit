package com.example.habit.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.habit.R;
import com.example.habit.entities.User;
import com.example.habit.adapters.FriendsListAdapter;
import com.example.habit.fragments.SendRequestFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsActivity extends AppCompatActivity {

    // Auth and db
    FirebaseAuth auth;
    FirebaseUser fb_user;
    FirebaseFirestore db;
    User user;

    // Buttons
    ImageButton homeButton;
    ImageButton friendsButton;
    ImageButton followRequestButton;

    // Lists
    ArrayList<User> followingListData;
    ArrayList<User> followRequestListData;
    FriendsListAdapter followingListAdapter;
    FriendsListAdapter followRequestListAdapter;
    SwipeMenuListView followingListView;
    SwipeMenuListView followRequestListView;
    SwipeMenuCreator followingSwipeCreator;
    SwipeMenuCreator followRequestSwipeCreator;
    RelativeLayout userBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Initialize auth and db
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fb_user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize buttons
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);
        followRequestButton = findViewById(R.id.new_follow_request_button);

        // Initialize lists data, views, and adapters
        followingListData = new ArrayList<User>();
        followRequestListData = new ArrayList<User>();
        followingListAdapter = new FriendsListAdapter(this, 0, followingListData, 0);
        followRequestListAdapter = new FriendsListAdapter(this, 0, followRequestListData, 1);
        followingListView = findViewById(R.id.following_list);
        followRequestListView = findViewById(R.id.follow_requests_list);
        followingListView.setAdapter(followingListAdapter);
        followRequestListView.setAdapter(followRequestListAdapter);

        /* Firebase listeners */

        db.collection("users")
                .document(fb_user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        // Store user object
                        user = Objects.requireNonNull(task.getResult()).toObject(User.class);

                        // On change listeners for user document
                        task.getResult()
                                .getReference()
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                System.out.println("In snapshot listener!");

                                // Store new user object
                                user = value.toObject(User.class);

                                followingListData.clear();

                                // Populate following list
                                for (String uuid : user.getFollowing()) {

                                    db.collection("users")
                                            .document(uuid)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        // Add to list
                                                        followingListData.add(task.getResult()
                                                                .toObject(User.class));
                                                        followingListAdapter.notifyDataSetChanged();

                                                    } else {

                                                        Log.e("FollowingList",
                                                                "Error getting user " + uuid);
                                                    }
                                                }
                                            });
                                }
                                followingListAdapter.notifyDataSetChanged();

                                followRequestListData.clear();
                                // Populate request list
                                for (String uuid : user.getRequests()) {

                                    db.collection("users")
                                            .document(uuid)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        // Add to list
                                                        followRequestListData.add(task.getResult()
                                                                .toObject(User.class));
                                                        followRequestListAdapter.notifyDataSetChanged();

                                                    } else {
                                                        Log.e("FollowingRequestList",
                                                                "Error getting user " + uuid);
                                                    }
                                                }
                                            });
                                }
                                followRequestListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });


        /* Swipe Menu Components */

        followingSwipeCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // Create habit not complete item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.Red)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_baseline_remove_circle_outline_24);
                menu.addMenuItem(deleteItem);

                // Create spacer item
                SwipeMenuItem spacer = new SwipeMenuItem(getApplicationContext());
                spacer.setBackground(new ColorDrawable(getResources().getColor(R.color.Dark_Gray_Background)));
                spacer.setWidth(dp2px(3));
                menu.addMenuItem(spacer);

                // Create habit complete item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(R.drawable.half_rectangle_grey);
                openItem.setWidth(dp2px(90));
                openItem.setIcon(R.drawable.ic_baseline_person_search_24);
                menu.addMenuItem(openItem);
            }
        };

        followRequestSwipeCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // Create habit not complete item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.Red)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_baseline_not_interested_24);
                menu.addMenuItem(deleteItem);

                // Create spacer item
                SwipeMenuItem spacer = new SwipeMenuItem(getApplicationContext());
                spacer.setBackground(new ColorDrawable(getResources().getColor(R.color.Dark_Gray_Background)));
                spacer.setWidth(dp2px(3));
                menu.addMenuItem(spacer);

                // Create habit complete item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(R.drawable.half_rectangle_green);
                openItem.setWidth(dp2px(90));
                openItem.setIcon(R.drawable.ic_baseline_playlist_add_check_24);
                menu.addMenuItem(openItem);
            }
        };

        followingListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                // Reset background
                userBackground = followingListView
                        .getChildAt(position)
                        .findViewById(R.id.following_user_content_holder);
                userBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));

                // Get clicked user
                User clickedUser = followingListAdapter.getItem(position);

                // Delete or view a user you follow
                switch (index) {

                    case 0:
                        User.stopFollowing(user.getUuid(), clickedUser.getUuid());

                    case 2:
                        Intent intent = new Intent(FriendsActivity.this, FriendsHabitActivity.class);
                        String userId = clickedUser.getUuid();
                        intent.putExtra("user id", userId);
                        startActivity(intent);
                }

                return false;
            }
        });

        followRequestListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                // Reset background
                userBackground = followRequestListView
                        .getChildAt(position)
                        .findViewById(R.id.follow_request_user_content_holder);
                userBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));

                // Get clicked user
                User clickedUser = followRequestListAdapter.getItem(position);

                System.out.println("In onMenuItemClick, pos: " + String.valueOf(position) + ", Index: " + String.valueOf(index));
                // Deny or accept follow request
                switch (index) {

                    case 0:
                        User.declineRequest(user.getUuid(), clickedUser.getUuid());
                        break;

                    case 2:
                        User.acceptRequest(user.getUuid(), clickedUser.getUuid());
                        break;
                }

                // Close list
                return false;
            }
        });

        followingListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                userBackground = followingListView
                        .getChildAt(position)
                        .findViewById(R.id.following_user_content_holder);
                userBackground.setBackgroundResource(R.drawable.habit_list_item_swiped);
            }

            @Override
            public void onSwipeEnd(int position) {
            }
        });

        followRequestListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                userBackground = followRequestListView
                        .getChildAt(position)
                        .findViewById(R.id.follow_request_user_content_holder);
                userBackground.setBackgroundResource(R.drawable.habit_list_item_swiped);
            }

            @Override
            public void onSwipeEnd(int position) {}
        });

        followingListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {}

            @Override
            public void onMenuClose(int position) {
                userBackground = followingListView
                        .getChildAt(position)
                        .findViewById(R.id.following_user_content_holder);
                userBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));
            }
        });

        followRequestListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {}

            @Override
            public void onMenuClose(int position) {
                userBackground = followRequestListView
                        .getChildAt(position)
                        .findViewById(R.id.follow_request_user_content_holder);
                userBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));
            }
        });

        // Set creators
        followingListView.setMenuCreator(followingSwipeCreator);
        followRequestListView.setMenuCreator(followRequestSwipeCreator);

        /* Button listeners */

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        followRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRequestFragment().show(getSupportFragmentManager(), "send request");
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}