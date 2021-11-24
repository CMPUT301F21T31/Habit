package com.example.habit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment to display a HabitEvent
 */
public class ViewEditHabitEventFragment extends DialogFragment {

    private HabitEvent habitEvent;
    private Habit habit;

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
        TextView titleView = view.findViewById(R.id.habit_event_title);
        TextView commentsView = view.findViewById(R.id.comments_edit_text);
        TextView locationView = view.findViewById(R.id.location_edit_text);
        titleView.setText(habit.getTitle() + " Event");
        commentsView.setText(habitEvent.getComments());
        locationView.setText(habitEvent.getLocation());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .create();
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
}