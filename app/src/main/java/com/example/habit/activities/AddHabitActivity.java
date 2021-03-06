package com.example.habit.activities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habit.entities.Habit;
import com.example.habit.R;
import com.example.habit.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * US 01.01.01
 * US 01.02.01
 * US 01.06.01
 * Add habit interface, containing text, calendar, switch, and button inputs allowing the user
 * to fill out all properties of a new Habit.
 * @author lewisning
 * @see android.app.Activity
 * @see android.content.Context
 */
public class AddHabitActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;

    ImageButton back;
    ImageButton confirm;
    ImageButton startDate;
    ImageButton endDate;
    Button Monday;
    Button Tuesday;
    Button Wednesday;
    Button Thursday;
    Button Friday;
    Button Saturday;
    Button Sunday;
    Switch addPublic;
    EditText title;
    EditText reason;
    TextView startMonth;
    TextView startDay;
    TextView startYear;
    TextView endMonth;
    TextView endDay;
    TextView endYear;

    int Syear;
    int Smonth;
    int Sday;
    int Eyear;
    int Emonth;
    int Eday;
    int nextPosition;

    Date startTime;
    Date endTime;
    HashMap<String, Boolean> selected_date = null;
    ArrayList<Boolean> recurrence = new ArrayList<Boolean>(
            Arrays.asList(false, false, false, false, false, false, false));

    /**
     * This method is implementing all functionalities of items on the interface which is adding
     * details of each habit
     * Working with add_habit.xml layout file
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        Intent intent = getIntent();
        nextPosition = intent.getIntExtra("nextPosition", 0);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        title = findViewById(R.id.habit_title);
        reason = findViewById(R.id.habit_reason);
        back = findViewById(R.id.back);
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
        addPublic = findViewById(R.id.add_public);

        final boolean[] myButtonIsClicked = {false};
        //boolean ifPublic = false;

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

        // 1. Click the calendar sign to select the start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(AddHabitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startYear.setText("" + year);
                                startMonth.setText("" + (month + 1));
                                startDay.setText("" + dayOfMonth);
                                calendar.set(year, month, dayOfMonth);
                                startTime = calendar.getTime();
                                Sday = dayOfMonth;
                                Smonth = month;
                                Syear = year;
                            }
                        }, year, month, dayOfMonth);
                calendar.set(Syear, Smonth, Sday);
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

                DatePickerDialog endDateDialog = new DatePickerDialog(AddHabitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endYear.setText("" + year);
                                endMonth.setText("" + (month + 1));
                                endDay.setText("" + dayOfMonth);
                                calendar.set(year, month, dayOfMonth);
                                endTime = calendar.getTime();
                                Eyear = year;
                                Emonth = month;
                                Eday = dayOfMonth;
                            }
                        }, year, month, dayOfMonth);
                calendar.set(Syear, Smonth, Sday);
                endDateDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                endDateDialog.show();
            }
        });


        // Passing the recurrence to the habit after clicked the confirm button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Let user enter the title, start and end date
                if (title.getText().toString().equals("")) {
                    Toast.makeText(AddHabitActivity.this,"Please enter the title", Toast.LENGTH_LONG).show();
                }
                else if (reason.getText().toString().equals("")) {
                    Toast.makeText(AddHabitActivity.this,"Please enter the reason", Toast.LENGTH_LONG).show();
                }
                else if (startYear.getText().toString().equals("")) {
                    Toast.makeText(AddHabitActivity.this,"Please select the start date", Toast.LENGTH_LONG).show();
                }
                else if (endYear.getText().toString().equals("")) {
                    Toast.makeText(AddHabitActivity.this,"Please select the end date", Toast.LENGTH_LONG).show();
                }
                else {
                    // 1. Get and set the habit title
                    String habit_title = title.getText().toString();

                    // 2. Get and set the habit reasons
                    String habit_reason = reason.getText().toString();

                    // 3. Set the recurrence
                    try {
                        selected_date = Habit.generateDaysDict(recurrence);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Habit curr_habit = new Habit(habit_title, habit_reason, startTime, endTime, selected_date, addPublic.isChecked(), nextPosition);
                    User.addHabit(user.getUid(), curr_habit);
                    finish();
                }
            }
        });
    }
}
