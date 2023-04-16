package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import java.util.List;
import java.util.Locale;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

  int REQUEST_CODE = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    createNotificationChannel();
    AppDatabase db = AppDatabase.build(getApplicationContext());
    List<User> users = db.userDao().getAll();
    if (users.size() != 0) {
      User user = db.userDao().getByStatus("online");
      Locale locale = new Locale(user.getLanguage());
      Resources resources = getResources();
      Configuration configuration = resources.getConfiguration();
      configuration.setLocale(locale);
      resources.updateConfiguration(configuration, resources.getDisplayMetrics());

      Intent intent = new Intent(SplashActivity.this, Receiver.class);
      PendingIntent pendingIntent =
          PendingIntent.getBroadcast(SplashActivity.this, REQUEST_CODE, intent, 0);
      AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
      am.setRepeating(
          AlarmManager.RTC_WAKEUP,
          System.currentTimeMillis(),
          AlarmManager.INTERVAL_DAY * 7,
          pendingIntent);
      startActivity(new Intent(SplashActivity.this, HomeActivity.class));

    } else {
      startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
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
}
