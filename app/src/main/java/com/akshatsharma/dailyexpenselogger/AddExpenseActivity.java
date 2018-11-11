package com.akshatsharma.dailyexpenselogger;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.akshatsharma.dailyexpenselogger.database.AppDatabase;
import com.akshatsharma.dailyexpenselogger.database.Expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddExpenseActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Extra for the expense ID to be received in the intent
    public static final String EXTRA_EXPENSE_ID = "extraExpenseId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_EXPENSE_ID = "instanceExpenseId";
    // Constant for default expense id to be used when not in update mode
    private static final int DEFAULT_EXPENSE_ID = -1;

    private Toolbar toolbar;
    private EditText datePicker, timePicker, descriptionEditText, amountEditText;
    private Spinner frequencySpinner;
    private Switch recurringSwitch;
    private TextView dateLabel, timeLabel, toolbarTitle;
    private View convenienceView;
    private LinearLayout frequencyLL;
    private Button saveButton;

    private int expenseId = DEFAULT_EXPENSE_ID;

    private AppDatabase database;

    private String[] frequencies = {"Daily","Weekdays", "Weekends", "Weekly", "Bi-Weekly", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        initViews();
        setupToolbar();
        initListeners();

        // Initialize a database object
        database = AppDatabase.getInstance(getApplicationContext());

        // Set the date of the expense to the current date by default
        datePicker.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

        // Populating the frequencySpinner
        populateSpinner();

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_EXPENSE_ID)) {
            expenseId = savedInstanceState.getInt(INSTANCE_EXPENSE_ID, DEFAULT_EXPENSE_ID);
        }

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_EXPENSE_ID)) {
            saveButton.setText(R.string.update_button);
            toolbarTitle.setText(R.string.edit_expense_toolbar_title);
            if(expenseId == DEFAULT_EXPENSE_ID) {
                expenseId = intent.getIntExtra(EXTRA_EXPENSE_ID, DEFAULT_EXPENSE_ID);

                final LiveData<Expense> expense = database.expenseDao().loadExpenseById(expenseId);
                expense.observe(this, new Observer<Expense>() {
                    @Override
                    public void onChanged(@Nullable Expense exp) {
                        expense.removeObserver(this);
                        populateUI(exp);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_EXPENSE_ID, expenseId);
        super.onSaveInstanceState(outState);
    }

    // Initialize all views in the layout
    private void initViews() {
        toolbar = findViewById(R.id.tbCreateExpense);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recurringSwitch = findViewById(R.id.switch_recurring_expense);
        descriptionEditText = findViewById(R.id.et_expense_description);
        amountEditText = findViewById(R.id.et_expense_amount);
        datePicker = findViewById(R.id.et_expense_date);
        timePicker = findViewById(R.id.et_expense_time);
        frequencySpinner = findViewById(R.id.spinner_expense_frequency);
        convenienceView = findViewById(R.id.view_layout_convenience);
        frequencyLL = findViewById(R.id.ll_frequency);
        dateLabel = findViewById(R.id.tv_date_label);
        timeLabel = findViewById(R.id.tv_time_label);
        saveButton = findViewById(R.id.btn_save_expense);
    }

    // This creates the toolbar and adds the title
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbarTitle.setText(R.string.expense_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    // This adds listeners to all relevant views
    private void initListeners() {
        recurringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeExpenseMode();
            }
        });
        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        frequencySpinner.setOnItemSelectedListener(this);
        saveButton.setOnClickListener(this);
    }

    // This populates the spinner
    private void populateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, frequencies);
        frequencySpinner.setAdapter(adapter);
    }

    // This populates the UI when in update mode
    private void populateUI(Expense expense) {
        if(expense == null) {
            return;
        }

        descriptionEditText.setText(expense.getDescription());
        amountEditText.setText(String.valueOf(expense.getAmount()));
        datePicker.setText(expense.getDate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_expense_date: {
                int year, month, day;
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.setText(new StringBuilder().append(dayOfMonth).append("/").append(month + 1).append("/").append(year));
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;
            }

//            case R.id.et_expense_time:
//                int hour, minute;
//                Calendar c = Calendar.getInstance();
//                hour = c.get(Calendar.HOUR_OF_DAY);
//                minute = c.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        timePicker.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
//                    }
//                }, hour, minute, true);
//                timePickerDialog.show();

            case R.id.btn_save_expense: {
                saveExpense();
                break;
            }
        }

    }

    // Used to change back and forth between recurring and non-recurring expense modes
    private void changeExpenseMode() {
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

    // This method saves the expense
    private void saveExpense() {
        final String description = descriptionEditText.getText().toString();
        final int amount = Integer.parseInt(amountEditText.getText().toString());
        final String date = datePicker.getText().toString();

        final Expense expense = new Expense(description, amount, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(expenseId == DEFAULT_EXPENSE_ID) {
                    // insert new expense
                    database.expenseDao().insertExpense(expense);
                } else {
                    // update expense
                    expense.setExpenseId(expenseId);
                    database.expenseDao().updateExpense(expense);
                }
                finish();
            }
        });
    }

}
