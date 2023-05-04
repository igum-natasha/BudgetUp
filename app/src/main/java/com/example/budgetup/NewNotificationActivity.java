package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class NewNotificationActivity extends AppCompatActivity {

  private Button btnSubmit;
  private ImageButton btnOk, btnOkMessage, btnBack;
  private EditText entName, entMessage;
  private TimePicker timePicker;
  private DatePicker datePicker;
  private TextView toolbarName;
  int REQUEST_CODE = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    initViews();
    btnOk.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (view != null) {
              InputMethodManager imm =
                  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
          }
        });

    btnOkMessage.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (view != null) {
              InputMethodManager imm =
                  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
          }
        });
    btnBack.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
          }
        });

    btnSubmit.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String name = entName.getText().toString();
            String message = entMessage.getText().toString();
            if (!name.equals("") && !message.equals("")) {
              long time = getTime();
              AppDatabase db = AppDatabase.build(getApplicationContext());
              String email = db.userDao().getByStatus("online").getEmail();
              List<Notification> notificationList =
                  db.notificationDao().getNotificationsByEmail(email);
              int id = 0;
              if (!notificationList.isEmpty()) {
                id = notificationList.get(notificationList.size() - 1).getId() + 1;
              }
              com.example.budgetup.Notification newNotification =
                  new com.example.budgetup.Notification();
              newNotification.setId(id);
              newNotification.setName(name);
              newNotification.setMessage(message);
              newNotification.setDate(time);
              newNotification.setUserEmail(email);
              newNotification.setStatus(true);
              db.notificationDao().insertNotification(newNotification);

              Intent intent = new Intent(NewNotificationActivity.this, Receiver.class);
              intent.putExtra("Name", name);
              intent.putExtra("Message", message);
              intent.putExtra("Id", id);
              PendingIntent pendingIntent =
                  PendingIntent.getBroadcast(NewNotificationActivity.this, REQUEST_CODE, intent, 0);
              AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
              Toast.makeText(view.getContext(), time+"", Toast.LENGTH_LONG)
                      .show();
              am.setRepeating(
                  AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
            }
            startActivity(new Intent(NewNotificationActivity.this, ProfileActivity.class));
          }
        });
  }

  private long getTime() {
    int hour = timePicker.getHour();
    int minute = timePicker.getMinute();
    int day = datePicker.getDayOfMonth();
    int month = datePicker.getMonth();
    int year = Calendar.getInstance().get(Calendar.YEAR);

    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day, hour, minute);
    return calendar.getTimeInMillis();
  }

  private void initViews() {
    btnSubmit = findViewById(R.id.btnSubmit);
    entName = findViewById(R.id.entName);
    entMessage = findViewById(R.id.entMessage);
    btnOk = findViewById(R.id.btnOk);
    btnOkMessage = findViewById(R.id.btnOkMessage);
    timePicker = findViewById(R.id.timePicker);
    timePicker.setIs24HourView(true);
    datePicker = findViewById(R.id.datePicker);
    toolbarName = findViewById(R.id.toolbarName);
    btnBack = findViewById(R.id.leftIcon);

    toolbarName.setText(R.string.new_notification);
  }
}
