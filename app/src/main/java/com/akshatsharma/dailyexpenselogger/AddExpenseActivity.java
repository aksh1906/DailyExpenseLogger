package com.akshatsharma.dailyexpenselogger;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddExpenseActivity extends AppCompatActivity
        implements View.OnClickListener {

    private EditText datePicker, timePicker;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        datePicker = findViewById(R.id.et_expense_date);
        timePicker = findViewById(R.id.et_expense_time);

        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);

        // Set the date of the expense to the current date by default
        datePicker.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        timePicker.setText(new SimpleDateFormat("hh:mm ", Locale.getDefault()).format(new Date()));
    }

    @Override
    public void onClick(View v) {
        if(v == datePicker) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    datePicker.setText(new StringBuilder().append(dayOfMonth).append("/").append(month+1).append("/").append(year));
                }
            }, year, month, day);
            datePickerDialog.show();
        }

        if(v == timePicker) {
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timePicker.setText(hourOfDay + ":" + minute);
                }
            }, hour, minute, false);
            timePickerDialog.show();
        }
    }
}
