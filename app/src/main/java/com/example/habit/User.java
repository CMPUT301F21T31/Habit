package com.example.habit;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class denoting a single User, having a list of Habits
 */
public class User {

    private String displayName;
    private String email;
    private String uuid;
    private Boolean stayLoggedIn;
    private ArrayList<String> habits;
    private ArrayList<String> followers;
    private ArrayList<String> following;
    private ArrayList<String> requests;

    /**
     * Constructor for user with habit list
     * @param displayName Non-unique name used throughout app
     * @param habits List of habits
     * @param email Email used for firebase auth
     */
    public User(String displayName, String email, String uuid, Boolean stayLoggedIn, ArrayList<String> habits,
                ArrayList<String> followers, ArrayList<String> following, ArrayList<String> requests) {
        this.displayName = displayName;
        this.email = email;
        this.uuid = uuid;
        this.stayLoggedIn = stayLoggedIn;
        this.habits = habits;
        this.followers = followers;
        this.following = following;
        this.requests = requests;
    }

    public User(String displayName, String email, String uuid, Boolean stayLoggedIn, ArrayList<String> habits) {
        this(displayName, email, uuid, stayLoggedIn, habits, new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>());
    }

    /**
     * Constructor for user with no habits, creates user with empty habit list
     */
    public User(String displayName, String email, String uuid, Boolean stayLoggedIn) {
        this(displayName, email, uuid, stayLoggedIn, new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>(), new ArrayList<String>());
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
     * Get a user's ID
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    public Boolean getStayLoggedIn() {
        return stayLoggedIn;
    }

    public void setStayLoggedIn(Boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    /**
     * @return all of User's habits
     */
    public ArrayList<String> getHabits() {
        return habits;
    }

    public void setHabits(ArrayList<String> habits) {
        this.habits = habits;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    /* Firestore Methods */

    /**
     * Update a user object
     * @param uuid
     * @param user
     */
    public static void updateUser(String uuid, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uuid).set(user);
    }

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
    public static void deleteHabit(String uuid, String habitId, int deletedPosition) {
        deleteHabitFromHabits(habitId);
        deleteHabitFromUser(uuid, habitId);
        fixHabitPositions(uuid, deletedPosition);
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

    /**
     * Maintain the list position variable of other habits when one is deleted
     * @param uuid
     * @param deletedPosition
     */
    private static void fixHabitPositions(String uuid, int deletedPosition) {

        // Get habits that belong to this user and are below the deleted habit in the list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habits")
                .whereEqualTo("userId", uuid)
                .whereGreaterThan("listPosition", deletedPosition)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * Subtract 1 from the position of any Habit that is lower in the list
                     * @param task Task to wait for
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (Habit habit: Objects.requireNonNull(task.getResult()).toObjects(Habit.class)) {
                                habit.setListPosition(habit.getListPosition() - 1);
                                updateHabit(habit.getHabitId(), habit);
                            }
                        }
                    }
                });

    }

    /**
     * Send a follow request
     * @param context Context for displaying alert messages
     * @param requesterUuid String uuid of requester
     * @param email Email address to request
     */
    public static void sendRequest(Context context, String requesterUuid, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                // Check if there is an already a request for this user
                                ArrayList<String> currentRequests = (ArrayList<String>) document.get("requests");
                                if (currentRequests.contains(requesterUuid)) {
                                    Toast.makeText(context, "You already have a pending request for this user!", Toast.LENGTH_SHORT);
                                }

                                // Add requesterUuid to requested user's requests list
                                DocumentReference requestedUser = document.getReference();
                                requestedUser.update("requests", FieldValue.arrayUnion(requesterUuid));
                            }
                        } else {
                            // Email not found
                            Toast.makeText(context, "Could not find a user with that email!", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    /**
     * Uuid1 declines a follow request from uuid2
     * @param uuid1
     * @param uuid2
     */
    public static void declineRequest(String uuid1, String uuid2) {

        // Get user documents
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference uuid1User = db.collection("users").document(uuid1);

        // Remove request from uuid1
        uuid1User.update("requests", FieldValue.arrayRemove(uuid2));
    }

    /**
     * uuid1 accepts the follow request from uuid2
     * @param uuid1
     */
    public static void acceptRequest(String uuid1, String uuid2) {

        // Get user documents
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference uuid1User = db.collection("users").document(uuid1);
        DocumentReference uuid2User = db.collection("users").document(uuid2);

        // Remove request from uuid1
        uuid1User.update("requests", FieldValue.arrayRemove(uuid2));

        // Add uuid1 to uuid2's following list
        uuid2User.update("following", FieldValue.arrayUnion(uuid1));

        // Add uuid2 to uuid1's followers list
        uuid1User.update("followers", FieldValue.arrayUnion(uuid2));
    }

    /**
     * uuid1 stops following uuid2
     * @param uuid1
     */
    public static void stopFollowing(String uuid1, String uuid2) {

        // Get user documents
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference uuid1User = db.collection("users").document(uuid1);
        DocumentReference uuid2User = db.collection("users").document(uuid2);

        // Remove uuid2 from uuid1's following list
        uuid1User.update("following", FieldValue.arrayRemove(uuid2));

        // Remove uuid1 from uuid2's followers list
        uuid2User.update("followers", FieldValue.arrayRemove(uuid1));

    }
}
