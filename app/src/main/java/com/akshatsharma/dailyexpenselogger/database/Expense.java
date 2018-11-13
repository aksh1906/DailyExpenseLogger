package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Date;

@Entity(tableName = "expense")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    private int expenseId;
    private String description;
    private int amount;
    private String date;
    private String category;
//    @ColumnInfo(name = "recurring_id")
//    private int recurringId;

    @Ignore
    public Expense(String description, int amount, String date, String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
//        this.recurringId = recurringId;
    }

//    @Ignore
    public Expense(int expenseId, String description, int amount, String date, String category/*, int recurringId*/) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //    public int getRecurringId() {
//        return recurringId;
//    }
//
//    public void setRecurringId(int recurringId) {
//        this.recurringId = recurringId;
//    }

    public static Expense populateData() {
        return new Expense("", 0, "", null);
    }
}
