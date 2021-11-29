package com.example.habit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habit.R;
import com.example.habit.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText emailInput;
    private EditText passwordInput;
    private EditText displayNameInput;
    private TextView displayNamePrompt;
    private TextView loginPrompt;

    private Button loginButton;
    private Button signupButton;
    private ImageButton continueButton;

    private CheckBox rememberUserView;

    String email;
    String password;
    String displayName;
    Boolean rememberUser = false; // Default to false

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        displayNameInput = findViewById(R.id.displayNameInput);
        displayNamePrompt = findViewById(R.id.display_name_prompt);
        loginPrompt = findViewById(R.id.loginPrompt);

        // Initialize buttons
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
        continueButton = findViewById(R.id.signupContinueButton);

        // Initialize checkbox
        rememberUserView = findViewById(R.id.rememberUser);

        // Check if user is here because they clicked the logout button
        Intent intent = getIntent();
        Boolean logoutClicked  = intent.getBooleanExtra("logoutClicked", false);

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Get firebase instance TODO: What to do if this fails
        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
        }

        if (currentUser != null && !logoutClicked) {

            // Get user from DB
            db.collection("users")
                    .document(mAuth.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                // Set user
                                user = task.getResult().toObject(User.class);

                                // Handle potential error if user has an old account
                                if (user == null || user.getStayLoggedIn() == null) {
                                    return;
                                } else if (user.getStayLoggedIn()) {
                                    // Go to habits if this user has checked remember me in the past
                                    goToHabits();
                                } else if (user.getEmail() != null) {
                                    // Otherwise just fill in email
                                    emailInput.setText(user.getEmail());
                                }

                            } else {

                                // Display error if we couldn't get user object
                                Toast.makeText(getBaseContext(),"Error getting user info. Please signup or login again.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username and password
                email =  emailInput.getText().toString();
                password = passwordInput.getText().toString();
                rememberUser = rememberUserView.isChecked();

                // Ensure user entered an email and password. Tell them to if not.
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter an email and password.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter an email.",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a password.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Attempt login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("LOGIN SUCCESS", "signInWithEmail:success");

                                    // Update user object if checked "Remember me"
                                    if (rememberUser || user == null || user.getStayLoggedIn() == null) {
                                        db.collection("users")
                                                .document(currentUser.getUid())
                                                .update("stayLoggedIn", rememberUser);
                                    }

                                    // Go to main page
                                    goToHabits();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGIN FAIL", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username and password
                email =  emailInput.getText().toString();
                password = passwordInput.getText().toString();
                rememberUser = rememberUserView.isChecked();

                // Ensure user entered an email and password. Tell them to if not.
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter an email and password.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                else if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter an email.",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter a password.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

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
                                    rememberUserView.setVisibility(View.INVISIBLE);
                                    loginPrompt.setVisibility(View.INVISIBLE);

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
                    User user = new User(displayName, email, fb_user.getUid(), rememberUser);

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

                    // After successful signup, go to main page
                    goToHabits();
                }
            }
        });

        signupButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    signupButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_clicked));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    signupButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_not_clicked));
                    v.performClick();
                }
                return true;
            }
        });

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    loginButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_clicked));
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    loginButton.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_not_clicked));
                    v.performClick();
                }
                return true;
            }
        });
    }

    private void goToHabits() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }
}