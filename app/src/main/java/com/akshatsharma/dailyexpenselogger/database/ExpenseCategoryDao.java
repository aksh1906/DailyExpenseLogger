package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ExpenseCategoryDao {

    @Insert
    void insertAll(ExpenseCategory... expenseCategories);

    @Query("SELECT category FROM expense_category WHERE item = :item")
    String loadCategoryByItemName(String item);

    @Insert
    void insertExpenseCategory(ExpenseCategory expenseCategory);

    @Query("SELECT item FROM expense_category")
    List<String> getAllItems();

    @Query("SELECT * FROM expense_category")
    List<ExpenseCategory> loadAllCategories();
}
