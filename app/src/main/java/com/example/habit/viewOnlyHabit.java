package com.example.habit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class viewOnlyHabit extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db;

    ImageButton back;
    Button Monday;
    Button Tuesday;
    Button Wednesday;
    Button Thursday;
    Button Friday;
    Button Saturday;
    Button Sunday;
    TextView title;
    TextView reason;
    TextView startMonth;
    TextView startDay;
    TextView startYear;
    TextView endMonth;
    TextView endDay;
    TextView endYear;
    ListView habitEventsListView;
    ArrayList<HabitEvent> habitEventsDataList;
    HabitEventList habitEventAdapter;

    int Syear;
    int Smonth;
    int Sday;

    Timestamp startTime;
    Timestamp endTime;
    HashMap<String, Boolean> selected_date = null;
    ArrayList<Boolean> recurrence = new ArrayList<Boolean>(
            Arrays.asList(false, false, false, false, false, false, false));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_habit_details);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        title = findViewById(R.id.habit_title);
        reason = findViewById(R.id.habit_reason);
        back = findViewById(R.id.back);
        startMonth = findViewById(R.id.start_month);
        startDay = findViewById(R.id.start_day);
        startYear = findViewById(R.id.start_year);
        endMonth = findViewById(R.id.end_month);
        endDay = findViewById(R.id.end_day);
        endYear = findViewById(R.id.end_year);
        Monday = findViewById(R.id.monday);
        Tuesday = findViewById(R.id.tuesday);
        Wednesday = findViewById(R.id.wednesday);
        Thursday = findViewById(R.id.thursday);
        Friday = findViewById(R.id.friday);
        Saturday = findViewById(R.id.saturday);
        Sunday = findViewById(R.id.sunday);

        // After enter the edit interface, the existing information should replace the hints
        User curr_user;

        Habit selected_habit = getIntent().getExtras().getParcelable("habit");
        Log.i("GOT HABIT", selected_habit.toString());

        // Setup habit events list
        habitEventsDataList = new ArrayList<>();
        habitEventAdapter = new HabitEventList(this, habitEventsDataList, selected_habit);
        habitEventsListView = findViewById(R.id.habit_event_list);
        habitEventsListView.setAdapter(habitEventAdapter);

        title.setText(selected_habit.getTitle());
        reason.setText(selected_habit.getReason());

        Date sDate = selected_habit.getStart();
        Calendar cal = Calendar.getInstance(); // Add this
        cal.setTime(sDate);
        int sd;
        int sm;
        int sy;

        sd = cal.get(Calendar.DAY_OF_MONTH);
        sm = cal.get(Calendar.MONTH);
        sy = cal.get(Calendar.YEAR);

        startDay.setText(String.valueOf(sd));
        startMonth.setText(String.valueOf(sm));
        startYear.setText(String.valueOf(sy));

        Date eDate = selected_habit.getEnd();
        cal.setTime(eDate);
        int ed;
        int em;
        int ey;

        ed = cal.get(Calendar.DAY_OF_MONTH);
        em = cal.get(Calendar.MONTH);
        ey = cal.get(Calendar.YEAR);

        endDay.setText(String.valueOf(ed));
        endMonth.setText(String.valueOf(em));
        endYear.setText(String.valueOf(ey));

        // Get the occurrence status
        if (selected_habit.getDaysOfWeek().get("Monday")) {
            Monday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Tuesday")) {
            Tuesday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Wednesday")) {
            Wednesday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Thursday")) {
            Thursday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Friday")) {
            Friday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Saturday")) {
            Saturday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }
        if (selected_habit.getDaysOfWeek().get("Sunday")) {
            Sunday.setBackground(getDrawable(R.drawable.round_button_clicked));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Display the habit events
        db.collection("habitEvents")
                .whereEqualTo("habitId", selected_habit.getHabitId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        // Log failure and exit
                        if (error != null) {
                            Log.w("FAILED", "Listen failed.", error);
                            return;
                        }

                        // Clear old events
                        habitEventsDataList.clear();
                        Log.i("CHANGE HABIT EVENT", "After clear");

                        for (QueryDocumentSnapshot doc : value) {
                            Log.i("CHANGE HABIT EVENT", "IN LOOP");
                            HabitEvent habitEvent = doc.toObject(HabitEvent.class);
                            Log.i("CHANGE HABIT EVENT", habitEvent.toString());
                            habitEventsDataList.add(habitEvent);
                        }
                        Log.i("CHANGE HABIT EVENT", "Notify adapter");
                        habitEventAdapter.notifyDataSetChanged();
                    }
                });

        habitEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HABIT EVENT", "Item clicked");
                new viewOnlyHabitEvent(habitEventAdapter.getItem(position), selected_habit).show(getSupportFragmentManager(), "VIEW/EDIT_HabitEvent");
            }
        });
    }
}
