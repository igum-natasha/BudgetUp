package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

  Button btnRegister;
  EditText etName, etEmail, etPassword;
  String email, name, password;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registration);
    initViews();
    btnRegister.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            email = etEmail.getText().toString();
            name = etName.getText().toString();
            password = etPassword.getText().toString();
            AppDatabase db = AppDatabase.build(getApplicationContext());
            User user = new User();
            if (name.equals("") & email.equals("") & password.equals("")) {
              Toast.makeText(
                      RegistrationActivity.this,
                      getResources().getString(R.string.regist_fail),
                      Toast.LENGTH_LONG)
                  .show();
              recreate();
            } else {
              user.setName(name);
              user.setPassword(password);
              user.setEmail(email);
              user.setStatus("online");
              db.userDao().insertUser(user);
              Toast.makeText(
                      RegistrationActivity.this,
                      getResources().getString(R.string.regist_suc),
                      Toast.LENGTH_LONG)
                  .show();
              startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
            }
          }
        });
  }

  private void initViews() {
    btnRegister = findViewById(R.id.btnUserRegister);
    etEmail = findViewById(R.id.etRegEmail);
    etName = findViewById(R.id.etName);
    etPassword = findViewById(R.id.etRegPassword);
  }
}
