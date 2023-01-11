package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

  Dialog deleteDialog;
  ImageButton btnExit, btnBack;
  LinearLayout backupGoogle, deleteDataLayout, languageLayout, shareLayout, questionsLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initViews();
    defineDeleteDialog();
    deleteDataLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteDialog.show();
      }
    });
    BottomNavigationView nav_view = findViewById(R.id.navigationView);

    nav_view.setSelectedItemId(R.id.account);
    nav_view.setOnItemSelectedListener(
            item -> {
              switch (item.getItemId()) {
                case R.id.home:
                  startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                  overridePendingTransition(0, 0);
                  return true;
                case R.id.statistic:
                  Intent intent = new Intent(ProfileActivity.this, StatisticsActivity.class);
                  startActivity(intent);
                  overridePendingTransition(0, 0);
                  return true;
                case R.id.account:
                  return true;
              }
              return false;
            });
  }

  private void defineDeleteDialog() {
    deleteDialog = new Dialog(ProfileActivity.this);
    deleteDialog.setContentView(R.layout.delete_data_dialog);
    deleteDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    deleteDialog
            .getWindow()
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    deleteDialog.setCancelable(false);
    Button ok = deleteDialog.findViewById(R.id.btnOk);
    Button cancel = deleteDialog.findViewById(R.id.btnCancel);
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteDialog.dismiss();
      }
    });
    ok.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                deleteDialog.dismiss(); // TODO: delete data from db and cloud
              }
            });
  }

  private void initViews() {

    btnBack = findViewById(R.id.left_icon);
    btnExit = findViewById(R.id.exit_icon);
    backupGoogle = findViewById(R.id.google);
    deleteDataLayout = findViewById(R.id.delete);
    languageLayout = findViewById(R.id.language);
    shareLayout = findViewById(R.id.share);
    questionsLayout = findViewById(R.id.questions);
  }
}
