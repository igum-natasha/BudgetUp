package com.example.budgetup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {
  @Query("DELETE FROM expenses WHERE userEmail LIKE :email")
  void deleteByEmail(String email);

  @Query("DELETE FROM expenses WHERE id LIKE :id")
  void deleteById(String id);

  @Query("SELECT * FROM expenses")
  List<Expense> getAll();

  @Query("SELECT * FROM expenses WHERE userEmail LIKE :email")
  Expense getByEmail(String email);

  @Query("SELECT * FROM expenses WHERE date BETWEEN :dayStart AND :dayEnd")
  List<Expense> getByDate(long dayStart, long dayEnd);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertExpense(Expense expense);
}
