package com.example.habit;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * US 01.04.01
 * This class is creating an interface for editing the details of habit
 * @author lewisning, Chen Xu
 * @see android.app.Activity
 * @see android.content.Context
 */
public class editHabit extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;

    ImageButton back;
    ImageButton delete;
    ImageButton confirm;
    ImageButton startDate;
    ImageButton endDate;
    ImageButton addHabitEventButton;
    Button Monday;
    Button Tuesday;
    Button Wednesday;
    Button Thursday;
    Button Friday;
    Button Saturday;
    Button Sunday;
    Switch ifPublic;
    EditText title;
    EditText reason;
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

    /**
     * This method is implementing the edit habit interface
     * Working with edit_habit.xml layout file.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_habit);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        title = findViewById(R.id.habit_title);
        reason = findViewById(R.id.habit_reason);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        confirm = findViewById(R.id.confirm);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        startMonth = findViewById(R.id.start_month);
        startDay = findViewById(R.id.start_day);
        startYear = findViewById(R.id.start_year);
        endMonth = findViewById(R.id.end_month);
        endDay  = findViewById(R.id.end_day);
        endYear = findViewById(R.id.end_year);
        Monday = findViewById(R.id.monday);
        Tuesday = findViewById(R.id.tuesday);
        Wednesday = findViewById(R.id.wednesday);
        Thursday = findViewById(R.id.thursday);
        Friday = findViewById(R.id.friday);
        Saturday = findViewById(R.id.saturday);
        Sunday = findViewById(R.id.sunday);
        addHabitEventButton = findViewById(R.id.addHabitEventButton2);
        ifPublic = findViewById(R.id.edit_public);

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
        sm = cal.get(Calendar.MONTH) + 1;
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
        em = cal.get(Calendar.MONTH) + 1;
        ey = cal.get(Calendar.YEAR);

        endDay.setText(String.valueOf(ed));
        endMonth.setText(String.valueOf(em));
        endYear.setText(String.valueOf(ey));

        final boolean[] myButtonIsClicked = {false};

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


        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Monday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(0, true);
                    Monday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Tuesday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(1, true);
                    Tuesday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Wednesday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(2, true);
                    Wednesday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Thursday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(3, true);
                    Thursday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Friday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(4, true);
                    Friday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Saturday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(5, true);
                    Saturday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        Sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Sunday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
                    recurrence.set(6, true);
                    Sunday.setBackground(getDrawable(R.drawable.round_button_clicked));
                    myButtonIsClicked[0] = true;
                }
            }
        });

        // Back to the main screen
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Delete this habit
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Should we have some kind of confirm alert box?
                User.deleteHabit(user.getUid(), selected_habit.getHabitId(), selected_habit.getListPosition());
                finish();
            }
        });

        // 1. Click the calendar sign to select the start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(editHabit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startYear.setText("" + year);
                                startMonth.setText("" + (month + 1));
                                startDay.setText("" + dayOfMonth);
                                startTime = new Timestamp(calendar.getTimeInMillis());
                                Sday = dayOfMonth;
                                Smonth = month;
                                Syear = year;
                            }
                        }, year, month, dayOfMonth);
                startDateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                startDateDialog.show();
            }
        });

        // 2. Click the calendar sign to select the end date
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog endDateDialog = new DatePickerDialog(editHabit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endYear.setText("" + year);
                                endMonth.setText("" + (month + 1));
                                endDay.setText("" + dayOfMonth);
                                endTime = new Timestamp(calendar.getTimeInMillis());
                            }
                        }, year, month, dayOfMonth);
                calendar.set(Syear, Smonth, Sday);
                endDateDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                endDateDialog.show();
            }
        });

        // Add a habit event for this habit
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddHabitEventFragment(selected_habit)
                        .show(getSupportFragmentManager(), "ADD_HabitEvent");
            }
        });

        // Get the current public status
        Boolean publicStatus = selected_habit.getIfPublic();
        ifPublic.setChecked(publicStatus);
        ifPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_habit.setIfPublic(ifPublic.isChecked());
                User.updateHabit(selected_habit.getHabitId(), selected_habit);
            }
        });

        // Passing the recurrence to the habit after clicked the confirm button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Get and set the habit title
                String habit_title = title.getText().toString();
                selected_habit.setTitle(habit_title);

                // 2. Get and set the habit reasons
                String habit_reason = reason.getText().toString();
                selected_habit.setReason(habit_reason);

                // 3. Set the recurrence
                try {
                    selected_date = Habit.generateDaysDict(recurrence);
                    selected_habit.setDaysOfWeek(selected_date);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 4. Set the start and end dates
                selected_habit.setStart(startTime);
                selected_habit.setStart(endTime);

                User.updateHabit(selected_habit.getHabitId(), selected_habit);
                finish();
            }
        });
        Log.i("Habit event info", selected_habit.getHabitId());
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

                        Log.i("Habit event info", "CHANGE");

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
                new ViewEditHabitEventFragment(habitEventAdapter.getItem(position), selected_habit).show(getSupportFragmentManager(), "VIEW/EDIT_HabitEvent");
            }
        });
    }
}
