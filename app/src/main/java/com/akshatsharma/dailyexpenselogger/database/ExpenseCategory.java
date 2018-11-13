package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "expense_category")
public class ExpenseCategory {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String item;
    private String category;

    public ExpenseCategory(String item, String category) {
        this.item = item;
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static ExpenseCategory[] populateData() {
        return new ExpenseCategory[] {
                new ExpenseCategory("Food", "Food"),
                new ExpenseCategory("Chips", "Food"),
                new ExpenseCategory("Pepsi", "Food"),
                new ExpenseCategory("Coke", "Food"),
                new ExpenseCategory("Vada Pav", "Food"),
                new ExpenseCategory("Eating Out", "Food"),
                new ExpenseCategory("Frankie", "Food"),
                new ExpenseCategory("Burger", "Food"),
                new ExpenseCategory("McDonalds", "Food"),
                new ExpenseCategory("Lays", "Food"),
                new ExpenseCategory("Burger King", "Food"),
                new ExpenseCategory("Pani Puri", "Food"),
                new ExpenseCategory("Chicken", "Food"),
                new ExpenseCategory("Samosa", "Food"),
                new ExpenseCategory("Idli", "Food"),
                new ExpenseCategory("Coffee", "Food"),
                new ExpenseCategory("Tea", "Food"),
                new ExpenseCategory("Milk", "Food"),
                new ExpenseCategory("Donut", "Food"),
                new ExpenseCategory("Kachori", "Food"),
                new ExpenseCategory("Chaat", "Food"),
                new ExpenseCategory("Food Court", "Food"),
                new ExpenseCategory("KFC", "Food"),
                new ExpenseCategory("Subway", "Food"),
                new ExpenseCategory("Popcorn", "Food"),
                new ExpenseCategory("Cold Coffee", "Food"),
                new ExpenseCategory("Local", "Travel"),
                new ExpenseCategory("Local Train", "Travel"),
                new ExpenseCategory("Metro", "Travel"),
                new ExpenseCategory("Auto", "Travel"),
                new ExpenseCategory("Rickshaw", "Travel"),
                new ExpenseCategory("Bus", "Travel"),
                new ExpenseCategory("Taxi", "Travel"),
                new ExpenseCategory("Ola", "Travel"),
                new ExpenseCategory("Uber", "Travel"),
                new ExpenseCategory("Cab", "Travel"),
                new ExpenseCategory("Train", "Travel")
        };
    }
}

