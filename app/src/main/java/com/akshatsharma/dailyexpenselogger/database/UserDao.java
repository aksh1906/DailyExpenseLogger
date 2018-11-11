package com.akshatsharma.dailyexpenselogger.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("SELECT monthly_income FROM user")
    int loadIncome();

    @Query("SELECT monthly_budget FROM user")
    int loadBudget();

    @Query("SELECT savings FROM user")
    int loadSavings();

}
