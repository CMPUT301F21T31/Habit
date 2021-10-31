package com.example.habit;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Month;


/**
 * US 01.01.01
 * US 01.02.01
 * US 01.06.01
 * Create the add habit interface
 */
public class addHabit extends AppCompatActivity {

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
    EditText startMonth;
    EditText startDay;
    EditText startYear;
    EditText endMonth;
    EditText endDay;
    EditText endYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit);

        final RelativeLayout relativeLayout;

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

        final boolean[] myButtonIsClicked = {false};

        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myButtonIsClicked[0]) {
                    Monday.setBackground(getDrawable(R.drawable.round_button));
                    myButtonIsClicked[0] = false;
                } else {
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


        // 2. Click the calendar sign to select the end date
    }
}
