package com.akshatsharma.dailyexpenselogger.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface UserDao {

//    @Query("SELECT first_name, last_name FROM user WHERE user_name = :userName")
//    LiveData<User> getUserName(String userName);
}
