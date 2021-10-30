package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTests {

    // Initialize DB and auth
    FirebaseFirestore db;
    private FirebaseAuth auth;
    private CountDownLatch authSignal = null;

    // Get references to each collection
    CollectionReference usersCollectionRef;
    CollectionReference habitsCollectionRef;
    CollectionReference habitEventsRef;

    // Mock objects
    User mockUser1;
    User mockUser2;
    Habit mockHabit1;
    Habit mockHabit2;
    HabitEvent mockHabitEvent1;
    HabitEvent mockHabitEvent2;

    @BeforeAll
    void setup() throws InterruptedException {

        // Instantiate DB
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        authSignal = new CountDownLatch(1);

        // User account for testing
        String email = "jboileau@ualberta.ca";
        String password = "testpwd";

        // Sign in to DB as test user
//        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password);
//        authResultTask.wait();
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("urbi@orbi.it", "12345678").addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            final AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            authSignal.countDown();
                        }
                    });
        } else {
            authSignal.countDown();
        }
        authSignal.await(10, TimeUnit.SECONDS);

        // Get references to each collection
        usersCollectionRef = db.collection("users");
        habitsCollectionRef = db.collection("habits");
        habitEventsRef = db.collection("habitEvents");

        // Instantiate mock users
        mockUser1 = new User("John", "jsmith49");
        mockUser2 = new User("Karla", "krox99");


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


        mockHabit1 = new Habit("Swimming", "Get fit", new Date(), new Date(), daysOfWeek1);
        mockHabit2 = new Habit("Reading", "Learn", new Date(), new Date(), daysOfWeek2);

        // Instantiate mock habit events
        mockHabitEvent1 = new HabitEvent("Edmonton", "Swam 20 laps");
        mockHabitEvent2 = new HabitEvent("Bedroom", "Finished chapter 7");
    }

    @Test
    void testAddUser() {

//        mockUser1.addHabit(mockHabit1, usersCollectionRef, habitsCollectionRef);
        assertEquals(0, 0);
    }

    @Test
    void testAddHabit() {
        assertEquals(0, 0);
    }

    @Test
    void testAddHabitEvent() {

    }

    @Test
    void testGetUser() {

    }

    @Test
    void testGetHabit() {

    }

    @Test
    void testGetHabitEvent() {

    }

    @Test
    void testEditUser() {

    }

    @Test
    void testEditHabit() {

    }

    @Test
    void testEditHabitEvent() {

    }

    @Test
    void testDeleteUser() {

    }

    @Test
    void testDeleteHabit() {

    }

    @Test
    void testDeleteHabitEvent() {

    }
}
