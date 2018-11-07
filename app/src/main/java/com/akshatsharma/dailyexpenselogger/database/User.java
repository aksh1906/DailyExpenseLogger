package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "monthly_income")
    private int monthlyIncome;
    @ColumnInfo(name = "monthly_budget")
    private int monthlyBudget;
    private int savings;

    @Ignore
    public User(String firstName, String lastName, int monthlyIncome, int monthlyBudget, int savings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.monthlyIncome = monthlyIncome;
        this.monthlyBudget = monthlyBudget;
        this.savings = savings;
    }

    public User(String userName, String firstName, String lastName, int monthlyIncome, int monthlyBudget, int savings) {
        this.userName =  userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.monthlyIncome = monthlyIncome;
        this.monthlyBudget = monthlyBudget;
        this.savings = savings;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
