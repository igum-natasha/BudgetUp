package com.example.budgetup;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class JournalFragment extends Fragment {

  private View view;
  private String[][] days;
  private String[][] expenses;
  private RecyclerView rvToday;
  private RecyclerView rv;
  TextView tvWeekName, tvMonthDay;

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

  private void initBarChart() {
    BarChart barChart = (BarChart) view.findViewById(R.id.barChart);
    ArrayList<BarEntry> expenses = new ArrayList<>();
    expenses.add(new BarEntry(0, 508));
    expenses.add(new BarEntry(1, 306));
    expenses.add(new BarEntry(2, 150));
    expenses.add(new BarEntry(3, 1400));
    expenses.add(new BarEntry(4, 100));
    expenses.add(new BarEntry(5, 350));
    ArrayList<String> xAxisLabel = new ArrayList<>();
    xAxisLabel.add("Food");
    xAxisLabel.add("Clothes");
    xAxisLabel.add("Car");
    xAxisLabel.add("Gifts");
    xAxisLabel.add("House");
    xAxisLabel.add("Transport");

    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

    BarDataSet barDataSet = new BarDataSet(expenses, "Expenses");
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
    barChart.animateY(1500);
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
    adapter.setOnItemClickListener(new RVAdapterCalendar.ClickListener() {
      @Override
      public void onItemClick(int position, View v) {
      }

      @Override
      public void onItemLongClick(int position, View v) {

      }
    });
  }

  private void showCalendar() {
    rv = view.findViewById(R.id.rv);

    GridLayoutManager llm = new GridLayoutManager(view.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeData();
    initializeAdapter();
  }

  private void initializeDataToday() {
    //    AppDatabase db = AppDatabase.build(getApplicationContext());
    //    expenses = db.expenseDao().getAll();
    Resources resources = view.getResources();
    int resourceId =
            resources.getIdentifier("food", "drawable", view.getContext().getPackageName());
    expenses =
            new String[][] {
                    {"Magnit", "-1000 RUB", Integer.toString(resourceId)},
                    {"Sportmaster", "-2800 RUB", Integer.toString(resourceId)},
                    {"Petrol", "-2500 RUB", Integer.toString(resourceId)}
            };
    if (expenses.length == 0) {
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
  }

}
