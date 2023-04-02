package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class BankDataActivity extends AppCompatActivity implements PickiTCallbacks {

  private static final int PICK_FILE_REQUEST = 1;
  TextView toolbarName;
  ImageButton btnBack, btnSber, btnTink;
  LinearLayout selectFile;
  PickiT pickiT;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bank_data);

    initViews();
    pickiT = new PickiT(this, this, this);
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

    selectFile.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (ActivityCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(
                  BankDataActivity.this,
                  new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                  1);
              ActivityCompat.requestPermissions(
                  BankDataActivity.this,
                  new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                  1);
            } else {
              Intent intent = new Intent();
              intent.setType("*/*");
              intent.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(
                  Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
            }
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_FILE_REQUEST) {
      if (resultCode == RESULT_OK) {
        if (data == null) {
          // no data present
          return;
        }
        pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
      }
    }
  }

  private void initViews() {
    btnBack = findViewById(R.id.leftIcon);
    toolbarName = findViewById(R.id.toolbarName);
    btnSber = findViewById(R.id.iconSber);
    btnTink = findViewById(R.id.iconTink);
    selectFile = findViewById(R.id.selectFile);

    toolbarName.setText(R.string.new_csv);
  }

  @Override
  public void PickiTonUriReturned() {}

  @Override
  public void PickiTonStartListener() {}

  @Override
  public void PickiTonProgressUpdate(int progress) {}

  @Override
  public void PickiTonCompleteListener(
      String path,
      boolean wasDriveFile,
      boolean wasUnknownProvider,
      boolean wasSuccessful,
      String Reason) {

    //  Chick if it was successful
    if (wasSuccessful) {
      //  Set returned path to TextView
      Toast.makeText(this, "Selected File Path:" + path, Toast.LENGTH_SHORT).show();
      DataParser parser = new DataParser(path);
      try {
        parser.parseFile(getWindow().getDecorView().getRootView());
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void PickiTonMultipleCompleteListener(
      ArrayList<String> paths, boolean wasSuccessful, String Reason) {}
}
