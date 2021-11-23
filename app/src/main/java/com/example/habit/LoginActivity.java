package com.example.habit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private ImageButton continueButton;
    private EditText emailInput;
    private EditText passwordInput;

    private Button bypasslogin;

    private EditText displayNameInput;
    private TextView displayNamePrompt;

    String email;
    String password;
    String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize input references
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        displayNameInput = findViewById(R.id.displayNameInput);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        bypasslogin = findViewById(R.id.bypasslogin);

        continueButton = findViewById(R.id.signupContinueButton);
        displayNamePrompt = findViewById(R.id.display_name_prompt);


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
        bypasslogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHabits();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get username and password
                String username = ""; // TODO: ADD THIS
                email =  emailInput.getText().toString();
                password = passwordInput.getText().toString();

                // Setup auth for this user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Show display name prompt
                                    loginButton.setVisibility(View.INVISIBLE);
                                    signupButton.setVisibility(View.INVISIBLE);
                                    emailInput.setVisibility(View.INVISIBLE);
                                    passwordInput.setVisibility(View.INVISIBLE);
                                    continueButton.setVisibility(View.VISIBLE);
                                    displayNamePrompt.setVisibility(View.VISIBLE);
                                    displayNameInput.setVisibility(View.VISIBLE);

                                } else {
                                    // If sign up fails, display a message to the user.
                                    Log.w("SIGNUP FAIL", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Account already exists.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayName = displayNameInput.getText().toString();

                if (displayName.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a display name.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNUP SUCCESS", "createUserWithEmail:success");
                    FirebaseUser fb_user = mAuth.getCurrentUser();
//                                    updateUI(user);

                    // Initialize DB
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Get references to user collection
                    CollectionReference usersCollectionRef = db.collection("users");

                    // Create new entry in users collection
                    User user = new User(displayName, "", email, fb_user.getUid());

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

                }

            }
        });
    }


    private void goToHabits() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }
}