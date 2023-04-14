package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "email")
  public String email;

  @NonNull
  @ColumnInfo(name = "name")
  public String name;

  @NonNull
  @ColumnInfo(name = "password")
  public String password;

  @NonNull
  @ColumnInfo(name = "language", defaultValue = "en")
  public String language;

  @NonNull
  @ColumnInfo(name = "status", defaultValue = "offline")
  public String status;

  @NonNull
  public String getName() {
    return name;
  }

  @NonNull
  public String getEmail() {
    return email;
  }

  @NonNull
  public String getPassword() {
    return password;
  }

  @NonNull
  public String getLanguage() {
    return language;
  }

  @NonNull
  public String getStatus() {
    return status;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public void setEmail(@NonNull String email) {
    this.email = email;
  }

  public void setPassword(@NonNull String password) {
    this.password = password;
  }

  public void setLanguage(@NonNull String language) {
    this.language = language;
  }

  public void setStatus(@NonNull String status) {
    this.status = status;
  }
}
