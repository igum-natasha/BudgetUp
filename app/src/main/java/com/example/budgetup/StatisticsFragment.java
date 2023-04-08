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
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {

  private View view;
  private List<Expense> expenses;
  private List<Date[]> days = new ArrayList<>();
  private List<Date[]> daysMonth = new ArrayList<>();

  ArrayList<BarEntry> data_expenses = new ArrayList<>();
  private RecyclerView rv;
  private RecyclerView rvDays;
  private ImageButton btnLeft, btnRight;
  int weekPos = 3, monthPos = 3;
  BarChart barChart;
  TextView tvMax, tvMin, expenseCount, tvNoInfo;
  String day;
  ArrayList<String> xAxisLabel =
      new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
  float[] sumByDay = {0, 0, 0, 0, 0, 0, 0};
  Calendar date;
  List<Date> dateListWeek = new ArrayList<>();
  List<Date> dateListMonth = new ArrayList<>();
  float maxSum = 0, sum = 0;
  float minSum = Float.POSITIVE_INFINITY;
  BarDataSet barDataSet;
  BarData barData;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_statistics, container, false);
    initViews();
    MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.toggleGroup);
    //    toggleGroup.check(R.id.button1);
    toggleGroup.addOnButtonCheckedListener(
        new MaterialButtonToggleGroup.OnButtonCheckedListener() {
          @Override
          public void onButtonChecked(
              MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
            switch (checkedId) {
              case R.id.button1:
                // FIXME: check week button automatically
                //                Toast.makeText(
                //                        view.getContext(),
                //                        getResources().getString(R.string.weekTitle),
                //                        Toast.LENGTH_LONG)
                //                    .show();
                date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                xAxisLabel =
                    new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
                showDaysWeek();
                break;
              case R.id.button2:
                //                Toast.makeText(
                //                        view.getContext(),
                //                        getResources().getString(R.string.monthTitle),
                //                        Toast.LENGTH_LONG)
                //                    .show();
                date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                xAxisLabel =
                    new ArrayList<>(
                        Arrays.asList(
                            "1", "4", "6", "9", "11", "13", "16", "19", "21", "23", "26", "29",
                            "31"));
                sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                showDaysMonth();
                break;
            }
          }
        });
    return view;
  }

  @SuppressLint("SetTextI18n")
  private void initEmptyBarChart(String type) {
    data_expenses.clear();
    maxSum = 0;
    sum = 0;
    minSum = 0;
    if (type.equals("week")) {
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0};
    } else {
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    for (int i = 0; i < sumByDay.length; i++) {
      data_expenses.add(new BarEntry(i, sumByDay[i]));
    }
    int[] colors = {
      getResources().getColor(R.color.menu_1),
      getResources().getColor(R.color.menu_2),
      getResources().getColor(R.color.menu_3),
      getResources().getColor(R.color.menu_4),
      getResources().getColor(R.color.menu_5),
      getResources().getColor(R.color.menu_6),
      getResources().getColor(R.color.error_100)
    };
    String exCount = sum + " " + "RUB"; // FIXME: format?
    String minCount = minSum + " " + "RUB";
    String maxCount = maxSum + " " + "RUB";
    defaultBarSettings(colors, exCount, minCount, maxCount);
  }

  private void defaultBarSettings(int[] colors, String exCount, String minCount, String maxCount) {
    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
    barDataSet = new BarDataSet(data_expenses, "Expenses");
    barDataSet.setColors(colors);
    barDataSet.setDrawValues(false);

    barData = new BarData(barDataSet);
    barChart.clear();
    Legend l = barChart.getLegend();
    l.setEnabled(false);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setDrawAxisLine(false);
    xAxis.setGranularity(1f);
    xAxis.setLabelCount(xAxisLabel.size());
    barChart.getAxisLeft().setGridColor(getResources().getColor(R.color.primary_200));
    barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.base_500));
    barChart.getAxisRight().setEnabled(false);
    barChart.setFitBars(true);
    barChart.setData(barData);
    barChart.getDescription().setEnabled(false);
    barChart.animateY((int) maxSum);

    expenseCount.setText(exCount);
    tvMin.setText(minCount);
    tvMax.setText(maxCount);
  }

  @SuppressLint("SetTextI18n")
  private void initBarChart(String type) {
    data_expenses.clear();
    maxSum = sum = 0;
    minSum = Float.POSITIVE_INFINITY;
    SimpleDateFormat df;
    if (type.equals("week")) {
      df = new SimpleDateFormat("EEE");
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0};
    } else {
      df = new SimpleDateFormat("d");
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    for (int i = 0; i < expenses.size(); i++) {
      Date d = new Date(expenses.get(i).getDate());
      day = df.format(d);
      if (xAxisLabel.contains(day)) {
        sumByDay[xAxisLabel.indexOf(day)] +=
            Float.parseFloat(expenses.get(i).getValue().replace('-', ' '));
      }
    }
    for (int i = 0; i < sumByDay.length; i++) {
      data_expenses.add(new BarEntry(i, sumByDay[i]));
      sum += sumByDay[i];
      if (sumByDay[i] > maxSum) {
        maxSum = sumByDay[i];
      }
      if (sumByDay[i] < minSum && sumByDay[i] > 0) {
        minSum = sumByDay[i];
      }
    }
    int[] colors = {
      getResources().getColor(R.color.menu_1),
      getResources().getColor(R.color.menu_2),
      getResources().getColor(R.color.menu_3),
      getResources().getColor(R.color.menu_4),
      getResources().getColor(R.color.menu_5),
      getResources().getColor(R.color.menu_6),
      getResources().getColor(R.color.error_100)
    };
    String exCount = sum + " " + expenses.get(0).getCurrency();
    String minCount = minSum + expenses.get(0).getCurrency();
    String maxCount = maxSum + expenses.get(0).getCurrency();
    defaultBarSettings(colors, exCount, minCount, maxCount);
  }

  private void initViews() {
    btnLeft = view.findViewById(R.id.left);
    btnRight = view.findViewById(R.id.right);
    barChart = (BarChart) view.findViewById(R.id.barChart);

    expenseCount = view.findViewById(R.id.expenseCount);
    tvNoInfo = view.findViewById(R.id.noInfoTV);
    tvMax = view.findViewById(R.id.maxCount);
    tvMin = view.findViewById(R.id.minCount);
  }

  private void initializeDataWeek() {
    AppDatabase db = AppDatabase.build(view.getContext());
    Date[] week = days.get(weekPos);
    expenses = db.expenseDao().getByDate(week[0].getTime(), week[1].getTime());

    if (expenses.isEmpty()) {
      initEmptyBarChart("week");
      //      tvNoInfo.setVisibility(View.VISIBLE);
      //      deleteDialog.show();
      // TODO: dialog
    } else {
      initBarChart("week");
      //      tvNoInfo.setVisibility(View.INVISIBLE);
    }
  }

  private void initializeDataMonth() {
    AppDatabase db = AppDatabase.build(view.getContext());
    Date[] month = daysMonth.get(monthPos);
    expenses = db.expenseDao().getByDate(month[0].getTime(), month[1].getTime());

    if (expenses.isEmpty()) {
      initEmptyBarChart("month");
      //      tvNoInfo.setVisibility(View.VISIBLE);
      //      deleteDialog.show();
      // TODO: dialog
    } else {
      initBarChart("month");
      //      tvNoInfo.setVisibility(View.INVISIBLE);
    }
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

  private void showCategoriesWeek() {
    rv = view.findViewById(R.id.rvCateg);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeDataWeek();
    initializeAdapter();
  }

  private void showCategoriesMonth() {
    rv = view.findViewById(R.id.rvCateg);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeDataMonth();
    initializeAdapter();
  }

  private void initializeDataDaysMonth() {
    date.set(Calendar.DAY_OF_MONTH, (-1) * 100);
    for (int i = 1; i <= 200; i++) {
      dateListMonth.add(date.getTime());
      date.add(Calendar.DATE, 1);
    }
    SimpleDateFormat formatNumDayWeek = new SimpleDateFormat("d");
    for (int i = 0; i < dateListMonth.size(); i++) {
      String checkDate = formatNumDayWeek.format(dateListMonth.get(i));
      if (checkDate.equals("1")) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateListMonth.get(i));
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        int countDays = Integer.parseInt(formatNumDayWeek.format(cal.getTime()));
        if (i + countDays > dateListMonth.size()) {
          countDays = dateListMonth.size() - i - 1;
        }
        daysMonth.add(new Date[] {dateListMonth.get(i), dateListMonth.get(i + countDays)});
        i += countDays - 1;
      }
    }
  }

  private void initializeDataDaysWeek() {
    date.add(Calendar.DAY_OF_MONTH, (-1) * 30);
    for (int i = 1; i <= 60; i++) {
      dateListWeek.add(date.getTime());
      date.add(Calendar.DATE, 1);
    }
    SimpleDateFormat formatNumDayWeek = new SimpleDateFormat("u");
    for (int i = 0; i < dateListWeek.size() - 6; i++) {
      if (formatNumDayWeek.format(dateListWeek.get(i)).equals("1")) {
        days.add(new Date[] {dateListWeek.get(i), dateListWeek.get(i + 6)});
        i += 6;
      }
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initializeAdapterDays(List<Date[]> daysList, String type) {
    RVAdapterDays adapter = new RVAdapterDays(daysList, type);
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

  private void showDaysWeek() {
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
            if (weekPos > 0) {
              llm.scrollToPositionWithOffset(weekPos, 0);
            }
            showCategoriesWeek();
          }
        });
    btnRight.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            weekPos = llm.findFirstVisibleItemPosition() + 1;
            Toast.makeText(view.getContext(), weekPos + " " + days.size(), Toast.LENGTH_LONG)
                .show();
            if (weekPos < days.size() - 1) {
              llm.scrollToPositionWithOffset(weekPos, 0);
            }
            showCategoriesWeek();
          }
        });
    if (days.isEmpty()) {
      initializeDataDaysWeek();
    }
    initializeAdapterDays(days, "week");
    rvDays.scrollToPosition(weekPos);
  }

  private void showDaysMonth() {
    rvDays = view.findViewById(R.id.rvDays);
    Toast.makeText(
            view.getContext(), getResources().getString(R.string.monthTitle), Toast.LENGTH_LONG)
        .show();
    LinearLayoutManager llm =
        new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
    rvDays.setLayoutManager(llm);
    rvDays.setHasFixedSize(true);
    btnLeft.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            monthPos = llm.findFirstVisibleItemPosition() - 1;
            if (monthPos > 0) {
              llm.scrollToPositionWithOffset(monthPos, 0);
            }
            showCategoriesMonth();
          }
        });
    btnRight.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            monthPos = llm.findFirstVisibleItemPosition() + 1;
            Toast.makeText(view.getContext(), monthPos + " " + daysMonth.size(), Toast.LENGTH_LONG)
                .show();
            if (monthPos < daysMonth.size() - 1) {
              llm.scrollToPositionWithOffset(monthPos, 0);
            }
            showCategoriesMonth();
          }
        });
    if (daysMonth.isEmpty()) {
      initializeDataDaysMonth();
    }
    initializeAdapterDays(daysMonth, "month");
    rvDays.scrollToPosition(monthPos);
  }
}
