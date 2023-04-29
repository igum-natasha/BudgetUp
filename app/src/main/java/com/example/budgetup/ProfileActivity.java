package com.example.budgetup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

  Dialog deleteDialog, languageDialog, questionsDialog;
  ImageButton btnExit, btnBack, btnNotification;
  LinearLayout backupGoogle, deleteDataLayout, languageLayout, shareLayout, questionsLayout;
  TextView userName, userEmail;
  String language;
  User user;
  AppDatabase db;
  GoogleSignInClient gsc;
    GoogleSignInOptions gso;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initViews();
    defineDeleteDialog();
    defineLanguageDialog();
    defineQuestionsDialog();
//    defineGoogleSync();
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    gsc = GoogleSignIn.getClient(this, gso);
    btnExit.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            user.setStatus("offline");
            db.userDao().update(user);
            List<Notification> notificationList =
                db.notificationDao().getNotificationsByEmail(user.getEmail());
            for (Notification notification : notificationList) {
              notification.setStatus(false);
              db.notificationDao().update(notification);
            }
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
    backupGoogle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signIn();
        }
    });
    btnNotification.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
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

  void signIn() {
      Intent signInIntent = gsc.getSignInIntent();
      startActivityForResult(signInIntent, 1000);
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

            } catch (ApiException e) {
                Toast.makeText(
                        ProfileActivity.this,
                        getResources().getString(R.string.errorGoogle),
                        Toast.LENGTH_LONG)
                        .show();
            }

        }
        defineGoogleSync();
//        gsc.signOut();
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
            List<Notification> notificationList =
                db.notificationDao().getNotificationsByEmail(user.getEmail());
            for (Notification notification : notificationList) {
              NotificationManager nMgr =
                  (NotificationManager)
                      getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
              nMgr.cancel(notification.getId());
            }
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

  private void defineGoogleSync() {
      GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
      GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(Scopes.DRIVE_FILE));
      if (googleSignInAccount != null) {
          credential.setSelectedAccount(googleSignInAccount.getAccount());
          com.google.api.services.drive.Drive googleDriveService = new Drive.Builder(
                  AndroidHttp.newCompatibleTransport(),
                  new GsonFactory(),
                  credential)
                  .setApplicationName(getString(R.string.app_name))
                  .build();
          String currentDBPath=getDatabasePath("database.db").getAbsolutePath();
          upload(currentDBPath, googleDriveService);
//          Toast.makeText(
//                  ProfileActivity.this,
//                  currentDBPath,
//                  Toast.LENGTH_LONG)
//                  .show();

      }
      gsc.signOut();
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

      ImageButton close = languageDialog.findViewById(R.id.close_icon);
      close.setOnClickListener(
              new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      languageDialog.dismiss();
                  }
              });
    RadioGroup radGrp = languageDialog.findViewById(R.id.radioGr);
    radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (id) {
                case R.id.radioRus:
                    language = "ru";
                    break;
                case R.id.radioEng:
                    language = "en";
                    break;
            }
            Locale locale = new Locale(language);
            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            user.setLanguage(language);
            db.userDao().update(user);
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
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
    btnNotification = findViewById(R.id.notification_icon);
    backupGoogle = findViewById(R.id.google);
    deleteDataLayout = findViewById(R.id.delete);
    languageLayout = findViewById(R.id.language);
    shareLayout = findViewById(R.id.share);
    questionsLayout = findViewById(R.id.questions);
  }

    private void upload(String dbPath, com.google.api.services.drive.Drive drive){
        File storageFile = new File();
        storageFile.setParents(Collections.singletonList("appDataFolder"));
        storageFile.setName("budgetdb");

//        File storageFileShm = new File();
//        storageFileShm.setParents(Collections.singletonList("appDataFolder"));
//        storageFileShm.setName("studentdb-shm");
//
//        File storageFileWal = new File();
//        storageFileWal.setParents(Collections.singletonList("appDataFolder"));
//        storageFileWal.setName("studentdb-wal");

        java.io.File filePath = new java.io.File(dbPath);
//        java.io.File filePathShm = new java.io.File(dbPathShm);
//        java.io.File filePathWal = new java.io.File(dbPathWal);
        FileContent mediaContent = new FileContent("",filePath);
//        FileContent mediaContentShm = new FileContent("",filePathShm);
//        FileContent mediaContentWal = new FileContent("",filePathWal);
        try {
            File file = drive.files().create(storageFile, mediaContent).execute();
            Toast.makeText(
                  ProfileActivity.this,
                    "Filename: " + file.getName()+ "File ID: " + file.getId(),
                  Toast.LENGTH_LONG)
                  .show();
//            System.out.printf("Filename: %s File ID: %s \n", file.getName(), file.getId());

//            File fileShm = googleDriveService.files().create(storageFileShm, mediaContentShm)                    .execute();
//            System.out.printf("Filename: %s File ID: %s \n", fileShm.getName(), fileShm.getId());
//
//            File fileWal = googleDriveService.files().create(storageFileWal, mediaContentWal)                  .execute();
//            System.out.printf("Filename: %s File ID: %s \n", fileWal.getName(), fileWal.getId());
    }
    catch(UserRecoverableAuthIOException e){
        startActivityForResult(e.getIntent(), 1);
    }
    catch(Exception e){
        e.printStackTrace();
    }

}
}
