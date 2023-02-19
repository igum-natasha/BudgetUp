package com.example.budgetup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

public class NewExpenseActivity extends AppCompatActivity {

  String[] paymentItem = {"Cash", "Credit card"};
  String payment, category;
  int image;
  AutoCompleteTextView autoCompleteTextView;
  ArrayAdapter<String> arrayAdapter;
  EditText entCount, entNote;
  Button btnCategory;
  ImageButton btnBackspace, btnOkNote, btnBack;
  Dialog categoryDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_expense);
    initViews();
    definePaymentMenu();
    defineCategoryDialog();

    btnBack.setOnClickListener(new View.OnClickListener() {
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
  }

  private void definePaymentMenu() {
    arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, paymentItem);
    autoCompleteTextView.setAdapter(arrayAdapter);

    autoCompleteTextView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            payment = adapterView.getItemAtPosition(i).toString() + "\n";
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
          @SuppressLint("NonConstantResourceId")
          @Override
          public void onCheckedChanged(RadioGroup arg0, int id) {
            switch (id) {
              case R.id.radioCar:
                category = "car";
                break;
              case R.id.radioCommun:
                category = "communication";
                break;
              case R.id.radioRest:
                category = "restaurants";
                break;
              case R.id.radioClothes:
                category = "clothes";
                break;
              case R.id.radioGift:
                category = "gift";
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
                category = "entertainment";
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
    btnBack = findViewById(R.id.left_icon);
    autoCompleteTextView = findViewById(R.id.autoComplete);
    entCount = findViewById(R.id.entIncomeCount);
    entNote = findViewById(R.id.entNote);
    btnCategory = findViewById(R.id.btnCategory);
    btnBackspace = findViewById(R.id.btnBackspace);
    btnOkNote = findViewById(R.id.btnOk);
  }
}
