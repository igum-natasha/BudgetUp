package com.example.budgetup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;

public class JournalFragment extends Fragment {

  private View view;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_journal, container, false);
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
    barChart.getAxisLeft().setDrawGridLines(false);
    barChart.getAxisRight().setEnabled(false);
    barChart.setFitBars(true);
    barChart.setData(barData);
    barChart.getDescription().setEnabled(false);
    barChart.animateY(1500);
  }
}
