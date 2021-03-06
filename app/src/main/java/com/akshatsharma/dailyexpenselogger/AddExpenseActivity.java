package com.akshatsharma.dailyexpenselogger;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import com.akshatsharma.dailyexpenselogger.database.AppDatabase;
import com.akshatsharma.dailyexpenselogger.database.Expense;
import com.akshatsharma.dailyexpenselogger.database.ExpenseCategory;
import com.akshatsharma.dailyexpenselogger.database.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddExpenseActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Extra for the expense ID to be received in the intent
    public static final String EXTRA_EXPENSE_ID = "extraExpenseId";
    // Extra for the expense ID to be received after rotation
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
    private FloatingActionButton saveButton;

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
//            saveButton.setText(R.string.update_button);
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
        saveButton = findViewById(R.id.fab_save_expense);
    }

    // This creates the toolbar and adds the title
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbarTitle.setText(R.string.expense_toolbar_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                        String dayString = "0";
                        if(dayOfMonth < 10) {
                            dayString = "0" + String.valueOf(dayOfMonth);
                        } else {
                            dayString = String.valueOf(dayOfMonth);
                        }
                        datePicker.setText(new StringBuilder().append(dayString).append("/").append(month + 1).append("/").append(year));
                    }
                }, year, month, day);
                datePickerDialog.show();
                break;
            }

            case R.id.fab_save_expense: {
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

        final Expense expense = new Expense(description, amount, date, null);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<String> categoryList = database.expenseCategoryDao().getAllItems();
                for (String category: categoryList) {
                    if(category.equals(description)) {
                        String expenseCategory = database.expenseCategoryDao().loadCategoryByItemName(category);
                        expense.setCategory(expenseCategory);
                    }
                }
                int income = database.userDao().loadIncome();
                int budget = database.userDao().loadBudget();
                int savings = database.userDao().loadSavings();
                int expensesThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                int remainingBudget = budget - expensesThisMonth;
                User user = new User(1, income, budget, savings, expensesThisMonth);
                if(expenseId == DEFAULT_EXPENSE_ID) {
                    // insert new expense
                    if(amount > remainingBudget) {
                        database.expenseDao().insertExpense(expense);
                        expensesThisMonth += remainingBudget;
                        savings -= (amount - remainingBudget);
                        user.setTotalExpenditureThisMonth(expensesThisMonth);
                        user.setSavings(savings);
                        database.userDao().updateUser(user);
                    } else {
                        database.expenseDao().insertExpense(expense);
                        expensesThisMonth += amount;
                        user.setTotalExpenditureThisMonth(expensesThisMonth);
                        database.userDao().updateUser(user);
                    }
                } else {
                    // update expense
                    int originalExpense = database.expenseDao().loadExpense(expenseId).getAmount();
                    int difference = amount - originalExpense;
                    if(difference > remainingBudget) {
                        expense.setExpenseId(expenseId);
                        expense.setAmount(amount);
                        database.expenseDao().updateExpense(expense);
                        expensesThisMonth += remainingBudget;
                        savings -= (difference - remainingBudget);
                        user.setTotalExpenditureThisMonth(expensesThisMonth);
                        user.setSavings(savings);
                        database.userDao().updateUser(user);
                    } else {
                        expensesThisMonth += difference;
                        user.setTotalExpenditureThisMonth(expensesThisMonth);
                        database.userDao().updateUser(user);
                        expense.setExpenseId(expenseId);
                        database.expenseDao().updateExpense(expense);
                    }
                }
                finish();
            }
        });
    }

}
