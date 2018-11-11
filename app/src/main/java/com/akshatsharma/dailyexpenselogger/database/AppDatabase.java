package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.akshatsharma.dailyexpenselogger.AppExecutors;

import java.util.concurrent.Executors;

@Database(entities = {Expense.class, RecurringExpense.class, User.class}, version = 1, exportSchema = false)
//@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "daily_expense_logger.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(final Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        getInstance(context).userDao().insert(User.populateData());
                                    }
                                });
                            }
                        })
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract ExpenseDao expenseDao();
    public abstract RecurringExpenseDao recurringExpenseDao();
    public abstract UserDao userDao();
}
