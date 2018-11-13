package com.akshatsharma.dailyexpenselogger;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.akshatsharma.dailyexpenselogger.database.AppDatabase;
import com.akshatsharma.dailyexpenselogger.database.Expense;
import com.akshatsharma.dailyexpenselogger.database.User;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements ExpenseAdapter.ItemClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    private TextView toolbarTitle, budgetDisplayTextView;
    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabAddIncome, fabAddExpense;
    private Context context;
    private LayoutInflater layoutInflater;
    private View view;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views, listeners, toolbar and the RecyclerView
        initViews();
        initListeners();
        setupToolbar();
        setupRecyclerView();
        // Initialize a database object
        database = AppDatabase.getInstance(getApplicationContext());
        checkForStartOfMonth();
        showRemainingBudget();




        // Set date on the calendar to the current date, and set its visibility to gone by default
        calendarView.setDate(System.currentTimeMillis(), false, true);
        calendarView.setVisibility(View.GONE);

        // The touch helper to the RecyclerView helps it recognize when the user swipes on it.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            // Called when the user swipes left or right on the ViewHolder
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                // Swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Expense> expenses = adapter.getExpenses();
                        int id = expenses.get(position).getExpenseId();
                        int amount = expenses.get(position).getAmount();
                        returnAmountToBudget(id, amount);
                        database.expenseDao().deleteExpense(expenses.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        // Get expenses for the current date by default
        retrieveExpenses(getCurrentDate());
    }

    // Method used to initialize all views in the layout
    private void initViews() {
        toolbar = findViewById(R.id.tbMainActivity);
        toolbarTitle = findViewById(R.id.toolbar_title);
        calendarView = findViewById(R.id.cv_calendar);
        recyclerView = findViewById(R.id.rv_expenses);
        floatingActionMenu = findViewById(R.id.fab_menu);
        fabAddExpense = findViewById(R.id.fab_menu_add_expense);
        fabAddIncome = findViewById(R.id.fab_menu_add_income);
        budgetDisplayTextView = findViewById(R.id.tv_display_budget);
    }

    // This method initializes all the relevant listeners
    private void initListeners() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // call a method, pass the values of year, month, day in it
                String date = convertToDateString(dayOfMonth, month, year);
                retrieveExpenses(date);
            }
        });

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddExpenseActivity
                Intent addExpenseIntent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(addExpenseIntent);
            }
        });

        fabAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddIncomeActivity
                Intent addIncomeIntent = new Intent(MainActivity.this, AddIncomeActivity.class);
                startActivity(addIncomeIntent);
            }
        });
    }

    // This method sets up the toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbarTitle.setText(R.string.toolbar_title);
    }

    // This method populates the remainingBudgetTextView with the user's remaining budget for the month
    private void showRemainingBudget() {
        LiveData<User> userData = database.userDao().loadUserData();
        userData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                int monthlyBudget = user.getMonthlyBudget();
                int totalExpenditureThisMonth = user.getTotalExpenditureThisMonth();
                int remainingBudget = monthlyBudget - totalExpenditureThisMonth;
                String budgetDisplay = getString(R.string.budget_display) + " ₹" + String.valueOf(remainingBudget);
                if(remainingBudget <= 0) {
                    budgetDisplayTextView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorExpense));
                    budgetDisplay += "! Money being deducted from savings!";
                } else {
                    budgetDisplayTextView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorBudgetDisplayBackground));
                }
                budgetDisplayTextView.setText(budgetDisplay);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_calendar:
                if (calendarView.getVisibility() == View.GONE) {
                    calendarView.setVisibility(View.VISIBLE);
                } else {
                    calendarView.setVisibility(View.GONE);
                }
                break;


            case R.id.edit_budget:
                // Create the AlertDialog which will allow the user to edit his monthly budget
                context = MainActivity.this;
                layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.edit_budget_dialog, null);
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(view);

                final TextView editBudgetTextView = view.findViewById(R.id.tv_dialog_title);
                final EditText editBudgetEditText = view.findViewById(R.id.et_edit_budget);
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update the monthly budget of the user
                                        int income = database.userDao().loadIncome();
                                        final int budget = database.userDao().loadBudget();
                                        int savings = database.userDao().loadSavings();
                                        int totalExpenditureThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                                        // The value of row id is always 1, since there is only one row in the table
                                        final User user = new User(1, income, budget, savings, totalExpenditureThisMonth);
                                        int newBudget = Integer.parseInt(editBudgetEditText.getText().toString());
                                        user.setSavings(income - newBudget);
                                        user.setMonthlyBudget(newBudget);
                                        database.userDao().updateUser(user);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                editBudgetTextView.setText(R.string.set_monthly_budget);
                int currentBudget = database.userDao().loadBudget();
                editBudgetEditText.setHint("Current Budget: ₹" + currentBudget);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.edit_income:
                // Create the AlertDialog which will allow the user to edit his monthly income
                context = MainActivity.this;
                layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.edit_budget_dialog, null);
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(view);

                final TextView editIncomeTextView = view.findViewById(R.id.tv_dialog_title);
                final EditText editIncomeEditText = view.findViewById(R.id.et_edit_budget);
                alertDialogBuilder.setCancelable(true)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update the monthly income of the user
                                        int income = database.userDao().loadIncome();
                                        final int budget = database.userDao().loadBudget();
                                        int savings = database.userDao().loadSavings();
                                        int totalExpenditureThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                                        final User user = new User(1, income, budget, savings, totalExpenditureThisMonth);
                                        int newIncome = Integer.parseInt(editIncomeEditText.getText().toString());
                                        user.setSavings(newIncome - budget);
                                        user.setMonthlyIncome(newIncome);
                                        database.userDao().updateUser(user);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                editIncomeTextView.setText(R.string.set_monthly_income);
                int currentIncome = database.userDao().loadIncome();
                editIncomeEditText.setHint("Current Income: ₹" + currentIncome);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.check_savings:
                context = MainActivity.this;
                layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.check_savings_dialog, null);
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(view);

                final TextView savingsTitleTextView = view.findViewById(R.id.tv_savings_dialog_title);
                final TextView savingsAmountTextView = view.findViewById(R.id.tv_savings_dialog_amt);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                savingsTitleTextView.setText(R.string.savings_title);
                int savings = database.userDao().loadSavings();
                String savingsString = "₹" + String.valueOf(savings);
                savingsAmountTextView.setText(savingsString);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.expenses_by_category:
                context = MainActivity.this;
                layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.show_category_expenses, null);
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(view);

                final TextView foodTitleTextView = view.findViewById(R.id.tv_food_expenses);
                final TextView foodAmountTextView = view.findViewById(R.id.tv_food_amount);
                final TextView travelTitleTextView = view.findViewById(R.id.tv_travel_expenses);
                final TextView travelAmountTextView = view.findViewById(R.id.tv_travel_amount);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                foodTitleTextView.setText("Food Expenses");
                travelTitleTextView.setText("Travel Expenses");
                int[] foodExpenses = database.expenseDao().getAmountByCategory("Food");
                int[] travelExpenses = database.expenseDao().getAmountByCategory("Travel");
                int totalFoodExpenses = 0;
                int totalTravelExpenses = 0;
                for (int foodExpense: foodExpenses) {
                    totalFoodExpenses += foodExpense;
                }
                for(int travelExpense: travelExpenses) {
                    totalTravelExpenses += travelExpense;
                }

                foodAmountTextView.setText(String.valueOf(totalFoodExpenses));
                travelAmountTextView.setText(String.valueOf(totalTravelExpenses));

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // This method sets up the RecyclerView
    private void setupRecyclerView() {
        // Set the layout for the RecyclerView to be a linear layout
        // which measures and positions items within a RecyclerView into a linear list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter, and attach it to the RecyclerView
        adapter = new ExpenseAdapter(this, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    private void retrieveExpenses(String date) {
        LiveData<List<Expense>> expenses = database.expenseDao().loadAllExpenses(date);
        expenses.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                adapter.setExpenses(expenses);
            }
        });
    }

    @Override
    public void onItemClickListener(final int itemId) {
        Expense expense = database.expenseDao().loadExpense(itemId);
        int amount = expense.getAmount();
        Log.d("amt", String.valueOf(amount));
        openActivityToEditValue(itemId, amount);
    }

    private String convertToDateString(int dayOfMonth, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        Date dt = calendar.getTime();
        String date = sdf.format(dt);
        return date;
    }


    private String getCurrentDate() {
        Date dt = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(dt);

        return date;
    }

    private void returnAmountToBudget(final int expenseId, final int amount) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int income = database.userDao().loadIncome();
                int budget = database.userDao().loadBudget();
                int savings = database.userDao().loadSavings();
                int expensesThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                User user = new User(1, income, budget, savings, expensesThisMonth);
                expensesThisMonth -= amount;
                user.setTotalExpenditureThisMonth(expensesThisMonth);
                database.userDao().updateUser(user);
            }
        });
    }

    private void openActivityToEditValue(int itemId, int amount) {
        if(amount > 0) {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            intent.putExtra(AddExpenseActivity.EXTRA_EXPENSE_ID, itemId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, AddIncomeActivity.class);
            intent.putExtra(AddIncomeActivity.EXTRA_EXPENSE_ID, itemId);
            startActivity(intent);
        }
    }

    private void checkForStartOfMonth() {
        final Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String dayOfMonth = sdf.format(date);
        Log.d("date",dayOfMonth);
        if(dayOfMonth.equals("01")) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    int budget = database.userDao().loadBudget();
                    int income = database.userDao().loadIncome();
                    int savings = database.userDao().loadSavings();
                    int expensesThisMonth = database.userDao().loadTotalExpenditureThisMonth();
                    int remainingBudget = budget - expensesThisMonth;
                    savings += remainingBudget;
                    User user = new User(income, budget, savings, expensesThisMonth);
                    user.setSavings(savings);
                    user.setTotalExpenditureThisMonth(0);
                    database.userDao().updateUser(user);
                }
            });
        }
    }
}
