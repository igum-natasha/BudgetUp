package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

  Button btnLogin, btnRegister;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_first);
    initViews();
    btnRegister.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(FirstActivity.this, RegistrationActivity.class));
          }
        });
    btnLogin.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(FirstActivity.this, LoginActivity.class));
          }
        });
  }

  private void initViews() {
    btnLogin = findViewById(R.id.btnLogin);
    btnRegister = findViewById(R.id.btnRedister);
  }
}
