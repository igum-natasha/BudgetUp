package com.example.budgetup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

  private List<Expense> expenses;
  private RecyclerView rv;
  final Random random = new Random();
  TextView tvInfoExp, tvToday;
  Dialog addDialog;
  Button btnTrack;
  ImageButton btnAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initViews();
    showExpenses();
    defineAddDialog();

    btnAdd.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            addDialog.show();
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
                Intent intent = new Intent(HomeActivity.this, StatisticsActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
              case R.id.account:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
          }
        });
  }

  private void initializeData() {
    AppDatabase db = AppDatabase.build(getApplicationContext());
    expenses = db.expenseDao().getAll();
    if (expenses.isEmpty()) {
      tvToday.setVisibility(View.INVISIBLE);
      tvInfoExp.setVisibility(View.VISIBLE);
      btnTrack.setVisibility(View.VISIBLE);
    } else {
      initPieChart();
    }
  }

  private void initializeAdapter() {
    RVAdapterToday adapter = new RVAdapterToday(expenses);
    rv.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterToday.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {
            //                 expenseInfoDialog.show();
          }

          @Override
          public void onItemLongClick(int position, View v) {}
        });
  }

  private void showExpenses() {
    rv = findViewById(R.id.rv);

    LinearLayoutManager llm = new LinearLayoutManager(this);
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeData();
    initializeAdapter();
  }

  private void initViews() {
    tvToday = findViewById(R.id.tvToday);
    btnTrack = findViewById(R.id.btnTrack);
    tvInfoExp = findViewById(R.id.tvInfoExp);
    btnAdd = findViewById(R.id.plus_icon);
    tvInfoExp.setVisibility(View.INVISIBLE);
    btnTrack.setVisibility(View.INVISIBLE);
  }

  @SuppressLint("DefaultLocale")
  private void initPieChart() {
    PieChart pieChart = findViewById(R.id.pieChart);
    ArrayList<PieEntry> data_expenses = new ArrayList<>();
    int[] palette = {
            getResources().getColor(R.color.menu_1),
            getResources().getColor(R.color.menu_2),
            getResources().getColor(R.color.menu_3),
            getResources().getColor(R.color.menu_4),
            getResources().getColor(R.color.menu_5),
            getResources().getColor(R.color.menu_6),
            getResources().getColor(R.color.menu_7)
    };
    int[] colors = new int[expenses.size()];
    float max_income = 0, max_expense = 0;
    String currency = expenses.get(0).getCurrency();
    for(int i = 0; i < expenses.size(); i++) {
      float value = Float.parseFloat(expenses.get(i).getValue());
      data_expenses.add(new PieEntry(Float.parseFloat(expenses.get(i).getValue().replace('-', ' ')), expenses.get(i).getCategory()));
      if ( value > 0 ) {
        max_income += value;
      }
      if (value < 0 ) {
        max_expense += value;
      }
      colors[i] = palette[i];
    }
    PieDataSet pieDataSet = new PieDataSet(data_expenses, "Expenses");
    pieDataSet.setColors(colors);
    pieDataSet.setDrawValues(false);

    PieData pieData = new PieData(pieDataSet);
    Legend l = pieChart.getLegend();
    l.setEnabled(false);
    pieChart.setData(pieData);
    pieChart.setEntryLabelColor(getResources().getColor(R.color.base_600));
    pieChart.getDescription().setEnabled(false);
    String text = String.format("%s %.2f\n%s %.2f", currency, max_income, currency, max_expense);
    pieChart.setCenterText(text);
    pieChart.setCenterTextSize(18f);
    pieChart.setCenterTextColor(getResources().getColor(R.color.primary_400));
    pieChart.animate();
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineAddDialog() {
    addDialog = new Dialog(HomeActivity.this);
    addDialog.setContentView(R.layout.add_dialog);
    addDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
    addDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    addDialog.setCancelable(false);
    LinearLayout income = addDialog.findViewById(R.id.layoutIncome);
    LinearLayout expense = addDialog.findViewById(R.id.layoutExpense);
    income.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent =
                new Intent(HomeActivity.this, NewIncomeActivity.class); // TODO: Add income activity
            startActivity(intent);
          }
        });
    expense.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent =
                new Intent(
                    HomeActivity.this, NewExpenseActivity.class); // TODO: Add expense activity
            startActivity(intent);
          }
        });
  }
}
