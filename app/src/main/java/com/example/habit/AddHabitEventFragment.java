package com.example.habit;

import static android.graphics.Bitmap.CompressFormat.PNG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class AddHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText; // TODO: @qg change this
    private EditText commentsEditText;

    ImageButton addButton;
    ImageButton backButton;
    ImageButton photoButton;

    /**
     * Create a new fragment to add a HabitEvent
     * @param habit Habit object to which to add event
     */
     public AddHabitEventFragment(Habit habit) {
         this.habit = habit;
     }

    /**
     * Set fields in fragment view
     * @param savedInstanceState Previous state
     * @return Dialog view of HabitEvent
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit_event, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");

        // Get EditTexts
        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);

        // Set add button
        addButton = view.findViewById(R.id.addHabitEventButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // photo = ...; // TODO: @qg Properly get and set photo here
                String location = locationEditText.getText().toString(); // TODO: @qg Properly get and set location here, will have to change from string
                String comments = commentsEditText.getText().toString();

                // Create HabitEvent and add to DB
                habitEvent = new HabitEvent(location, comments); // TODO: @qg Will have to add a "photo" property to the HabitEvent class and change the constructor
                Habit.addEvent(habit.getHabitId(), habitEvent);

                // Close dialog
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set back button
        backButton = view.findViewById(R.id.backHabitEventButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set photo button
        photoButton = view.findViewById(R.id.habitEventPhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: @qg Add what happens when use clicks the photo button in HabitEvent fragment
            }
        });

        // Build and return dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }

    /**
     * Create a new fragment view
     * @param inflater Inflater to use
     * @param container Container for dialog
     * @param savedInstanceState Previous state
     * @return View for HabitEvent fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        int width = getResources().getDimensionPixelSize(R.dimen.habitEvent_fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.habitEvent_fragment_height);
        getDialog().getWindow().setLayout(width, height);
    }
}