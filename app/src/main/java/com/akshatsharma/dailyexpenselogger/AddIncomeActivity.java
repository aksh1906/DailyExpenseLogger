package com.akshatsharma.dailyexpenselogger;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddIncomeActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private android.support.v7.widget.Toolbar toolbar;
    private EditText datePicker, timePicker;
    private Spinner frequencySpinner;
    private Switch recurringSwitch;
    private TextView dateLabel, timeLabel, toolbarTitle;
    private View convenienceView;
    private LinearLayout frequencyLL;
    private String[] frequencies = {"Daily", "Weekdays", "Weekends", "Weekly", "Bi-Weekly", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        recurringSwitch = findViewById(R.id.switch_recurring_income);
        datePicker = findViewById(R.id.et_income_date);
        timePicker = findViewById(R.id.et_income_time);
        frequencySpinner = findViewById(R.id.spinner_income_frequency);
        convenienceView = findViewById(R.id.view_layout_convenience);
        frequencyLL = findViewById(R.id.ll_frequency);
        dateLabel = findViewById(R.id.tv_date_label);
        timeLabel = findViewById(R.id.tv_time_label);

        createToolbar();

        recurringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIncomeMode();
            }
        });
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        frequencySpinner.setOnItemSelectedListener(this);

        // Set the date of the income to the current date by default
        datePicker.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
        // Set the time of the income to the current time by default
        timePicker.setText(new SimpleDateFormat("hh:mm ", Locale.getDefault()).format(new Date()));

        // Populating the frequencySpinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, frequencies);
        frequencySpinner.setAdapter(adapter);
    }

    // This creates the title and adds the toolbar
    private void createToolbar() {
        toolbar = findViewById(R.id.tbCreateIncome);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText(R.string.income_toolbar_title);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == datePicker) {
            int year, month, day;
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
            int hour, minute;
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timePicker.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
                }
            }, hour, minute, true);
            timePickerDialog.show();
        }
    }

    private void changeIncomeMode() {
        if(recurringSwitch.isChecked()) {
            convenienceView.setVisibility(View.GONE);
            frequencyLL.setVisibility(View.VISIBLE);
            frequencySpinner.setVisibility(View.VISIBLE);
            dateLabel.setText(R.string.recurring_start_date_label);
            timeLabel.setText(R.string.recurring_start_time_label);
        } else {
            convenienceView.setVisibility(View.VISIBLE);
            frequencyLL.setVisibility(View.GONE);
            frequencySpinner.setVisibility(View.GONE);
            dateLabel.setText(R.string.date_label);
            timeLabel.setText(R.string.time_label);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    public void test() {

    }
}
