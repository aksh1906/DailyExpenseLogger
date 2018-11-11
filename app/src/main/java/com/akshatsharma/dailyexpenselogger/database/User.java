package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "monthly_income")
    private int monthlyIncome = 0;
    @ColumnInfo(name = "monthly_budget")
    private int monthlyBudget = 0;
    private int savings = 0;

    @Ignore
    public User(int monthlyIncome, int monthlyBudget, int savings) {
        this.monthlyIncome = monthlyIncome;
        this.monthlyBudget = monthlyBudget;
        this.savings = savings;
    }


    public User(int rowId, int monthlyIncome, int monthlyBudget, int savings) {
        this.rowId = rowId;
        this.monthlyIncome = monthlyIncome;
        this.monthlyBudget = monthlyBudget;
        this.savings = savings;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(int monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public int getSavings() {
        return savings;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }

    public static User populateData() {
        return new User(0, 0, 0);
    }
}
