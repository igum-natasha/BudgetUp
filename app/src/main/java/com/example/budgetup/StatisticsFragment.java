package com.example.budgetup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

  private View view;

  private String[][] days;
  private RecyclerView rv;
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_statistics, container, false);
    initBarChart();
    showCategories();
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
    expenses.add(new BarEntry(6, 150));
    ArrayList<String> xAxisLabel = new ArrayList<>();
    xAxisLabel.add("Mon");
    xAxisLabel.add("Tue");
    xAxisLabel.add("Wed");
    xAxisLabel.add("Thu");
    xAxisLabel.add("Fri");
    xAxisLabel.add("Sat");
    xAxisLabel.add("Sun");

    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

    BarDataSet barDataSet = new BarDataSet(expenses, "Expenses");
    int[] colors = {
            getResources().getColor(R.color.menu_1),
            getResources().getColor(R.color.menu_2),
            getResources().getColor(R.color.menu_3),
            getResources().getColor(R.color.menu_4),
            getResources().getColor(R.color.menu_5),
            getResources().getColor(R.color.menu_6),
            getResources().getColor(R.color.error_100)
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
                    {"Food"}, {"Car"}, {"Gifts"}
            };
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initializeAdapter() {
    RVAdapterCategory adapter = new RVAdapterCategory(days);
    rv.setAdapter(adapter);
    adapter.setOnItemClickListener(new RVAdapterCategory.ClickListener() {
      @Override
      public void onItemClick(int position, View v) {

      }

      @Override
      public void onItemLongClick(int position, View v) {

      }
    });
//    BottomNavigationView nav_view = StatisticsActivity.getNavigationview();
//    rv.setOnTouchListener(new TranslateAnimUtil(this.getContext(), nav_view));
  }

  private void showCategories() {
    rv = view.findViewById(R.id.rvCateg);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeData();
    initializeAdapter();
  }
}
