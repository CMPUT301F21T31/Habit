package com.example.habit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class tutorial_3emailpassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailInput;
    EditText passwordInput;
    Button continueButton;
    Intent last;
    String username;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3emailpassword);

        // Get previous data
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        username = prefs.getString("username", "");
        displayName = prefs.getString("firstStartname", "");

        // Get view and button references
        emailInput = findViewById(R.id.tutorial_email_input);
        passwordInput = findViewById(R.id.tutorial_pwd_input);
        continueButton = findViewById(R.id.tutorial_emailpwd_continue);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get username and password
                String email =  emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Setup auth for this user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(tutorial_3emailpassword.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SIGNUP SUCCESS", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);

                                    // Initialize DB
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    // Get references to user collection
                                    CollectionReference usersCollectionRef = db.collection("users");

                                    // Create new entry in users collection
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("email", email);
                                    data.put("username", username);
                                    data.put("displayName", displayName);

                                    // Add to users collection
                                    usersCollectionRef
                                        .document(user.getUid()) // Use Uid as key for users
                                        .set(data)
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
                                    Intent intent = new Intent(tutorial_3emailpassword.this, tutorial_4habit.class);
                                    startActivity(intent);

                                } else {
                                    // If sign up fails, display a message to the user.
                                    Log.w("SIGNUP FAIL", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(tutorial_3emailpassword.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}