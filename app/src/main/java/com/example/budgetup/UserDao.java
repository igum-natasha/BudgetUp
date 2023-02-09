package com.example.budgetup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

  @Query("DELETE FROM users WHERE email LIKE :email")
  void deleteByEmail(String email);

  @Query("SELECT * FROM users")
  List<User> getAll();

  @Query("SELECT * FROM users WHERE email LIKE :email")
  User getByEmail(String email);

  @Query("SELECT * FROM users WHERE status LIKE :status")
  User getByStatus(String status);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertUser(User user);
}
