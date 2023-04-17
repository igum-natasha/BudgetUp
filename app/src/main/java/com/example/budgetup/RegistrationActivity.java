package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

  Button btnRegister;
  EditText etName, etEmail, etPassword;
  String email, name, password;
  int REQUEST_CODE = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    initViews();
    createNotificationChannel();
    btnRegister.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            email = etEmail.getText().toString();
            name = etName.getText().toString();
            password = etPassword.getText().toString();
            AppDatabase db = AppDatabase.build(getApplicationContext());
            List<User> users = db.userDao().getAll();

            User user = new User();
            if (name.equals("") & email.equals("") & password.equals("")) {
              Toast.makeText(
                      RegistrationActivity.this,
                      getResources().getString(R.string.regist_fail),
                      Toast.LENGTH_LONG)
                  .show();
              recreate();
            } else {
              for (User user1 : users) {
                if (email.equals(user1.getEmail())) {
                  Toast.makeText(
                          RegistrationActivity.this,
                          getResources().getString(R.string.regist_fail_email, email),
                          Toast.LENGTH_LONG)
                      .show();
                  recreate();
                }
              }
              user.setName(name);
              user.setPassword(password);
              user.setEmail(email);
              user.setStatus("online");
              db.userDao().insertUser(user);
              Toast.makeText(
                      RegistrationActivity.this,
                      getResources().getString(R.string.regist_suc),
                      Toast.LENGTH_LONG)
                  .show();

              long time = AlarmManager.INTERVAL_DAY * 7;
              Intent intent = new Intent(RegistrationActivity.this, Receiver.class);
              intent.putExtra("Name", getString(R.string.notification_title));
              intent.putExtra("Message", getString(R.string.notification_desc));
              intent.putExtra("Date", time);
              PendingIntent pendingIntent =
                  PendingIntent.getBroadcast(RegistrationActivity.this, REQUEST_CODE, intent, 0);
              AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
              am.setRepeating(
                  AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time, pendingIntent);
              startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
            }
          }
        });
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "BudgetUpChannel";
      String description = "Channel for BudgetUp";
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel("notifyBudgetUp", name, importance);
      channel.setDescription(description);

      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  private void initViews() {
    btnRegister = findViewById(R.id.btnUserRegister);
    etEmail = findViewById(R.id.etRegEmail);
    etName = findViewById(R.id.etName);
    etPassword = findViewById(R.id.etRegPassword);
  }
}
