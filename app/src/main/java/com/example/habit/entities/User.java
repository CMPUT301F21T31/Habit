package com.example.habit.entities;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Justin
 * Class denoting a single User of our app. This is the most basic entity class for
 * our app. Properties of this class are what we store in the database.
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
     * Constructor for a User object with all properties.
     * @param displayName String non-unique name used to refer to user within app
     * @param email String unique email used for firebase authentication and follow requests
     * @param uuid String uuid of user from firebase, used as key for User records in database
     * @param stayLoggedIn Boolean indicating if the app should keep the user logged in when possible
     * @param habits List of string HabitIDs belonging to this user
     * @param followers List of string UserIDs that follow this user
     * @param following List of string UserIDs that this user is following
     * @param requests List of string UsersIDs with current requests to follow this user
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

    /**
     *
     * @param displayName String non-unique name used to refer to user within app
     * @param email String unique email used for firebase authentication and follow requests
     * @param uuid String uuid of user from firebase, used as key for User records in database
     * @param stayLoggedIn Boolean indicating if the app should keep the user logged in when possible
     * @param habits List of string HabitIDs belonging to this user
     */
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
     * @return String user ID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Get a boolean indicating if the user app should try to keep the user logged in
     * @return Boolean stayLoggedIn flag
     */
    public Boolean getStayLoggedIn() {
        return stayLoggedIn;
    }

    /**
     * Set the stayLoggedIn flag
     * @param stayLoggedIn
     */
    public void setStayLoggedIn(Boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    /**
     * Get an array list of all the habit IDs for this user
     * @return ArrayList of ID strings
     */
    public ArrayList<String> getHabits() {
        return habits;
    }

    /**
     * Set this users list of habit IDs
     * @param habits Array of habit ID strings
     */
    public void setHabits(ArrayList<String> habits) {
        this.habits = habits;
    }

    /**
     * Get array list of user IDs that follow this user
     * @return ArrayList of ID strings
     */
    public ArrayList<String> getFollowers() {
        return followers;
    }

    /**
     * Get array list of user IDs that this user follows
     * @return ArrayList of ID strings
     */
    public ArrayList<String> getFollowing() {
        return following;
    }

    /**
     * Get array list of user IDs that have a request to follow this user
     * @return ArrayList of ID strings
     */
    public ArrayList<String> getRequests() {
        return requests;
    }

    /* Firestore Methods */

    /**
     * Update a user object
     * @param uuid Uuid of the user object to update
     * @param user User object to use in update
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
     * @param uuid String id of user
     * @param deletedPosition Integer indicating the position of the deleted habit
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
     * Send a follow request from user indicated by requesterUuid, to the user with the provided
     * email. Takes a context so an alert can be displayed if the email doesn't belong to any user.
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

                                // Email not found
                                Toast.makeText(context, "Request sent.", Toast.LENGTH_SHORT);
                            }
                        } else {
                            // Email not found
                            Toast.makeText(context, "Search failed - please try again.", Toast.LENGTH_SHORT);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Email not found
                        Toast.makeText(context, "Could not find a user with that email!", Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * Uuid1 declines a follow request from uuid2. Updates the following and followers lists of
     * both users.
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
     * Uuid1 accepts a follow request from uuid2. Updates the following and followers lists of
     * both users.
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
     * Uuid1 stops following uuid2. Updates the following and followers lists of both users.
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
