package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "expense")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    private int expenseId;
    private String description;
    private int amount;
    private String date;
//    @ColumnInfo(name = "recurring_id")
//    private int recurringId;

    @Ignore
    public Expense(String description, int amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
//        this.recurringId = recurringId;
    }

//    @Ignore
    public Expense(int expenseId, String description, int amount, String date/*, int recurringId*/) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.date = date;
//        this.recurringId = recurringId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public int getRecurringId() {
//        return recurringId;
//    }
//
//    public void setRecurringId(int recurringId) {
//        this.recurringId = recurringId;
//    }
}
