package com.example.habit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class denoting a single User, having a list of Habits
 */
public class User {

    private String displayName;
    private String userName;
    private ArrayList<Habit> habits;

    /**
     * Constructor for user with habit list
     * @param displayName Non-unique name used throughout app
     * @param userName Unique name used for adding followers
     * @param habits List of habits
     */
    public User(String displayName, String userName, ArrayList<Habit> habits) {
        this.displayName = displayName;
        this.userName = userName;
        this.habits = habits;
    }

    /**
     * Constructor for user with no habits, simply creates user with empty habit list
     */
    public User(String displayName, String userName) {
        this(displayName, userName, new ArrayList<Habit>());
    }

    /**
     * @return The display name for the user, not necessarily unique in the system.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName New display name for a user, changing what they see in the app
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Username of user, which is unique in the system.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName Username to check for in system, throw error if it does otherwise set username
     */
    public void setUserName(String userName) {
        // TODO: Check if username exists here, should this be done in user class or another?
        this.userName = userName;
    }

    /**
     * @return all of User's habits
     */
    public ArrayList<Habit> getHabits() {
        return habits;
    }

    /**
     * @param habit Habit to add to this User's habit list
     */
    public void addHabit(Habit habit, CollectionReference userColRef, CollectionReference habitColRef) {
//        this.habits.add(habit);


        Map<String, Object> data = new HashMap<>();

        // Add habit data to map
        data.put("title", habit.getTitle());
        data.put("reason", habit.getReason());
        data.put("start", new Timestamp(habit.getStart()));
        data.put("end", new Timestamp(habit.getEnd()));
        data.put("daysOfWeek", habit.getDaysOfWeek());
        data.put("events", habit.getEvents());

        // Add to habits collection
        habitColRef
                .document() // Use auto-generated ID
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(String.valueOf(R.string.db_add_success), "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(String.valueOf(R.string.db_add_fail), "Data could not be added!" + e.toString());
                    }
                });

        // Add habit ID to this users habit list
    }

    /**
     * @param habit Habit to delete from this user's list if it exists
     * @throws NoSuchElementException Thrown if habit does not exist in this User's list
     */
    public void deleteHabit(Habit habit) {
        if (this.habits.contains(habit)) {
            this.habits.remove(habit);
        } else {
            throw new NoSuchElementException("Habit to be deleted not found in habits list!");
        }
    }
}
