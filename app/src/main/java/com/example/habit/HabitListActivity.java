package com.example.habit;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class HabitListActivity extends AppCompatActivity {

    // Firebase references
    private FirebaseAuth auth;
    FirebaseFirestore db;

    FirebaseUser fb_user;
    User user;

    // Habit list
    ListView habitList;
    ArrayList<Habit> habitDataList;
    HabitList habitAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Initialize auth and db
        auth = FirebaseAuth.getInstance();
        fb_user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize habit list
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);
        habitList = findViewById(R.id.habit_list);
        habitList.setAdapter(habitAdapter);

        Log.i("HabitList", "In onCreate");

        if (fb_user == null) {
            // Go back to login
        } else {

            // Add some habits (TEST)
            // testDB();

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

                                            // Clear old habits
                                            habitDataList.clear();
                                            for (QueryDocumentSnapshot doc : value) {
                                                    // Get all changed habits and add to list
                                                    Log.d("CHANGE", doc.toObject(Habit.class).toString());
                                                    habitDataList.add(doc.toObject(Habit.class));
                                            }
                                            habitAdapter.notifyDataSetChanged();
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
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openEditHabit(i);
            }
        });

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
        Habit mockHabit2 = new Habit("Reading", "Read 3 books", new Date(), new Date(), daysOfWeek1);
        Habit mockHabit3 = new Habit("Eating", "Eat a salad", new Date(), new Date(), daysOfWeek1);
        Habit mockHabit4 = new Habit("Studying", "Get an A+", new Date(), new Date(), daysOfWeek1);

        // Instantiate mock habit events
        HabitEvent mockHabitEvent1 = new HabitEvent("Edmonton", "Swam 20 laps");

        // Add habit to both the habits collection and habit list for this user
        User.addHabit(fb_user.getUid(), mockHabit1);
        User.addHabit(fb_user.getUid(), mockHabit2);
        User.addHabit(fb_user.getUid(), mockHabit3);
        User.addHabit(fb_user.getUid(), mockHabit4);
//        Habit.addEvent("F5h5jNzuOCBZx5ptZQ0e", mockHabitEvent1);
//        User.deleteHabit("Ac5hod5NHwQUTo40T3Wf");
//        User.addHabit(user.getUid(), mockHabit1);
//        try {
//            sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        User.deleteHabit(user.getUid(),"QBQabjShu9C0l2gZVrT6");
//        Habit.deleteEvent("F5h5jNzuOCBZx5ptZQ0e", "hJuy8BPeEQ0BH026Mv4W");
    }

    private void openAddHabit() {
        Intent intent = new Intent(this, addHabit.class);
        startActivity(intent);
    }

    private void openEditHabit(int i) {
        Intent intent = new Intent(this, editHabit.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST", (Serializable)habitDataList);
        intent.putExtra("BUNDLE", args);
        intent.putExtra("item_index", i);
        startActivity(intent);
    }

}