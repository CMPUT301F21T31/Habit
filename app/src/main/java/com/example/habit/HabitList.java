package com.example.habit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        // Get habit element layout
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit_content, parent, false);
        }

        Habit habit = habits.get(position);

        // Get field references
        TextView habitName = view.findViewById(R.id.habit_name_text);
        TextView occurence = view.findViewById(R.id.habit_occurence_text);
        ImageView progress = view.findViewById(R.id.habit_progress_bar);

        // Set fields
        habitName.setText(habit.getTitle());

        return view;
    }

}
