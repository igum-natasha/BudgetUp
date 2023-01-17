package com.example.budgetup;

import static com.github.mikephil.charting.utils.ColorTemplate.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initPieChart();
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
            getResources().getColor(R.color.menu_6)};
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
}
