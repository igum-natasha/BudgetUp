package com.example.budgetup;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JournalFragment extends Fragment {

  private View view;
  private String[][] days;
  private List<Expense> expenses;
  private RecyclerView rvToday;
  private RecyclerView rv;
  TextView tvWeekName, tvMonthDay, expenseCount;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_journal, container, false);
    initViews();
    showCalendar();
    showExpenses();
    initBarChart();
    return view;
  }

  @SuppressLint("SetTextI18n")
  private void initBarChart() {
    BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
    String category;
    ArrayList<BarEntry> data_expenses = new ArrayList<>();
    ArrayList<String> xAxisLabel =
        new ArrayList<>(Arrays.asList("food", "clothes", "car", "gift", "house", "transport"));
    float[] sumByCategory = {0, 0, 0, 0, 0, 0};
    float maxSum = 0, sum = 0;
    for (int i = 0; i < expenses.size(); i++) {
      category = expenses.get(i).getCategory();
      if (xAxisLabel.contains(category)) {
        sumByCategory[xAxisLabel.indexOf(category)] +=
            Float.parseFloat(expenses.get(i).getValue().replace('-', ' '));
      }
    }
    for (int i = 0; i < sumByCategory.length; i++) {
      data_expenses.add(new BarEntry(i, sumByCategory[i]));
      if (sumByCategory[i] > maxSum) {
        maxSum = sumByCategory[i];
        sum += sumByCategory[i];
      }
    }
    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

    BarDataSet barDataSet = new BarDataSet(data_expenses, "Expenses");
    int[] colors = {
      getResources().getColor(R.color.menu_1),
      getResources().getColor(R.color.menu_2),
      getResources().getColor(R.color.menu_3),
      getResources().getColor(R.color.menu_4),
      getResources().getColor(R.color.menu_5),
      getResources().getColor(R.color.menu_6)
    };
    barDataSet.setColors(colors);
    barDataSet.setDrawValues(false);

    BarData barData = new BarData(barDataSet);
    Legend l = barChart.getLegend();
    l.setEnabled(false);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(false);
    xAxis.setGranularity(1f);
    xAxis.setLabelCount(xAxisLabel.size());
    barChart.getAxisLeft().setGridColor(getResources().getColor(R.color.primary_200));
    barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.base_500));
    //    barChart.getAxisLeft().setDrawGridLines(false);
    barChart.getAxisRight().setEnabled(false);
    barChart.setFitBars(true);
    barChart.setData(barData);
    barChart.getDescription().setEnabled(false);
    barChart.animateY((int) maxSum);
    expenseCount.setText(sum + " " + expenses.get(0).getCurrency());
  }

  private void initializeData() {
    //    AppDatabase db = AppDatabase.build(getApplicationContext());
    //    expenses = db.expenseDao().getAll();
    days =
        new String[][] {
          {"Mon", "30"}, {"Tue", "31"}, {"Wed", "1"}, {"Thu", "2"}, {"Fri", "3"},
          {"Sat", "4"}, {"Sun", "5"}, {"Mon", "6"}, {"Tue", "7"}, {"Wed", "8"},
        };
  }

  private void initializeAdapter() {
    RVAdapterCalendar adapter = new RVAdapterCalendar(days);
    rv.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterCalendar.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {}

          @Override
          public void onItemLongClick(int position, View v) {}
        });
  }

  private void showCalendar() {
    rv = view.findViewById(R.id.rv);

    GridLayoutManager llm =
        new GridLayoutManager(view.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeData();
    initializeAdapter();
  }

  private void initializeDataToday() {
    AppDatabase db = AppDatabase.build(view.getContext());
    expenses = db.expenseDao().getAll();
    if (expenses.isEmpty()) {
      //      deleteDialog.show();
      // TODO: dialog
    }
  }

  private void initializeAdapterToday() {
    RVAdapterToday adapter = new RVAdapterToday(expenses);
    rvToday.setAdapter(adapter);
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
    rvToday = view.findViewById(R.id.rvToday);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rvToday.setLayoutManager(llm);
    rvToday.setHasFixedSize(true);

    initializeDataToday();
    initializeAdapterToday();
  }

  private void initViews() {
    tvMonthDay = view.findViewById(R.id.monthDay);
    tvWeekName = view.findViewById(R.id.weekName);
    expenseCount = view.findViewById(R.id.expenseCount);
  }
}
