package com.example.habit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habit.R;
import com.example.habit.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * US 05.01.01
 * This class is creating a dialog fragment to sending a follow request to other users
 * @author lewisning
 * @see android.app.Activity
 * @see android.content.Context
 */
public class SendRequestFragment extends DialogFragment {

    private FirebaseAuth auth;
    FirebaseUser user;

    /**
     * Create a new send request fragment view
     * @param inflater Inflater to use
     * @param container Container for dialog
     * @param savedInstanceState Previous state
     * @return View for SendRequest fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * This method is focusing on the functionalities of all items on the dialog.
     * @param savedInstanceState Previous instance variables
     * @return Dialog view of HabitEvent
     */
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
