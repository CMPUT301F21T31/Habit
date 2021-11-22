package com.example.habit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private String email;
    private ArrayList<String> habits;

    /**
     * Constructor for user with habit list
     * @param displayName Non-unique name used throughout app
     * @param userName Unique name used for adding followers
     * @param habits List of habits
     * @param email Email used for firebase auth
     */
    public User(String displayName, String userName, String email, ArrayList<String> habits) {
        this.displayName = displayName;
        this.userName = userName;
        this.email = email;
        this.habits = habits;
    }

    /**
     * Constructor for user with no habits, creates user with empty habit list
     */
    public User(String displayName, String userName, String email) {
        this(displayName, userName, email, new ArrayList<String>());
    }

    /**
     * Empty constructor allowing storage of User objects in firestore
     */
    public User() {}

    /* Getters and Setters */

    /**
     * Get display name for the user, not necessarily unique in the system.
     * @return String with display name
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
     * Get username of user, which is unique in the system
     * NOTE: Removing this as email is already unique
     * @return String with username
     */
    @Deprecated
    public String getUserName() {
        return userName;
    }

    /**
     * Set username for user, should have already checked if unique
     * NOTE: Removing this as email is already unique
     * @param userName String username to set
     */
    @Deprecated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get a users email
     * @return String with user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set a users email
     * @param email Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return all of User's habits
     */
    public ArrayList<String> getHabits() {
        return habits;
    }

    /* Firestore Methods */

    /**
     * Add a habit object to the habits collection and add a reference to that habit to a users
     * habits list.
     * @param uuid String id of user to add habit to
     * @param habit Habit object to add
     */
    public static void addHabit(String uuid, Habit habit) {
        String habitId = addHabitToHabits(habit, uuid);
        addHabitToUser(uuid, habitId);
    }

    /**
     * Add a Habit object to the habits collection. Do not call this directly.
     * @param habit Habit object to add
     * @param uuid String id of user to add to
     */
    private static String addHabitToHabits(Habit habit, String uuid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newHabit = db.collection("habits").document();
        habit.setHabitId(newHabit.getId());
        habit.setUserId(uuid);
        newHabit.set(habit);
        return newHabit.getId();
    }

    /**
     * Add habit ID to a users habits list. Do not call this directly.
     * @param uuid String id of user to add to
     * @param habitID String id of habit to add
     */
    private static void addHabitToUser(String uuid, String habitID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("users").document(uuid);
        thisUser.update("habits", FieldValue.arrayUnion(habitID));
    }

    /**
     * Update an existing habit in DB
     * @param habitId String id of habit to update
     * @param habit Habit object with updated field(s)
     */
    public static void updateHabit(String habitId, Habit habit) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habits")
                .document(habitId)
                .set(habit);
    }

    /**
     * Delete habit from habits collection and habits list for a user
     * @param uuid String id to delete from
     * @param habitId String id of habit to delete
     */
    public static void deleteHabit(String uuid, String habitId) {
        deleteHabitFromHabits(habitId);
        deleteHabitFromUser(uuid, habitId);
    }

    /**
     * Delete habit from habits collection
     * @param habitId String id of habit to delete
     */
    private static void deleteHabitFromHabits(String habitId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habits").document(habitId).delete();
    }

    /**
     * Delete habit ID from habits list for a user
     * @param uuid String id of user to delete from
     * @param habitId String id of habit to delete
     */
    private static void deleteHabitFromUser(String uuid, String habitId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("users").document(uuid);
        thisUser.update("habits", FieldValue.arrayRemove(habitId));
    }
}
