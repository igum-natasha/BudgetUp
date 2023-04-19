package com.example.budgetup;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

  private View view;
  private List<Expense> expenses;
  private List<Date[]> days = new ArrayList<>();
  private List<Date[]> daysMonth = new ArrayList<>();
  private RecyclerView rv;
  private RecyclerView rvDays;
  private ImageButton btnLeft, btnRight;
  int weekPos = 3, monthPos = 3;
  BarChart barChart;
  User user;
  TextView tvMax, tvMin, expenseCount, tvNoInfo;
  ArrayList<String> xAxisLabel = new ArrayList<>();
  float[] sumByDay = {0, 0, 0, 0, 0, 0, 0};
  Calendar date;
  List<Date> dateListWeek = new ArrayList<>();
  List<Date> dateListMonth = new ArrayList<>();
  float maxSum = 0, sum = 0;
  float minSum = Float.POSITIVE_INFINITY;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_statistics, container, false);
    initViews();

    MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.toggleGroup);
    toggleGroup.addOnButtonCheckedListener(
        new MaterialButtonToggleGroup.OnButtonCheckedListener() {
          @Override
          public void onButtonChecked(
              MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
            switch (checkedId) {
              case R.id.button1:
                date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                xAxisLabel =
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.week)));
                showDaysWeek();
                break;
              case R.id.button2:
                date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                xAxisLabel =
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.month)));
                sumByDay =
                    new float[] {
                      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                      0, 0, 0, 0, 0
                    };
                showDaysMonth();
                break;
            }
          }
        });
    toggleGroup.check(R.id.button1);
    return view;
  }

  @SuppressLint("SetTextI18n")
  private void initEmptyBarChart(String type) {
    ArrayList<BarEntry> data_expenses = new ArrayList<>();
    maxSum = 0;
    sum = 0;
    minSum = 0;
    int[] colors = new int[sumByDay.length];
    TypedArray palette = getResources().obtainTypedArray(R.array.colors);
    TypedArray baseColors = getResources().obtainTypedArray(R.array.base_colors);
    if (type.equals("week")) {
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0};
    } else {
      sumByDay =
          new float[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0
          };
    }
    for (int i = 0; i < sumByDay.length; i++) {
      data_expenses.add(new BarEntry(i, sumByDay[i]));
      colors[i] = palette.getColor(i, 0);
    }
    String exCount = sum + " " + "RUB"; // FIXME: format?
    String minCount = minSum + " " + "RUB";
    String maxCount = maxSum + " " + "RUB";
    expenseCount.setText(exCount);
    tvMin.setText(minCount);
    tvMax.setText(maxCount);
    defaultBarSettings(barChart, data_expenses, xAxisLabel, maxSum, colors, baseColors);
  }

  @SuppressLint("ResourceType")
  private static void defaultBarSettings(
      BarChart barChart,
      ArrayList<BarEntry> dataExpenses,
      ArrayList<String> xAxisLabel,
      float maxSum,
      int[] colors,
      TypedArray baseColors) {
    BarDataSet barDataSet;
    BarData barData;
    XAxis xAxis = barChart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
    barDataSet =
        new BarDataSet(
            dataExpenses, barChart.getRootView().getContext().getString(R.string.expense));
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
    xAxis.setLabelRotationAngle(-90);
    barChart.getAxisLeft().setGridColor(baseColors.getColor(0, 0));
    barChart.getAxisLeft().setTextColor(baseColors.getColor(1, 0));
    barChart.getAxisRight().setEnabled(false);
    barChart.setFitBars(true);
    barChart.setData(barData);
    barChart.getDescription().setEnabled(false);
    barChart.animateY((int) maxSum);
  }

  @SuppressLint("SetTextI18n")
  public static void initBarChart(
      BarChart barChart,
      List<Expense> expensesList,
      ArrayList<String> xAxisLabel,
      String type,
      TypedArray palette,
      TypedArray baseColors,
      TextView tvMax,
      TextView tvMin,
      TextView expenseCount) {
    ArrayList<BarEntry> dataExpenses = new ArrayList<>();
    float maxSum = 0, sum = 0;
    float minSum = Float.POSITIVE_INFINITY;
    SimpleDateFormat df;
    float[] sumByDay;
    int[] colors = new int[expensesList.size()];
    if (type.equals("week")) {
      df = new SimpleDateFormat("EEE");
      sumByDay = new float[] {0, 0, 0, 0, 0, 0, 0};
    } else {
      df = new SimpleDateFormat("d");
      sumByDay =
          new float[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0
          };
    }
    for (int i = 0; i < expensesList.size(); i++) {
      Date d = new Date(expensesList.get(i).getDate());
      String day = df.format(d);
      if (xAxisLabel.contains(day)) {
        Log.d("value_before", expensesList.get(i).getValue() + "");
        float value = Float.parseFloat(expensesList.get(i).getValue()); // .replace('-', ' '));
        Log.d("value", value + "");
        sumByDay[xAxisLabel.indexOf(day)] += value;
      }
      colors[i] = palette.getColor(i, 0);
    }
    for (int i = 0; i < sumByDay.length; i++) {
      dataExpenses.add(new BarEntry(i, sumByDay[i]));
      sum += sumByDay[i];
      if (sumByDay[i] > maxSum) {
        maxSum = sumByDay[i];
      }
      if (sumByDay[i] < minSum && sumByDay[i] < maxSum) {
        minSum = sumByDay[i];
      }
    }

    String exCount = sum + " " + expensesList.get(0).getCurrency();
    String minCount = minSum + expensesList.get(0).getCurrency();
    String maxCount = maxSum + expensesList.get(0).getCurrency();
    expenseCount.setText(exCount);
    tvMin.setText(minCount);
    tvMax.setText(maxCount);
    defaultBarSettings(barChart, dataExpenses, xAxisLabel, maxSum, colors, baseColors);
  }

  private void initViews() {
    btnLeft = view.findViewById(R.id.left);
    btnRight = view.findViewById(R.id.right);
    barChart = (BarChart) view.findViewById(R.id.barChart);

    expenseCount = view.findViewById(R.id.expenseCount);
    tvNoInfo = view.findViewById(R.id.noInfoTV);
    tvMax = view.findViewById(R.id.maxCount);
    tvMin = view.findViewById(R.id.minCount);
    AppDatabase db = AppDatabase.build(view.getContext());
    user = db.userDao().getByStatus("online");
  }

  private void initializeDataWeek() {
    AppDatabase db = AppDatabase.build(view.getContext());
    Date[] week = days.get(weekPos);
    expenses = db.expenseDao().getByDate(user.getEmail(), week[0].getTime(), week[1].getTime());

    if (expenses.isEmpty()) {
      initEmptyBarChart("week");
      tvNoInfo.setVisibility(View.VISIBLE);
      // TODO: dialog
    } else {
      TypedArray colors = getResources().obtainTypedArray(R.array.colors);
      TypedArray baseColors = getResources().obtainTypedArray(R.array.base_colors);
      initBarChart(
          barChart, expenses, xAxisLabel, "week", colors, baseColors, tvMax, tvMin, expenseCount);
      tvNoInfo.setVisibility(View.INVISIBLE);
    }
  }

  private void initializeDataMonth() {
    AppDatabase db = AppDatabase.build(view.getContext());
    Date[] month = daysMonth.get(monthPos);
    expenses = db.expenseDao().getByDate(user.getEmail(), month[0].getTime(), month[1].getTime());

    if (expenses.isEmpty()) {
      initEmptyBarChart("month");
      tvNoInfo.setVisibility(View.VISIBLE);
      // TODO: dialog
    } else {
      TypedArray colors = getResources().obtainTypedArray(R.array.colors);
      TypedArray baseColors = getResources().obtainTypedArray(R.array.base_colors);
      initBarChart(
          barChart, expenses, xAxisLabel, "month", colors, baseColors, tvMax, tvMin, expenseCount);
      tvNoInfo.setVisibility(View.INVISIBLE);
    }
  }

  private Map<Integer, List<Expense>> createMap(List<Expense> expenseList) {
    Map<Integer, List<Expense>> result = new HashMap<>();
    List<String> categList = new ArrayList<>();
    List<Expense> list;
    for (Expense exp : expenseList) {
      String category = exp.getCategory();
      if (!categList.contains(category)) {
        categList.add(category);
        if (result.containsKey(categList.indexOf(category))) {
          list = result.get(categList.indexOf(category));
        } else {
          list = new ArrayList<>();
        }
        list.add(exp);
        result.put(categList.indexOf(category), list);
      }
    }
    return result;
  }

  @SuppressLint("ClickableViewAccessibility")
  private void initializeAdapter(String type) {
    Map<Integer, List<Expense>> new_expenses = createMap(expenses);
    RVAdapterCategory adapter = new RVAdapterCategory(new_expenses, type);
    rv.setAdapter(adapter);
    ViewGroup.LayoutParams params = rv.getLayoutParams();
    params.height = new_expenses.size() * 1300;
    rv.setLayoutParams(params);
    adapter.setOnItemClickListener(
        new RVAdapterCategory.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {}

          @Override
          public void onItemLongClick(int position, View v) {}
        });
  }

  private void showCategoriesWeek() {
    rv = view.findViewById(R.id.rvCateg);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeDataWeek();
    initializeAdapter("week");
  }

  private void showCategoriesMonth() {
    rv = view.findViewById(R.id.rvCateg);

    LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
    rv.setLayoutManager(llm);
    rv.setHasFixedSize(true);

    initializeDataMonth();
    initializeAdapter("month");
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
            //            Toast.makeText(view.getContext(), weekPos + " " + days.size(),
            // Toast.LENGTH_LONG)
            //                .show();
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
    //    Toast.makeText(
    //            view.getContext(), getResources().getString(R.string.monthTitle),
    // Toast.LENGTH_LONG)
    //        .show();
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
