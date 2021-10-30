package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class HabitListActivity extends AppCompatActivity {

    // Variable references for later
    FirebaseFirestore db;
    CollectionReference usersCollectionRef;
    CollectionReference habitsCollectionRef;
    CollectionReference habitEventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Initialize DB
        db = FirebaseFirestore.getInstance();

        // Get references to each collection
        usersCollectionRef = db.collection("users");
        habitsCollectionRef = db.collection("habits");
        habitEventsRef = db.collection("habitEvents");

        // Get user details
    }

    private void testDB() {
        // Instantiate mock users
        User mockUser1 = new User("John", "jsmith49");
        User mockUser2 = new User("Karla", "krox99");


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


        Habit mockHabit1 = new Habit("Swimming", "Get fit", new Date(), new Date(), daysOfWeek1);
        Habit mockHabit2 = new Habit("Reading", "Learn", new Date(), new Date(), daysOfWeek2);

        // Instantiate mock habit events
        HabitEvent mockHabitEvent1 = new HabitEvent("Edmonton", "Swam 20 laps");
        HabitEvent mockHabitEvent2 = new HabitEvent("Bedroom", "Finished chapter 7");

        mockUser1.addHabit(mockHabit1, usersCollectionRef, habitsCollectionRef);
        mockUser2.addHabit(mockHabit2, usersCollectionRef, habitsCollectionRef);
    }


}