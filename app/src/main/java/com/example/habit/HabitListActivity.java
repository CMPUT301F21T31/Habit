package com.example.habit;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
    HabitList allHabitsAdapter;

    // Daily habits list
    ListView dailyHabitsListView;
    ArrayList<Habit> dailyHabitsDataList;
    HabitList dailyHabitsAdapter;

    // Buttons
    AppCompatButton allButton;
    AppCompatButton todayButton;

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
        allHabitsAdapter = new HabitList(this, allHabitsDataList, false);
        allHabitsListView = findViewById(R.id.all_habits_list);
        allHabitsListView.setAdapter(allHabitsAdapter);

        // Initialize daily habits list
        dailyHabitsDataList = new ArrayList<>();
        dailyHabitsAdapter = new HabitList(this, dailyHabitsDataList, true);
        dailyHabitsListView = findViewById(R.id.daily_habits_list);
        dailyHabitsListView.setAdapter(dailyHabitsAdapter);

        // Initialize buttons
        allButton = findViewById(R.id.habit_list_all_button);
        todayButton = findViewById(R.id.habit_list_today_button);

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

        // Direct the user to addHabit interface
        ImageButton add_habit_button = findViewById(R.id.add_habit_button);

        add_habit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHabit();
            }
        });

        // Once click the item in the list, then it start to edit the details
        allHabitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openEditHabit(i);
            }
        });
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
    }

    /**
     * Open the add habit screen
     */
    private void openAddHabit() {
        Intent intent = new Intent(this, addHabit.class);
        startActivity(intent);
    }

    /**
     * Open the edit/view habit screen
     * @param i Index of habit to edit/view
     */
    private void openEditHabit(int i) {
        Log.i("Test", "Launching edit habit");
        Intent intent = new Intent(this, editHabit.class);
        Habit habitToEdit = allHabitsAdapter.getItem(i);
        intent.putExtra("habit", habitToEdit);
        startActivity(intent);
    }

}