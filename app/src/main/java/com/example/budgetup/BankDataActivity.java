package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BankDataActivity extends AppCompatActivity {

  TextView toolbarName;
  ImageButton btnBack, btnSber, btnTink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bank_data);

    initViews();
    btnBack.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
          }
        });

    btnSber.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent browserIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://online.sberbank.ru/"));
            startActivity(browserIntent);
          }
        });

    btnTink.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent browserIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tinkoff.ru/login/"));
            startActivity(browserIntent);
          }
        });

    BottomNavigationView nav_view = findViewById(R.id.navigationView);
    nav_view.setSelectedItemId(R.id.home);
    nav_view.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.home:
                return true;
              case R.id.statistic:
                Intent intent = new Intent(BankDataActivity.this, StatisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
              case R.id.account:
                startActivity(new Intent(BankDataActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
          }
        });
  }

  private void initViews() {
    btnBack = findViewById(R.id.leftIcon);
    toolbarName = findViewById(R.id.toolbarName);
    btnSber = findViewById(R.id.iconSber);
    btnTink = findViewById(R.id.iconTink);

    toolbarName.setText(R.string.new_income);
  }
}
