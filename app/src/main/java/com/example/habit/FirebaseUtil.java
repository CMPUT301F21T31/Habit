package com.example.habit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {

    /**
     * TODO
     * @param id
     */
    public static void getUser(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
            }
        });
    }

    /**
     * TODO
     * @param id
     */
    public static void getHabit(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("cities").document("BJ");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Habit habit = documentSnapshot.toObject(Habit.class);
            }
        });
    }

    /**
     * TODO
     * @param id
     */
    public static void getHabitEvent(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("cities").document("BJ");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HabitEvent habitEvent = documentSnapshot.toObject(HabitEvent.class);
            }
        });
    }

    /**
     * Add a User object to the users collection
     * @param user
     * @param uuid
     */
    public static void addUser(User user, String uuid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uuid).set(user);
    }

    /**
     * Add a Habit object to the habits collection
     * @param habit
     */
    public static void addHabit(Habit habit) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habits").document().set(habit);
    }

    public static void addUserHabit(String habitID) {
        // String uuid, String habitID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("users").document("le3BYE2Qe3ffXWv3QtHwEMGoLao2");
        thisUser.update("habits", FieldValue.arrayUnion("test1"));
    }

    /**
     * Add a HabitEvent object to the HabitEvents collection
     * @param habitEvent
     */
    public static void addHabitEvent(HabitEvent habitEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habitEvents").document().set(habitEvent);
    }
}
