package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.util.UUID;

public class NewExpenseActivity extends AppCompatActivity {

  String[] paymentItem = {"Cash", "Credit card"};
  String payment, category;
  int image;
  AutoCompleteTextView autoCompleteTextView;
  ArrayAdapter<String> arrayAdapter;
  EditText entCount, entNote, entCard;
  LinearLayout lCard;
  TextView toolbarName;
  Button btnCategory;
  ImageButton btnBackspace, btnOkNote, btnBack, btnOkCard;
  Dialog categoryDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_expense);
    initViews();
    definePaymentMenu();
    defineCategoryDialog();

    btnBack.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(NewExpenseActivity.this, HomeActivity.class));
          }
        });
    btnCategory.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            categoryDialog.show();
          }
        });
    btnBackspace.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            entCount.setText("");
          }
        });

    btnOkNote.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (view != null) {
              InputMethodManager imm =
                  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
          }
        });
    btnOkCard.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (view != null) {
              InputMethodManager imm =
                  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                Intent intent = new Intent(NewExpenseActivity.this, StatisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
              case R.id.account:
                startActivity(new Intent(NewExpenseActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
          }
        });
  }

  private void definePaymentMenu() {
    arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, paymentItem);
    autoCompleteTextView.setAdapter(arrayAdapter);

    autoCompleteTextView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            payment = adapterView.getItemAtPosition(i).toString();
            if (payment.equals(paymentItem[1])) {
              lCard.setVisibility(View.VISIBLE);
            } else {
              entCard.setText("None");
            }
          }
        });
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineCategoryDialog() {
    categoryDialog = new Dialog(NewExpenseActivity.this);
    categoryDialog.setContentView(R.layout.category_expense_dialog);
    categoryDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    categoryDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    categoryDialog.setCancelable(false);
    ImageButton close = categoryDialog.findViewById(R.id.closeIcon);
    RadioGroup radGrp = categoryDialog.findViewById(R.id.radioGroup);
    radGrp.setOnCheckedChangeListener(
        new RadioGroup.OnCheckedChangeListener() {
          @SuppressLint({"NonConstantResourceId", "ResourceType"})
          @Override
          public void onCheckedChanged(RadioGroup arg0, int id) {
            switch (id) {
              case R.id.radioCar:
                category = "car";
                break;
              case R.id.radioCommun:
                category = "phone";
                break;
              case R.id.radioRest:
                category = "cafe";
                break;
              case R.id.radioClothes:
                category = "clothes";
                break;
              case R.id.radioGift:
                category = "gifts";
                break;
              case R.id.radioFood:
                category = "food";
                break;
              case R.id.radioHealth:
                category = "health";
                break;
              case R.id.radioHouse:
                category = "house";
                break;
              case R.id.radioSports:
                category = "sports";
                break;
              case R.id.radioPets:
                category = "pets";
                break;
              case R.id.radioTransport:
                category = "transport";
                break;
              case R.id.radioEnter:
                category = "pastime";
                break;
              case R.id.radioServices:
                category = "services";
                break;
              case R.id.radioOthers:
                category = "others";
                break;
              default:
                break;
            }
            Date date = new Date();
            long dateLong = date.getTime();
            AppDatabase db = AppDatabase.build(getApplicationContext());
            User user = db.userDao().getByStatus("online");
            Expense expense = new Expense();
            expense.setId(UUID.randomUUID().toString());
            expense.setDate(dateLong);
            expense.setCategory(category);
            expense.setCurrency("RUB");
            expense.setNote(entNote.getText().toString());
            expense.setValue("-" + entCount.getText().toString());
            expense.setUserEmail(user.getEmail());
            expense.setPayment(payment);
            expense.setCardNum(entCard.getText().toString());
            image =
                getResources()
                    .getIdentifier(category, "drawable", getApplicationContext().getPackageName());
            expense.setImage(image);
            db.expenseDao().insertExpense(expense);
            categoryDialog.dismiss();
            Toast.makeText(
                    NewExpenseActivity.this,
                    getResources().getString(R.string.expense_add_suc),
                    Toast.LENGTH_LONG)
                .show();
            startActivity(new Intent(NewExpenseActivity.this, HomeActivity.class));
          }
        });
    close.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            categoryDialog.dismiss();
          }
        });
  }

  private void initViews() {
    btnBack = findViewById(R.id.leftIcon);
    autoCompleteTextView = findViewById(R.id.autoComplete);
    entCount = findViewById(R.id.entIncomeCount);
    entNote = findViewById(R.id.entNote);
    entCard = findViewById(R.id.entCard);
    lCard = findViewById(R.id.linCard);
    btnCategory = findViewById(R.id.btnCategory);
    btnBackspace = findViewById(R.id.btnBackspace);
    btnOkNote = findViewById(R.id.btnOk);
    btnOkCard = findViewById(R.id.btnOkCard);
    toolbarName = findViewById(R.id.toolbarName);

    toolbarName.setText(R.string.new_expense);
    lCard.setVisibility(View.INVISIBLE);
  }
}
