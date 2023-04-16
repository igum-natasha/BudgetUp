package com.example.budgetup;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Receiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    showNotification(context);
  }

  public void showNotification(Context context) {
    Intent intent = new Intent(context, HomeActivity.class);
    int reqCode = 0;
    @SuppressLint("UnspecifiedImmutableFlag")
    PendingIntent pi = PendingIntent.getActivity(context, reqCode, intent, 0);
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context, "notifyBudgetUp")
            .setSmallIcon(R.drawable.ic_main_logo)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification));
    mBuilder.setContentIntent(pi);
    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
    mBuilder.setAutoCancel(true);
    NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
    mNotificationManager.notify(200, mBuilder.build());
  }
}
