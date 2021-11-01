package com.example.habit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class calendar extends AppCompatActivity {
Button calb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initOverall();
        calb=findViewById(R.id.calb);
        calb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goback();
            }
        });
    }
    public void goback(){ Intent intent=new Intent(this,tutorial_6time.class);
        startActivity(intent);

    }


    public void initOverall(){
        final TextView dateTest = (TextView)findViewById(R.id.convert);
        final CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String selectedDates = sdf.format(new Date(year-1900,month,dayOfMonth));
                dateTest.setText(selectedDates);
                SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("firstdate",selectedDates);
                editor.apply();
            }
        });

    }

}

