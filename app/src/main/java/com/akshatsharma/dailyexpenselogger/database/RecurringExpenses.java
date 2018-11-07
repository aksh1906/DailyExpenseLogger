package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "recurring_expense"/*, foreignKeys = @ForeignKey(entity = Expenses.class, parentColumns = "expenseId", childColumns = "expenseId")*/)
public class RecurringExpenses {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recurring_id")
    private int recurringId;
    private String frequency;
    @ColumnInfo(name = "auto_expense_date")
    private Date autoExpenseDate;
//    @ColumnInfo(name = "expense_id")
//    private int expenseId;

    @Ignore
    public RecurringExpenses(String frequency, Date autoExpenseDate) {
        this.frequency = frequency;
        this.autoExpenseDate = autoExpenseDate;
    }

    public RecurringExpenses(int recurringId, String frequency, Date autoExpenseDate) {
        this.recurringId = recurringId;
        this.frequency = frequency;
        this.autoExpenseDate = autoExpenseDate;
    }

    public int getRecurringId() {
        return recurringId;
    }

    public void setRecurringId(int recurringId) {
        this.recurringId = recurringId;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Date getAutoExpenseDate() {
        return autoExpenseDate;
    }

    public void setAutoExpenseDate(Date autoExpenseDate) {
        this.autoExpenseDate = autoExpenseDate;
    }

//    public int getExpenseId() {
//        return expenseId;
//    }
//
//    public void setExpenseId(int expenseId) {
//        this.expenseId = expenseId;
//    }
}
