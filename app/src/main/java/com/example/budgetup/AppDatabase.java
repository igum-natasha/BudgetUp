package com.example.budgetup;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    entities = {User.class, Expense.class},
    version = 6,
    exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
  private static AppDatabase db;

  public abstract UserDao userDao();

  public abstract ExpenseDao expenseDao();

  public static AppDatabase build(Context context) {
    if (db == null) {
      db =
          Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
              .fallbackToDestructiveMigration()
              .allowMainThreadQueries()
              .build();
    }
    return db;
  }
}
