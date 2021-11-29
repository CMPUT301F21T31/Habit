package com.example.habit.adapters;

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

import com.example.habit.entities.Habit;
import com.example.habit.entities.HabitEvent;
import com.example.habit.R;

import java.util.ArrayList;

/**
 * List containing all HabitEvents for a given Habit
 */
public class HabitEventListAdapter extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> habitEvents;
    private Context context;
    private Habit parentHabit;

    /**
     * Create a HabitEventList
     * @param context Context of where this is created
     * @param habitEvents List of events to display
     */
    public HabitEventListAdapter(Context context, ArrayList<HabitEvent> habitEvents, Habit parentHabit) {
        super(context, 0, habitEvents);
        Log.i("EVENT VIEW NOTE", "CREATING HabitEventList");
        this.habitEvents = habitEvents;
        this.context = context;
        this.parentHabit = parentHabit;
    }

    /**
     * Get view for element in list
     * @param position int position of element
     * @param convertView view to use
     * @param parent parent viewgroup of list element
     * @return View for HabitEvent element
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.i("EVENT VIEW NOTE", "IN GETVIEW");
        View view = convertView;

        // Get habitEvent element layout
        if (view == null || true) {
            Log.i("EVENT VIEW NOTE", "VIEW NULL");
            view = LayoutInflater.from(context).inflate(R.layout.habit_event_list_content, parent, false);
        }

        // Get single habit event
        HabitEvent habitEvent = habitEvents.get(position);

        // Get field references
        TextView habitEventTitle = view.findViewById(R.id.habit_event_name_text);
        TextView habitEventDate = view.findViewById(R.id.habit_event_date_text);
        ImageView habitEventStatus = view.findViewById(R.id.habit_event_status_bar);

        // Set fields
        habitEventTitle.setText(parentHabit.getTitle() + " Event");
        // habitEventTitle.setText(habitEvent.getComments());

        return view;
    }

}
