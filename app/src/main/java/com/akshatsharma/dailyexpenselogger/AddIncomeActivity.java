package com.akshatsharma.dailyexpenselogger;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

import com.akshatsharma.dailyexpenselogger.database.AppDatabase;
import com.akshatsharma.dailyexpenselogger.database.Expense;
import com.akshatsharma.dailyexpenselogger.database.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddIncomeActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Extra for the expense ID to be received in the intent
    public static final String EXTRA_EXPENSE_ID = "extraExpenseId";
    // Extra for the expense ID to be received after rotation
    public static final String INSTANCE_EXPENSE_ID = "instanceExpenseId";
    // Constant for default expense id to be used when not in update mode
    private static final int DEFAULT_EXPENSE_ID = -1;

    private android.support.v7.widget.Toolbar toolbar;
    private EditText datePicker, descriptionEditText, amountEditText;
    private Spinner frequencySpinner;
    private Switch recurringSwitch;
    private TextView dateLabel, timeLabel, toolbarTitle;
    private View convenienceView;
    private LinearLayout frequencyLL;
    private Button saveButton;

    private int expenseId = DEFAULT_EXPENSE_ID;

    private AppDatabase database;

    private String[] frequencies = {"Daily", "Weekdays", "Weekends", "Weekly", "Bi-Weekly", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        initViews();
        setupToolbar();
        initListeners();

        database = AppDatabase.getInstance(getApplicationContext());

        // Set the date of the income to the current date by default
        datePicker.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

        populateSpinner();

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_EXPENSE_ID)) {
            expenseId = savedInstanceState.getInt(INSTANCE_EXPENSE_ID, DEFAULT_EXPENSE_ID);
        }

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_EXPENSE_ID)) {
            saveButton.setText(R.string.update_button);
            toolbarTitle.setText(R.string.edit_income_toolbar_title);
            if(expenseId == DEFAULT_EXPENSE_ID) {
                expenseId = intent.getIntExtra(EXTRA_EXPENSE_ID, DEFAULT_EXPENSE_ID);

                final LiveData<Expense> income = database.expenseDao().loadExpenseById(expenseId);
                income.observe(this, new Observer<Expense>() {
                    @Override
                    public void onChanged(@Nullable Expense expense) {
                        income.removeObserver(this);
                        populateUi(expense);
                    }
                });
            }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.tbCreateIncome);
        descriptionEditText = findViewById(R.id.et_income_description);
        amountEditText = findViewById(R.id.et_income_amount);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recurringSwitch = findViewById(R.id.switch_recurring_income);
        datePicker = findViewById(R.id.et_income_date);
        frequencySpinner = findViewById(R.id.spinner_income_frequency);
        convenienceView = findViewById(R.id.view_layout_convenience);
        frequencyLL = findViewById(R.id.ll_frequency);
        dateLabel = findViewById(R.id.tv_date_label);
        timeLabel = findViewById(R.id.tv_time_label);
        saveButton = findViewById(R.id.btn_save_income);
    }

    // This creates the title and adds the toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbarTitle.setText(R.string.income_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListeners() {
        recurringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeIncomeMode();
            }
        });
        datePicker.setOnClickListener(this);
        frequencySpinner.setOnItemSelectedListener(this);
        saveButton.setOnClickListener(this);
    }

    private void populateSpinner() {
        // Populating the frequencySpinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, frequencies);
        frequencySpinner.setAdapter(adapter);
    }

    private void populateUi(Expense expense) {
        if(expense == null) {
            return;
        }

        descriptionEditText.setText(expense.getDescription());
        amountEditText.setText(String.valueOf(expense.getAmount() * -1));
        datePicker.setText(expense.getDate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_income_date:
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
                break;

            case R.id.btn_save_income:
                saveIncome();
                break;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    private void saveIncome() {
        final String description = descriptionEditText.getText().toString();
        final int amount = (Integer.parseInt(amountEditText.getText().toString())) * -1;
        final String date = datePicker.getText().toString();

        final Expense income = new Expense(description, amount, date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int incomeMonth = database.userDao().loadIncome();
                int budget = database.userDao().loadBudget();
                int savings = database.userDao().loadSavings();
                int expensesThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                User user = new User(1, incomeMonth, budget, savings, expensesThisMonth);
                if(expenseId == DEFAULT_EXPENSE_ID) {
                    database.expenseDao().insertExpense(income);
                    expensesThisMonth += amount;
                    user.setTotalExpenditureThisMonth(expensesThisMonth);
                    database.userDao().updateUser(user);
                } else {
                    int originalIncome = database.expenseDao().loadExpense(expenseId).getAmount();
                    int difference = originalIncome - amount;
                    expensesThisMonth -= difference;
                    user.setTotalExpenditureThisMonth(expensesThisMonth);
                    database.userDao().updateUser(user);
                    income.setExpenseId(expenseId);
                    database.expenseDao().updateExpense(income);
                }
                finish();
            }
        });
    }
}
