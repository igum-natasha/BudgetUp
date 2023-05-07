package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationActivity extends AppCompatActivity {

  private ImageButton btnAdd;
  CircleImageView btnLeft;
  private TextView title;
  private RecyclerView rvNotification;
  Dialog notificationDialog;
  List<Notification> notificationList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification2);
    initViews();

    showNotifications();

    btnAdd.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(NotificationActivity.this, NewNotificationActivity.class));
          }
        });

    BottomNavigationView nav_view = findViewById(R.id.navigationView);

    nav_view.setSelectedItemId(R.id.account);
    nav_view.setOnItemSelectedListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.home:
              startActivity(new Intent(NotificationActivity.this, HomeActivity.class));
              overridePendingTransition(0, 0);
              return true;
            case R.id.statistic:
              Intent intent = new Intent(NotificationActivity.this, StatisticsActivity.class);
              startActivity(intent);
              overridePendingTransition(0, 0);
              return true;
            case R.id.account:
              startActivity(new Intent(NotificationActivity.this, ProfileActivity.class));
              overridePendingTransition(0, 0);
              return true;
          }
          return false;
        });
  }

  private void initializeData() {
    AppDatabase db = AppDatabase.build(getApplicationContext());
    User user = db.userDao().getByStatus("online");
    notificationList = db.notificationDao().getNotificationsByEmail(user.getEmail());
  }

  private void initializeAdapter() {
    RVAdapterNotification adapter = new RVAdapterNotification(notificationList);
    rvNotification.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterNotification.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {
            defineNotificationDialog(position);
            notificationDialog.show();
          }

          @Override
          public void onItemLongClick(int position, View v) {}
        });
  }

  private void showNotifications() {
    rvNotification = findViewById(R.id.rvNotification);

    LinearLayoutManager llm = new LinearLayoutManager(this);
    rvNotification.setLayoutManager(llm);
    initializeData();
    initializeAdapter();
  }

  private void initViews() {
    btnAdd = findViewById(R.id.plus_icon);
    btnLeft = findViewById(R.id.avatar_icon);
    btnLeft.setVisibility(View.GONE);
    title = findViewById(R.id.user_name);

    title.setText(getResources().getString(R.string.notification));
  }

  private void defineNotificationDialog(int position) {
    notificationDialog = new Dialog(NotificationActivity.this);
    notificationDialog.setContentView(R.layout.notification_dialog);
    notificationDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    notificationDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    notificationDialog.getWindow().setGravity(Gravity.CENTER);
    notificationDialog.setCancelable(false);
    ImageView status = notificationDialog.findViewById(R.id.notifStatus);
    TextView date = notificationDialog.findViewById(R.id.dateTitle);
    TextView name = notificationDialog.findViewById(R.id.notifName);
    TextView message = notificationDialog.findViewById(R.id.notifMessage);
    Notification selectedNotification = notificationList.get(position);
    ImageButton close = notificationDialog.findViewById(R.id.closeIcon);
    ImageButton delete = notificationDialog.findViewById(R.id.deleteIcon);
    String image;
    if (selectedNotification.getStatus()) {
      image = "check";
    } else {
      image = "close";
    }
    status.setBackgroundResource(
        getApplicationContext()
            .getResources()
            .getIdentifier(image, "drawable", getApplicationContext().getPackageName()));
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd, HH:mm");
    date.setText(df.format(selectedNotification.getDate()));
    name.setText(selectedNotification.getName());
    message.setText(selectedNotification.getMessage());
    close.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            notificationDialog.dismiss();
          }
        });
    delete.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            AppDatabase db = AppDatabase.build(getApplicationContext());
            db.notificationDao().deleteById(selectedNotification.getId());
            notificationDialog.dismiss();
            finish();
          }
        });
  }
}
