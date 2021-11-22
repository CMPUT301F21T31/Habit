package com.example.habit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Displays either all user habits or only today's with a different view for each
 */
public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;
    private Boolean daily;

    /**
     * Create a new HabitList to display either daily or all habits
     * @param context Context of where this is created
     * @param habits ArrayList of habits to display
     * @param daily Is this a daily list
     */
    public HabitList(Context context, ArrayList<Habit> habits, Boolean daily) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.daily = daily;
    }

    /**
     * Get a View for element in list
     * @param position Integer position in list
     * @param convertView View to use
     * @param parent Parent view of the element
     * @return View for element
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        // Get habit element layout
        if (view == null) {
            Log.i("HABIT VIEW NOTE", "VIEW NULL");
            if (daily) {
                // Use daily habit content view
                view = LayoutInflater.from(context).inflate(R.layout.daily_habit_content, parent, false);
            } else {
                // Use all habit content view
                view = LayoutInflater.from(context).inflate(R.layout.all_habit_content, parent, false);
            }
        }

        // Get single habit
        Habit habit = habits.get(position);

        if (daily) {
            // Get field references
            TextView habitName = view.findViewById(R.id.habit_name_text);
            ImageView status = view.findViewById(R.id.habit_status_bar_daily);
            // Set fields
            habitName.setText(habit.getTitle());
        } else {
            // Get field references
            TextView habitName = view.findViewById(R.id.habit_name_text);
            TextView occurence = view.findViewById(R.id.habit_occurence_text);
            ImageView progress = view.findViewById(R.id.habit_progress_bar);
            // Set fields
            habitName.setText(habit.getTitle());
        }
        return view;
    }
}
