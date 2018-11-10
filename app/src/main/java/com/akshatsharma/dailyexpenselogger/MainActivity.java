package com.akshatsharma.dailyexpenselogger;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.akshatsharma.dailyexpenselogger.database.AppDatabase;
import com.akshatsharma.dailyexpenselogger.database.Expense;
import com.akshatsharma.dailyexpenselogger.database.ExpenseDao;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ExpenseAdapter.ItemClickListener {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;

    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initViews();


        // Set date on the calendar to the current date
        calendarView.setDate(System.currentTimeMillis(), false, true);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // call a method, pass the values of year, month, day in it
                String message = dayOfMonth + "/" + month + "/" + year;
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Set the layout for the RecyclerView to be a linear layout
        // which measures and positions items within a RecyclerView into a linear list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter, and attach it to the RecyclerView
        adapter = new ExpenseAdapter(this, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

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
                        database.expenseDao().deleteExpense(expenses.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        database = AppDatabase.getInstance(getApplicationContext());
        retrieveExpenses();
    }

    // Method used to initialize all views in the layout
    private void initViews() {
        calendarView = findViewById(R.id.cv_calendar);
        recyclerView = findViewById(R.id.rv_expenses);
    }

    private void retrieveExpenses() {
        LiveData<List<Expense>> expenses = database.expenseDao().loadAllExpenses(dateFromCalendarView());
        expenses.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                adapter.setExpenses(expenses);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {

        // Start AddExpenseActivity, while adding the ItemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
        intent.putExtra(AddExpenseActivity.EXTRA_EXPENSE_ID, itemId);
        startActivity(intent);
    }

    public void changeDate(int year, int month, int dayOfMonth) {
        // this method will change the displayed expenses based on the date that they were created on

    }
}
