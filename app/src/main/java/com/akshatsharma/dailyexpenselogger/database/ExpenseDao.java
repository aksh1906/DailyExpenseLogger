package com.akshatsharma.dailyexpenselogger.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense WHERE date = :date ORDER BY date")
    LiveData<List<Expense>> loadAllExpenses(Date date);

    @Insert
    void insertExpense(Expense expense);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    @Query("SELECT * FROM expense WHERE expense_id = :id")
    LiveData<Expense> loadExpenseById(int id);
}
