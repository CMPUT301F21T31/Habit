package com.example.habit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginButton;
    private Button signupButton;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize input references
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        // Check if user is logged in --> Change this to determine which activity to load
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this,"Logged in",Toast.LENGTH_LONG).show();
            Toast.makeText(this, currentUser.getUid(),Toast.LENGTH_LONG).show();
            goToHabits();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username and password
                String email =  emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Attempt login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOGIN SUCCESS", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                        goToHabits();
//                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGIN FAIL", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get username and password
                String displayName = ""; // TODO: ADD THIS
                String username = ""; // TODO: ADD THIS
                String email =  emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Setup auth for this user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SIGNUP SUCCESS", "createUserWithEmail:success");
                                    FirebaseUser fb_user = mAuth.getCurrentUser();
//                                    updateUI(user);

                                    // Initialize DB
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    // Get references to user collection
                                    CollectionReference usersCollectionRef = db.collection("users");

                                    // Create new entry in users collection
                                    User user = new User(displayName, username, email);

                                    // Add to users collection
                                    usersCollectionRef
                                            .document(fb_user.getUid()) // Use Uid as key for users
                                            .set(user)
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

                                    // Move to next stage of tutorial
                                    Intent intent = new Intent(LoginActivity.this, HabitListActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign up fails, display a message to the user.
                                    Log.w("SIGNUP FAIL", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void goToHabits() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }
}