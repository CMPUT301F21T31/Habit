package com.example.habit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

/**
 * US 05.01.01
 */
public class sendRequest extends DialogFragment {

    private FirebaseAuth auth;
    FirebaseUser user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_send_friend_request, null);
        EditText friendEmail = view.findViewById(R.id.request_email);
        ImageButton back = view.findViewById(R.id.backFriendActivityButton);
        ImageButton confirmSend = view.findViewById(R.id.confirmRequestButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        confirmSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = friendEmail.getText().toString();

                if (getEmail.equals("")) {
                    Toast.makeText(getContext(),"Please enter the email", Toast.LENGTH_LONG).show();
                }
                else {
                    User.sendRequest(getContext(), user.getUid(), getEmail);
                    Objects.requireNonNull(getDialog()).dismiss();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }
}
