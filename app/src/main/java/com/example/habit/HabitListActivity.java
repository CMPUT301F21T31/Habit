package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class HabitListActivity extends AppCompatActivity {

    // Firebase references
    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    CollectionReference usersCollectionRef;
    CollectionReference habitsCollectionRef;
    CollectionReference habitEventsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Initialize auth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            // TODO: Go back to login here
        }

        // @Lewis see testDB() for an example of how to create habits and habit events in DB
        // testDB();
    }

    /**
     * Example function for calling User.addHabit
     */
    private void testDB() {

        // Instantiate mock habits
        HashMap<String, Boolean> daysOfWeek1 = null;
        HashMap<String, Boolean> daysOfWeek2 = null;
        try {
            daysOfWeek1 = Habit.generateDaysDict(new ArrayList<Boolean>(
                    Arrays.asList(false, true, false, false, true, false, false)));
            daysOfWeek2 = Habit.generateDaysDict(new ArrayList<Boolean>(
                    Arrays.asList(true, false, false, true, false, false, true)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Instantiate mock habits
        Habit mockHabit1 = new Habit("Swimming", "Get fit", new Date(), new Date(), daysOfWeek1);

        // Instantiate mock habit events
        HabitEvent mockHabitEvent1 = new HabitEvent("Edmonton", "Swam 20 laps");

        // Add habit to both the habits collection and habit list for this user
        User.addHabit(user.getUid(), mockHabit1);
    }


}