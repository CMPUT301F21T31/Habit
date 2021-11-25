package com.example.habit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Displays either all user habits or only today's with a different view for each
 */
public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;
    private Boolean daily;
    private User user;

    // Buttons to move habit up or down
    ImageButton upButton;
    ImageButton downButton;

    // Habit that was clicked on and the habit we will swap positions with
    Habit habit;
    Habit swapHabit;

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

    @Override
    public int getCount() {
        return habits.size();
    }

    @Override
    public Habit getItem(int pos) {
        return habits.get(pos);
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

        // Get view reference
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

                // Get buttons for all view
                upButton = view.findViewById(R.id.moveHabitUpButton);
                downButton = view.findViewById(R.id.moveHabitDownButton);
            }
        }

        // Get single habit
        habit = habits.get(position);

        // Set fields depending on which type of HabitList this is
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
            ProgressBar progress = view.findViewById(R.id.habit_progress_bar);
            progressbaranimation baranimation = new progressbaranimation(progress, 0, 100);
            baranimation.setDuration(1000);
            progress.startAnimation(baranimation);

            // Set fields
            habitName.setText(habit.getTitle());
        }

        // Set up and down button click listeners if in all view
        if (!daily) {
            upButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * Update the stored index of this habit, which will be reflected by the snapshot
                 * listener in HabitListActivity
                 * @param v View of the element whose up button was clicked
                 */
                @Override
                public void onClick(View v) {

                    // Get position of this habit
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow);

                    // Only move if not the first element in list
                    if (position != 0) {

                        // Get habit and habit to swap with
                        habit = habits.get(position);
                        swapHabit = habits.get(position - 1);

                        // Adjust array in database
                        habit.setListPosition(position - 1);
                        swapHabit.setListPosition(position);
                        User.updateHabit(habit.getHabitId(), habit);
                        User.updateHabit(swapHabit.getHabitId(), swapHabit);
                    }
                }
            });

            downButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * Update the stored index of this habit, which will be reflected by the snapshot
                 * listener in HabitListActivity
                 * @param v View of the element whose down button was clicked
                 */
                @Override
                public void onClick(View v) {

                    // Get position of this habit
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow);

                    // Only move if not the last element in list
                    if (position < (habits.size() - 1)) {

                        // Get habit and habit to swap with
                        habit = habits.get(position);
                        swapHabit = habits.get(position + 1);


                        // Adjust array in database
                        habit.setListPosition(position + 1);
                        swapHabit.setListPosition(position);
                        User.updateHabit(habit.getHabitId(), habit);
                        User.updateHabit(swapHabit.getHabitId(), swapHabit);
                    }
                }
            });
        }

        return view;
    }
}
