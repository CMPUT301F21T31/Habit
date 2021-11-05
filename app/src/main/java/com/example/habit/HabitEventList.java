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

public class HabitEventList extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> habitEvents;
    private Context context;

    public HabitEventList(Context context, ArrayList<HabitEvent> habitEvents) {
        super(context, 0, habitEvents);
        Log.i("EVENT VIEW NOTE", "CREATING HabitEventList");
        this.habitEvents = habitEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.i("EVENT VIEW NOTE", "IN GETVIEW");
        View view = convertView;

        // Get habitEvent element layout
        if (view == null || true) {
            Log.i("EVENT VIEW NOTE", "VIEW NULL");
            view = LayoutInflater.from(context).inflate(R.layout.habit_event_content, parent, false);
        }

        // Get single habit
        HabitEvent habitEvent = habitEvents.get(position);

        // Get field references
        TextView habitEventTitle = view.findViewById(R.id.habit_event_name_text);
        TextView habitEventDate = view.findViewById(R.id.habit_event_date_text);
        ImageView habitEventStatus = view.findViewById(R.id.habit_event_status_bar);

        // Set fields
        habitEventTitle.setText(habitEvent.getComments());

        return view;
    }

}