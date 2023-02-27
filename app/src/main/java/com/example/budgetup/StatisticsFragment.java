package com.example.budgetup;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {

  private View view;
  private List<Expense> expenses;
  private List<Date[]> days = new ArrayList<>();
  private RecyclerView rv;
  private RecyclerView rvDays;
  private ImageButton btnLeft, btnRight;
  int weekPos = 4;
  Calendar date = Calendar.getInstance();
  List<Date> dateList = new ArrayList<>();

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_statistics, container, false);
    initViews();
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.add(Calendar.DAY_OF_MONTH, (-1) * 30);
    for (int i = 1; i <= 60; i++) {
      dateList.add(date.getTime());
      date.add(Calendar.DATE, 1);
    }
    showDays();
    initBarChart();
    showCategories();
    MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.toggleGroup);
    toggleGroup.check(R.id.button1);
    toggleGroup.addOnButtonCheckedListener(
        new MaterialButtonToggleGroup.OnButtonCheckedListener() {
          @Override
          public void onButtonChecked(
              MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
            switch (checkedId) {
              case R.id.button1:
                //                showDays();
                Toast.makeText(
                        view.getContext(),
                        getResources().getString(R.string.weekTitle),
                        Toast.LENGTH_LONG)
                    .show();
                break;
              case R.id.button2:
                Toast.makeText(
                        view.getContext(),
                        getResources().getString(R.string.monthTitle),
                        Toast.LENGTH_LONG)
                    .show();
                break;
            }
          }
        });
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

  private void initViews() {
    btnLeft = view.findViewById(R.id.left);
    btnRight = view.findViewById(R.id.right);
  }

  private void initializeData() {
    AppDatabase db = AppDatabase.build(view.getContext());
    Date[] week = days.get(weekPos);
    expenses = db.expenseDao().getByDate(week[0].getTime(), week[1].getTime());
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initializeAdapter() {
    RVAdapterCategory adapter = new RVAdapterCategory(expenses);
    rv.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterCategory.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {}

          @Override
          public void onItemLongClick(int position, View v) {}
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

  private void initializeDataDays() {

    SimpleDateFormat formatNumDayWeek = new SimpleDateFormat("u");
    for (int i = 0; i < dateList.size() - 6; i++) {
      if (formatNumDayWeek.format(dateList.get(i)).equals("1")) {
        days.add(new Date[] {dateList.get(i), dateList.get(i + 6)});
      }
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initializeAdapterDays() {
    RVAdapterDays adapter = new RVAdapterDays(days);
    rvDays.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterDays.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {}

          @Override
          public void onItemLongClick(int position, View v) {}
        });
    //    BottomNavigationView nav_view = StatisticsActivity.getNavigationview();
    //    rv.setOnTouchListener(new TranslateAnimUtil(this.getContext(), nav_view));
  }

  private void showDays() {
    rvDays = view.findViewById(R.id.rvDays);

    LinearLayoutManager llm =
        new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
    rvDays.setLayoutManager(llm);
    rvDays.setHasFixedSize(true);
    btnLeft.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            weekPos = llm.findFirstVisibleItemPosition() - 1;
            llm.scrollToPositionWithOffset(weekPos, 0);
            showCategories();
          }
        });
    btnRight.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            weekPos = llm.findFirstVisibleItemPosition() + 1;
            llm.scrollToPositionWithOffset(weekPos, 0);
            showCategories();
          }
        });

    initializeDataDays();
    initializeAdapterDays();
    rvDays.scrollToPosition(weekPos);
  }
}
