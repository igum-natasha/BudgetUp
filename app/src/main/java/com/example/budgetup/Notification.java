package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "notifications",
    foreignKeys = {
      @ForeignKey(
          entity = User.class,
          parentColumns = "email",
          childColumns = "userEmail",
          onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index("userEmail")})
public class Notification {
  @NonNull
  @ColumnInfo(name = "userEmail")
  public String userEmail;

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public int id;

  @NonNull
  @ColumnInfo(name = "name")
  public String name;

  @NonNull
  @ColumnInfo(name = "message")
  public String message;

  @NonNull
  @ColumnInfo(name = "date")
  public long date;

  @NonNull
  @ColumnInfo(name = "status", defaultValue = "true")
  public boolean status;

  public void setDate(@NonNull long date) {
    this.date = date;
  }

  public void setUserEmail(@NonNull String userEmail) {
    this.userEmail = userEmail;
  }

  public void setStatus(@NonNull boolean status) {
    this.status = status;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public void setId(@NonNull int id) {
    this.id = id;
  }

  public void setMessage(@NonNull String message) {
    this.message = message;
  }

  @NonNull
  public String getUserEmail() {
    return userEmail;
  }

  @NonNull
  public int getId() {
    return id;
  }

  @NonNull
  public String getName() {
    return name;
  }

  @NonNull
  public long getDate() {
    return date;
  }

  @NonNull
  public String getMessage() {
    return message;
  }

  @NonNull
  public boolean getStatus() {
    return status;
  }
}
