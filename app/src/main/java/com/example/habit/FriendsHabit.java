package com.example.habit;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Date;

public class FriendsHabit extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db;

    FirebaseUser fb_user;
    User user;

    TextView title;
    ListView habitList;
    ArrayList<Habit> dataList;
    HabitList habitListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_habit);

        auth = FirebaseAuth.getInstance();
        fb_user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.title_with_name);
        habitList = findViewById((R.id.habit_view_list));

        dataList = new ArrayList<>();
        habitListAdapter = new HabitList(this, dataList, false);
        habitList.setAdapter(habitListAdapter);

        if (fb_user == null) {
            // Go back to login
        } else {

            // Get User record for logged in user
            DocumentReference docRef = db
                    .collection("users")
                    .document(fb_user.getUid());
            docRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // Get user object and their habit IDs
                            user = document.toObject(User.class);
                            Habit habit = document.toObject(Habit.class);
                            title.setText(user.getDisplayName() + "'s Habits");

                            ArrayList<String> habits = user.getHabits();

                            // On change listener for habits belonging to this user
                            db.collection("habits")
                                    .whereEqualTo("userId", fb_user.getUid()) // Only get habits for this user
                                    .whereEqualTo(String.valueOf(true), habit.getIfPublic())
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
                                            dataList.clear();

                                            for (QueryDocumentSnapshot doc : value) {
                                                // Get all changed habits and add to list
                                                Habit habit = doc.toObject(Habit.class);
                                                Log.d("CHANGE", doc.toObject(Habit.class).toString());
                                                dataList.add(habit);

                                                // Add to daily list if Habit occurs today
                                                if (habit.isOnDay(weekday)) {
                                                    dataList.add(habit);
                                                }
                                            }
                                            habitListAdapter.notifyDataSetChanged();
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
    }
}
