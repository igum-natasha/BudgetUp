package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

  Dialog deleteDialog, languageDialog, questionsDialog;
  ImageButton btnExit, btnBack;
  LinearLayout backupGoogle, deleteDataLayout, languageLayout, shareLayout, questionsLayout;
  TextView userName, userEmail;
  String language;
  User user;
  AppDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initViews();
    defineDeleteDialog();
    defineLanguageDialog();
    defineQuestionsDialog();
    btnExit.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            user.setStatus("offline");
            db.userDao().update(user);
            startActivity(new Intent(ProfileActivity.this, FirstActivity.class));
          }
        });
    btnBack.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
          }
        });
    deleteDataLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            deleteDialog.show();
          }
        });
    languageLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            languageDialog.show();
          }
        });

    questionsLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            questionsDialog.show();
          }
        });
    shareLayout.setOnClickListener(
        new View.OnClickListener() {
          @SuppressLint("QueryPermissionsNeeded")
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_email));
            intent.setData(Uri.parse("mailto: igum.natasha@gmail.com"));
            if (intent.resolveActivity(getPackageManager()) != null) {
              startActivity(intent);
            }
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

  @SuppressLint("UseCompatLoadingForDrawables")
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
    cancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            deleteDialog.dismiss();
          }
        });
    ok.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            db.userDao().deleteByEmail(user.getEmail());
            Toast.makeText(
                    ProfileActivity.this,
                    getResources().getString(R.string.delete_suc),
                    Toast.LENGTH_LONG)
                .show();
            startActivity(new Intent(ProfileActivity.this, FirstActivity.class));
            // TODO: delete data from db and cloud

          }
        });
  }

  @SuppressLint({"UseCompatLoadingForDrawables", "UseSwitchCompatOrMaterialCode"})
  private void defineLanguageDialog() {
    languageDialog = new Dialog(ProfileActivity.this);
    languageDialog.setContentView(R.layout.language_dialog);
    languageDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    languageDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    languageDialog.setCancelable(false);
    Switch rus = languageDialog.findViewById(R.id.swRus);
    Switch eng = languageDialog.findViewById(R.id.swEng);
    ImageButton close = languageDialog.findViewById(R.id.close_icon);
    if ("en".equals(user.getLanguage())) {
      eng.setChecked(true);
    } else {
      rus.setChecked(true);
    }
    rus.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
              language = "ru";
              Locale locale = new Locale(language);
              Resources resources = getResources();
              Configuration configuration = resources.getConfiguration();
              configuration.setLocale(locale);
              resources.updateConfiguration(configuration, resources.getDisplayMetrics());
              user.setLanguage(language);
              db.userDao().update(user);
              eng.setChecked(false);
              startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            } else {
              eng.setChecked(true);
            }
          }
        });
    eng.setOnCheckedChangeListener(
        new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
              language = "en";
              Locale locale = new Locale(language);
              Resources resources = getResources();
              Configuration configuration = resources.getConfiguration();
              configuration.setLocale(locale);
              resources.updateConfiguration(configuration, resources.getDisplayMetrics());
              user.setLanguage(language);
              db.userDao().update(user);
              rus.setChecked(false);
              startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            } else {
              rus.setChecked(true);
            }
          }
        });

    close.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            languageDialog.dismiss();
          }
        });
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineQuestionsDialog() {
    questionsDialog = new Dialog(ProfileActivity.this);
    questionsDialog.setContentView(R.layout.feedback_dialog);
    questionsDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    questionsDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    questionsDialog.setCancelable(false);
    Button send = questionsDialog.findViewById(R.id.btnSend);
    ImageButton close = questionsDialog.findViewById(R.id.close_icon);
    EditText feedback = questionsDialog.findViewById(R.id.entFeedback);
    close.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            questionsDialog.dismiss();
          }
        });
    send.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
            intent.setData(Uri.parse("mailto: igum.natasha@gmail.com"));
            if (intent.resolveActivity(getPackageManager()) != null) {
              startActivity(intent);
            } else {
              Toast.makeText(
                      ProfileActivity.this, getString(R.string.fill_field), Toast.LENGTH_LONG)
                  .show();
            }
          }
        });
  }

  private void initViews() {
    db = AppDatabase.build(getApplicationContext());
    user = db.userDao().getByStatus("online");
    userName = findViewById(R.id.userName);
    userName.setText(user.getName());
    userEmail = findViewById(R.id.userEmail);
    userEmail.setText(user.getEmail());
    btnBack = findViewById(R.id.left_icon);
    btnExit = findViewById(R.id.exit_icon);
    backupGoogle = findViewById(R.id.google);
    deleteDataLayout = findViewById(R.id.delete);
    languageLayout = findViewById(R.id.language);
    shareLayout = findViewById(R.id.share);
    questionsLayout = findViewById(R.id.questions);
  }
}
