package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HabitEventViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_view);

        // Get HabitEvent from Intent
        HabitEvent hb = new HabitEvent("Kinsmen Swimming Pool", "Went for a 1000m IM, 500m freestyle");

        TextView locationTv = findViewById(R.id.locationTv);
        TextView commentsTv = findViewById(R.id.commentsTv);

        locationTv.setText(hb.getLocation());
        commentsTv.setText(hb.getComments());
    }
}