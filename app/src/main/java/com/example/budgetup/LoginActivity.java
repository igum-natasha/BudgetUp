package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

  Button btnLogin;
  EditText etEmail, etPassword;
  String email, password;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initViews();
    btnLogin.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            AppDatabase db = AppDatabase.build(getApplicationContext());
            User user = db.userDao().getByEmail(email);
            if (user.getPassword().equals(password)) {
              user.setStatus("online");
              Toast.makeText(
                      LoginActivity.this,
                      getResources().getString(R.string.login_suc),
                      Toast.LENGTH_LONG)
                  .show();
              startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
              Toast.makeText(
                      LoginActivity.this,
                      getResources().getString(R.string.login_fail),
                      Toast.LENGTH_LONG)
                  .show();
              startActivity(new Intent(LoginActivity.this, FirstActivity.class));
            }
          }
        });
  }

  private void initViews() {
    btnLogin = findViewById(R.id.btnUserLogin);
    etEmail = findViewById(R.id.etEmail);
    etPassword = findViewById(R.id.etPassword);
  }
}
