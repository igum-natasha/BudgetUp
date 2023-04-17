package com.example.budgetup;

import static java.util.UUID.*;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Receiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      String name = bundle.getString("Name");
      String message = bundle.getString("Message");
      int id = intent.getIntExtra("Id", 0);
      showNotification(context, name, message, id);
    }
  }

  public void showNotification(Context context, String name, String message, int id) {
    Intent intent = new Intent(context, HomeActivity.class);
    int reqCode = 0;
    @SuppressLint("UnspecifiedImmutableFlag")
    PendingIntent pi = PendingIntent.getActivity(context, reqCode, intent, 0);
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context, "notifyBudgetUp")
            .setSmallIcon(R.drawable.ic_main_logo)
            .setContentTitle(name)
            .setContentText(message);
    mBuilder.setContentIntent(pi);
    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
    mBuilder.setAutoCancel(true);
    NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);

    mNotificationManager.notify(id, mBuilder.build());
  }
}
