package com.example.budgetup;

import static com.github.mikephil.charting.utils.ColorTemplate.*;

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

public class HomeActivity extends AppCompatActivity {

  //  private List<Expense> expenses;
  private String[][] expenses;
  private RecyclerView rv;
  TextView tvInfoExp, tvToday;
  Dialog addDialog;
  Button btnTrack;
  ImageButton btnAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initViews();
    initPieChart();
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
    //    AppDatabase db = AppDatabase.build(getApplicationContext());
    //    expenses = db.expenseDao().getAll();
    Resources resources = getApplicationContext().getResources();
    int resourceId =
        resources.getIdentifier("food", "drawable", getApplicationContext().getPackageName());
    expenses =
        new String[][] {
          {"Magnit", "-1000 RUB", Integer.toString(resourceId)},
          {"Sportmaster", "-2800 RUB", Integer.toString(resourceId)},
          {"Petrol", "-2500 RUB", Integer.toString(resourceId)}
        };
    if (expenses.length == 0) {
      tvToday.setVisibility(View.INVISIBLE);
      tvInfoExp.setVisibility(View.VISIBLE);
      btnTrack.setVisibility(View.VISIBLE);
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

  private void initPieChart() {
    PieChart pieChart = findViewById(R.id.pieChart);
    ArrayList<PieEntry> expenses = new ArrayList<>();
    expenses.add(new PieEntry(508, "Food"));
    expenses.add(new PieEntry(306, "Clothes"));
    expenses.add(new PieEntry(150, "Car"));
    expenses.add(new PieEntry(1400, "Gifts"));
    expenses.add(new PieEntry(100, "House"));
    expenses.add(new PieEntry(350, "Transport"));
    int[] colors = {
      getResources().getColor(R.color.menu_1),
      getResources().getColor(R.color.menu_2),
      getResources().getColor(R.color.menu_3),
      getResources().getColor(R.color.menu_4),
      getResources().getColor(R.color.menu_5),
      getResources().getColor(R.color.menu_6)
    };
    PieDataSet pieDataSet = new PieDataSet(expenses, "Expenses");
    pieDataSet.setColors(colors);
    pieDataSet.setDrawValues(false);

    PieData pieData = new PieData(pieDataSet);
    Legend l = pieChart.getLegend();
    l.setEnabled(false);
    pieChart.setData(pieData);
    //    pieChart.setDrawEntryLabels(false);
    pieChart.setEntryLabelColor(getResources().getColor(R.color.base_600));
    pieChart.getDescription().setEnabled(false);
    pieChart.setCenterText("RUB 2814");
    pieChart.setCenterTextSize(18f);
    pieChart.setCenterTextColor(getResources().getColor(R.color.error_800));
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
                new Intent(HomeActivity.this, ProfileActivity.class); // TODO: Add expense activity
            startActivity(intent);
          }
        });
  }
}
