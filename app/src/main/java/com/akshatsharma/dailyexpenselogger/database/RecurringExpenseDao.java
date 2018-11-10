package com.akshatsharma.dailyexpenselogger.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface RecurringExpenseDao {

    @Insert
    void insertRecurringExpense(RecurringExpense recurringExpense);

    @Update
    void updateRecurringExpense(RecurringExpense recurringExpense);

//    @Query("SELECT frequency FROM recurring_expense WHERE recurring_id = :recurringId")
//    LiveData<RecurringExpense> getFrequencyOfRecurringExpense(int recurringId);

}
