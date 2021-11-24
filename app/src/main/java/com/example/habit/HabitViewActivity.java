package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

public class HabitViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_view);

        // How is habit getting passed in? Intent/Bundle?
        Habit habit = new Habit();

        // Links to UI
        TextView title = findViewById(R.id.tvTitle);
        TextView reason = findViewById(R.id.tvReason);
        TextView start = findViewById(R.id.tvStartDate);
        TextView end = findViewById(R.id.tvEndDate);
        LinearLayout daysContainer = findViewById(R.id.daysLayout);

        /**
         * Constructor for a new habit TODO: Should some of these be optional?
         * @param title What this habit is called
         * @param reason Motivation/goal for this habit
         * @param start When the habit should start (just date, no time)
         * @param end When the habit should end (just date, no time)
         * @param daysOfWeek Hashmap with weekday keys and a boolean indicating if the habit occurs on that day
         * @param events Instances of this habit occurring
         */

        // Set values in View
        title.setText(habit.getTitle());
        reason.setText(habit.getReason());
        start.setText(habit.getStart().toString());
        end.setText(habit.getEnd().toString());

        // Iterate through days of the week, mark those days in UI
        Map<String, Boolean> days = habit.getDaysOfWeek();
        for (Map.Entry<String, Boolean> day : days.entrySet()) {
            if (day.getKey().equals("Monday")) {

            } else if (day.getKey().equals("Tuesday")) {

            } else if (day.getKey().equals("Wednesday")) {

            } else if (day.getKey().equals("Thursday")) {

            } else if (day.getKey().equals("Friday")) {

            } else if (day.getKey().equals("Saturday")) {

            } else if (day.getKey().equals("Sunday")) {

            }
        }
    }
}