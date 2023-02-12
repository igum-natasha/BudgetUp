package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "expenses",
    foreignKeys = {
      @ForeignKey(
          entity = User.class,
          parentColumns = "email",
          childColumns = "userEmail",
          onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index("userEmail")})
public class Expense {
  @NonNull
  @ColumnInfo(name = "userEmail")
  public String userEmail;

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  public String id;

  @NonNull
  @ColumnInfo(name = "date")
  public long date;

  @NonNull
  @ColumnInfo(name = "value")
  public String value;

  @NonNull
  @ColumnInfo(name = "category")
  public String category;

  @NonNull
  @ColumnInfo(name = "payment")
  public String payment;

  @NonNull
  @ColumnInfo(name = "note")
  public String note;

  @NonNull
  @ColumnInfo(name = "currency", defaultValue = "RUB")
  public String currency;

  @NonNull
  @ColumnInfo(name = "Image")
  public int image;

  public void setUserEmail(@NonNull String userEmail) {
    this.userEmail = userEmail;
  }

  public void setImage(int image) {
    this.image = image;
  }

  public void setDate(@NonNull long date) {
    this.date = date;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public void setCategory(@NonNull String category) {
    this.category = category;
  }

  public void setCurrency(@NonNull String currency) {
    this.currency = currency;
  }

  public void setValue(@NonNull String value) {
    this.value = value;
  }

  public void setNote(@NonNull String note) {
    this.note = note;
  }

  public void setPayment(@NonNull String payment) {
    this.payment = payment;
  }

  @NonNull
  public long getDate() {
    return date;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public String getUserEmail() {
    return userEmail;
  }

  @NonNull
  public String getCategory() {
    return category;
  }

  @NonNull
  public String getValue() {
    return value;
  }

  @NonNull
  public String getPayment() {
    return payment;
  }

  @NonNull
  public String getCurrency() {
    return currency;
  }

  @NonNull
  public String getNote() {
    return note;
  }

  @NonNull
  public int getImage() {
    return image;
  }
}
