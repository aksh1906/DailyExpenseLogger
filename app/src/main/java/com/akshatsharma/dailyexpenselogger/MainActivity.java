package com.akshatsharma.dailyexpenselogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.cv_calendar);
        calendarView.setDate(System.currentTimeMillis(), false, true);
    }
}
