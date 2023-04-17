package com.example.budgetup;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("DELETE FROM notifications WHERE userEmail LIKE :email")
    void deleteByEmail(String email);

    @Query("DELETE FROM notifications WHERE id LIKE :id")
    void deleteById(String id);

    @Query("SELECT * FROM notifications WHERE id LIKE :id")
    Notification getById(String id);

    @Query("SELECT * FROM notifications WHERE userEmail LIKE :email")
    List<Notification> getNotificationsByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(Notification notification);

    @Update
    void update(Notification notification);

}
