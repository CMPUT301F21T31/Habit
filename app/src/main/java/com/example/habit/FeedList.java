package com.example.habit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FeedList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;
    private Boolean daily;

    public FeedList(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
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
        view = LayoutInflater.from(context).inflate(R.layout.feed_content, parent, false);
        return view;
    }
}
