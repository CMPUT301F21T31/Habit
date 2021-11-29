package com.example.habit;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * US 05.03.01
 * This class is viewing all events of each PUBLIC habits belongs to the current user followers.
 * All text boxes and image in the interface are viewable ONLY.
 * @author lewisning
 * @see android.app.Activity
 * @see android.content.Context
 */
public class viewOnlyHabitEvent extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private TextView locationTextView; // TODO: @qg change this
    private TextView commentsTextView;

    ImageButton backButton;

    /**
     * Get a new fragment to display a HabitEvent
     * @param habitEvent HabitEvent to display
     * @param habit Parent habit
     */
    public viewOnlyHabitEvent(HabitEvent habitEvent, Habit habit) {
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.habitevent_view_only, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");

        // Get EditTexts
        locationTextView = view.findViewById(R.id.location_view_text);
        commentsTextView = view.findViewById(R.id.comments_view_text);

        // Set text fields
//        locationTextView.setText(habitEvent.getLocation());
        commentsTextView.setText(habitEvent.getComments());

        // Set back button
        backButton = view.findViewById(R.id.backHabitEventButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
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
