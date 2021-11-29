package com.example.habit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

/**
 * US 05.03.01
 * This class is creating a list PUBLIC habits belongs to the current user followers.
 * @author lewisning
 * @see android.app.Activity
 * @see android.content.Context
 */
public class FriendsHabitActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db;

    FirebaseUser fb_user;
    User user;

    TextView title;
    ListView habitList;
    ImageButton back;
    ArrayList<Habit> dataList;
    HabitListAdapter habitListAdapter;

    /**
     * This method is including updating database and setting the functionalities for all items
     * on the interface.
     * Working with friends_habit.xml layout file
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_habit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.title_with_name);
        habitList = findViewById((R.id.habit_view_list));
        back = findViewById((R.id.back));

        dataList = new ArrayList<>();
        habitListAdapter = new HabitListAdapter(this, dataList, false, true);
        habitList.setAdapter(habitListAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String userId = intent.getExtras().getString("user id");

        // Get User record for logged in user
        DocumentReference docRef = db
                .collection("users")
                .document(userId);
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
                                .whereEqualTo("userId", userId) // Only get habits for this user
                                .whereEqualTo("ifPublic", true)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                        // Log failure and exit
                                        if (error != null) {
                                            Log.w("FAILED", "Listen failed.", error);
                                            return;
                                        }

                                        // Clear old habits
                                        dataList.clear();

                                        for (QueryDocumentSnapshot doc : value) {
                                            // Get all changed habits and add to list
                                            Habit habit = doc.toObject(Habit.class);
                                            Log.d("CHANGE", doc.toObject(Habit.class).toString());
                                            dataList.add(habit);
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

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(FriendsHabitActivity.this, ViewOnlyHabitActivity.class);
                Habit habitToView = habitListAdapter.getItem(position);
                intent1.putExtra("habit", habitToView);
                startActivity(intent1);
             }
        });

    }
}
