package com.example.habit.activities;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.habit.fragments.AddHabitEventFragment;
import com.example.habit.entities.Habit;
import com.example.habit.R;
import com.example.habit.entities.User;
import com.example.habit.adapters.HabitListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Main screen for application containing list of both all habits and today's habits. User can
 * navigate to friends screen, add a new habit, view one of their existing habits, or logout.
 *
 * Each element in the all habit list is clickable, opening the EditHabitActivity. In the daily
 * habit list, each element is swipeable with the options to denote an instance of that habit, i.e.
 * create a habit event, or mark that habit as incomplete for today.
 */
public class HabitListActivity extends AppCompatActivity {

    // Firebase references
    private FirebaseAuth auth;
    FirebaseFirestore db;

    // User objects
    FirebaseUser fb_user;
    User user;

    // All habits list
    ListView allHabitsListView;
    ArrayList<Habit> allHabitsDataList;
    HabitListAdapter allHabitsAdapter;

    // Daily habits list
    SwipeMenuListView dailyHabitsListView;
    ArrayList<Habit> dailyHabitsDataList;
    HabitListAdapter dailyHabitsAdapter;
    RelativeLayout dailyHabitBackground;

    // Buttons
    AppCompatButton allButton;
    AppCompatButton todayButton;
    ImageButton homeButton;
    ImageButton friendsButton;
    ImageButton logoutButton;

    // Greeting string
    TextView greeting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Initialize auth and db
        auth = FirebaseAuth.getInstance();
        fb_user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize all habits list
        allHabitsDataList = new ArrayList<>();
        allHabitsAdapter = new HabitListAdapter(this, allHabitsDataList, false);
        allHabitsListView = findViewById(R.id.all_habits_list);
        allHabitsListView.setAdapter(allHabitsAdapter);

        // Initialize daily habits list
        dailyHabitsDataList = new ArrayList<>();
        dailyHabitsAdapter = new HabitListAdapter(this, dailyHabitsDataList, true);
        dailyHabitsListView = findViewById(R.id.daily_habits_list);
        dailyHabitsListView.setAdapter(dailyHabitsAdapter);

        // Initialize buttons
        allButton = findViewById(R.id.habit_list_all_button);
        todayButton = findViewById(R.id.habit_list_today_button);
        homeButton = findViewById(R.id.homeButton);
        friendsButton = findViewById(R.id.friendsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Initialize greeting
        greeting = findViewById(R.id.greeting);

        Log.i("HabitList", "In onCreate");

        if (fb_user == null) {
            // Go back to login
        } else {

            // Get User record for logged in user
            DocumentReference docRef = db.collection("users").document(fb_user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // Get user object and their habit IDs
                            user = document.toObject(User.class);
                            greeting.setText("Hey, " + user.getDisplayName());

                            ArrayList<String> habits = user.getHabits();
                            Log.i("User habits", habits.toString());

                            // On change listener for habits belonging to this user
                            db.collection("habits")
                                    .whereEqualTo("userId", fb_user.getUid()) // Only get habits for this user
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            // Log failure and exit
                                            if (error != null) {
                                                Log.w("FAILED", "Listen failed.", error);
                                                return;
                                            }

                                            // Get today's weekday so we know which habits to add to today list
                                            String weekday = new SimpleDateFormat("EEEE").format(new Date()).toString();
                                            Log.i("Today", weekday);

                                            // Clear old habits
                                            allHabitsDataList.clear();
                                            dailyHabitsDataList.clear();

                                            for (QueryDocumentSnapshot doc : value) {

                                                // Get all changed habits and add to list
                                                Habit habit = doc.toObject(Habit.class);
                                                Log.d("CHANGE", doc.toObject(Habit.class).toString());
                                                allHabitsDataList.add(habit);

                                                // Add to daily list if Habit occurs today
                                                if (habit.isOnDay(weekday)) {
                                                    dailyHabitsDataList.add(habit);
                                                }
                                            }

                                            // Sort the habit lists based on list position
                                            allHabitsDataList.sort(Comparator.comparing(Habit::getListPosition));
                                            dailyHabitsDataList.sort(Comparator.comparing(Habit::getListPosition));

                                            // Tell the adapters to display/re-display the lists
                                            allHabitsAdapter.notifyDataSetChanged();
                                            dailyHabitsAdapter.notifyDataSetChanged();
                                        }
                                    });
                        } else {
                            Log.d("HABIT NOT EXISTS", "No such document");
                        }
                    } else {
                        Log.d("READ FAIL", "get failed with ", task.getException());
                    }
                }
            });
        }

        // Creator for daily habit list swipe menu
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // Create habit not complete item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.Red)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_baseline_clear_24);
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

        dailyHabitsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                Log.i("SWIPE MENU CLICK", Integer.toString(position) + " " + Integer.toString(index));

                // Reset background
                dailyHabitBackground = findViewById(R.id.daily_habit_content_holder);
                dailyHabitBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));

                // Get habit
                Habit habit = dailyHabitsAdapter.getItem(position);

                if (index == 0) {

                } else {

                    // Habit occurrence completed - increment completed count + add event
                    new AddHabitEventFragment(habit)
                            .show(getSupportFragmentManager(), "ADD_HabitEvent");
                }

                // Close the menu
                return false;
            }
        });

        dailyHabitsListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                dailyHabitBackground = dailyHabitsListView
                        .getChildAt(position)
                        .findViewById(R.id.daily_habit_content_holder);
                dailyHabitBackground.setBackgroundResource(R.drawable.habit_list_item_swiped);
            }

            @Override
            public void onSwipeEnd(int position) {}
        });

        dailyHabitsListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {}

            @Override
            public void onMenuClose(int position) {
                dailyHabitBackground = dailyHabitsListView
                        .getChildAt(position)
                        .findViewById(R.id.daily_habit_content_holder);
                dailyHabitBackground.setBackground(new ColorDrawable(getResources()
                        .getColor(R.color.Dark_Gray_Background)));
            }
        });

        // Set creator
        dailyHabitsListView.setMenuCreator(creator);

        // Direct the user to addHabit interface
        ImageButton add_habit_button = findViewById(R.id.add_habit_button);

        add_habit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHabit();
            }
        });

        // Once click the item in the list, then it start to edit the details
        System.out.println("Setting all habits on item click");
        allHabitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("In on item click");
                openEditHabit(i);
            }
        });
        System.out.println("Set all habits on item click");

        /* Button listeners */

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayButton.setBackground(getBaseContext().getDrawable(R.drawable.rounded_corners_button_not_clicked));
                allButton.setBackground(getBaseContext().getDrawable(R.drawable.rounded_corners_button_clicked));
                dailyHabitsListView.setVisibility(View.INVISIBLE);
                allHabitsListView.setVisibility(View.VISIBLE);
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allButton.setBackground(getBaseContext().getDrawable(R.drawable.rounded_corners_button_not_clicked));
                todayButton.setBackground(getBaseContext().getDrawable(R.drawable.rounded_corners_button_clicked));
                allHabitsListView.setVisibility(View.INVISIBLE);
                dailyHabitsListView.setVisibility(View.VISIBLE);
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriends();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    /**
     * Open the add habit screen, pass in the list position for the next Habit
     */
    private void openAddHabit() {
        Intent intent = new Intent(this, AddHabitActivity.class);
        intent.putExtra("nextPosition", allHabitsDataList.size());
        startActivity(intent);
    }

    /**
     * Open the edit/view habit screen
     * @param i Index of habit to edit/view
     */
    private void openEditHabit(int i) {
        Log.i("Test", "Launching edit habit");
        Intent intent = new Intent(this, EditHabitActivity.class);
        Habit habitToEdit = allHabitsAdapter.getItem(i);
        intent.putExtra("habit", habitToEdit);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("logoutClicked", true);
        startActivity(intent);
    }

    private void openFriends() {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}