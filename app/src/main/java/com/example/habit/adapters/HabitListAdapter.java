package com.example.habit.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.habit.R;
import com.example.habit.entities.Habit;
import com.example.habit.entities.User;
import com.example.habit.animations.ProgressBarAnimation;

import java.util.ArrayList;

/**
 * Adapter for displaying either all habits or only today's, with a different view for each option
 * US 01.07.01
 * US 01.08.01
 * US 01.08.02
 * US 01.09.01
 */
public class HabitListAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;
    private Boolean daily;
    private Boolean friendsHabit;
    static Boolean animation = true;

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
     * @param daily Boolean indicating if this is a daily habits list or not
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits, Boolean daily) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.daily = daily;
        this.friendsHabit = false;
    }

    /**
     * Constructor with an additional option to indicate if this list is used to display the habits
     * of a user you are following, in which case it should not have any up or down arrows
     * @param context Context of where this is created
     * @param habits ArrayList of habits to display
     * @param daily Boolean indicating if this is a daily habits list or not
     * @param friendsHabit Boolean indicating if this is a friend's habit list
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits, Boolean daily, Boolean friendsHabit) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.daily = daily;
        this.friendsHabit = friendsHabit;
    }

    /**
     * Get total number of elements in the list
     * @return Integer containing total number of elements in the list
     */
    @Override
    public int getCount() {
        return habits.size();
    }

    /**
     * Get an item at position pos from this list
     * @param pos
     * @return
     */
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
    @RequiresApi(api = Build.VERSION_CODES.O)
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
                view = LayoutInflater.from(context).inflate(R.layout.daily_habit_list_content, parent, false);
            } else {
                // Use all habit content view
                view = LayoutInflater.from(context).inflate(R.layout.all_habit_list_content, parent, false);

                // Get buttons for all view
                upButton = view.findViewById(R.id.moveHabitUpButton);
                downButton = view.findViewById(R.id.moveHabitDownButton);

                // Hide up/down buttons if viewing a friends habit
                if (friendsHabit) {
                    upButton.setVisibility(View.INVISIBLE);
                    downButton.setVisibility(View.INVISIBLE);
                }
            }
        }

        // Get single habit
        habit = habits.get(position);
        animation = true;

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

            int planned = habit.totalPlanned();
            float completedPercent;
            if (planned > 0) {
                completedPercent = ((float) habit.getCompleted() / habit.totalPlanned())*100;
            } else {
                completedPercent = 100;
            }

            if (animation ==true) {
                ProgressBarAnimation baranimation = new ProgressBarAnimation(progress, 0, completedPercent);
                baranimation.setDuration(1000);
                progress.startAnimation(baranimation);
                animation =false;
                // replace 100 with motion data on progress
            }
            else {
                // replace 100 with static data on progress
                progress.setProgress(100);
            }

            // Set fields
            habitName.setText(habit.getTitle());
            occurence.setText(habit.getOccursText());
        }

        // Set up and down button click listeners if in all view
        if (!daily) {

            // Set listener to move the habit up one spot in the list
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

            // Set listener to move the habit down one spot in the list
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
