package com.example.habit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

/**
 * Fragment to display a HabitEvent
 */
public class ViewEditHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText; // TODO: @qg change this
    private EditText commentsEditText;

    ImageButton backButton;
    ImageButton deleteButton;
    ImageButton editButton;
    ImageButton photoButton;

    /**
     * Get a new fragment to display a HabitEvent
     * @param habitEvent HabitEvent to display
     * @param habit Parent habit
     */
    public ViewEditHabitEventFragment(HabitEvent habitEvent, Habit habit) {
        this.habitEvent = habitEvent;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_edit_habit_event, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");

        // Get EditTexts
        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);

        // Set text fields
        locationEditText.setText(habitEvent.getLocation());
        commentsEditText.setText(habitEvent.getComments());

        // Set edit button
        editButton = view.findViewById(R.id.addHabitEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // photo = ...; // TODO: @qg Properly get and set photo here
                String location = locationEditText.getText().toString(); // TODO: @qg Properly get and set location here, will have to change from string
                String comments = commentsEditText.getText().toString();

                // Update the fields that changed
                // TODO: @qg add another if block to check if user added a photo
                if (!location.isEmpty()) {
                    // TODO: @qg change this to handle location that isn't a string
                    habitEvent.setLocation(location);
                }

                if (!comments.isEmpty()) {
                    habitEvent.setComments(comments);
                }

                // Update habit in DB
                Habit.updateHabitEvent(habitEvent.getHabitEventId(), habitEvent);

                // Close dialog
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set delete button
        deleteButton = view.findViewById(R.id.deleteHabitEventButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit.deleteEvent(habit.getHabitId(), habitEvent.getHabitEventId());
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