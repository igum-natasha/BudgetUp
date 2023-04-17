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
    AppDatabase db = AppDatabase.build(getApplicationContext());
    List<User> users = db.userDao().getAll();
    if (users.size() != 0) {
      User user = db.userDao().getByStatus("online");
      String language = "en";
      if (user != null) {
        language = user.getLanguage();
      } else {
        startActivity(new Intent(SplashActivity.this, FirstActivity.class));
      }
      Locale locale = new Locale(language);
      Resources resources = getResources();
      Configuration configuration = resources.getConfiguration();
      configuration.setLocale(locale);
      resources.updateConfiguration(configuration, resources.getDisplayMetrics());
      startActivity(new Intent(SplashActivity.this, HomeActivity.class));

    } else {
      startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
  }
}
