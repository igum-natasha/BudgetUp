package com.example.budgetup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.loopeer.shadow.ShadowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JournalFragment extends Fragment {

  private View view;
  private List<String[]> days = new ArrayList<>();
  ArrayList<BarEntry> data_expenses = new ArrayList<>();
  private List<Expense> expenses;
  private RecyclerView rvToday;
  private RecyclerView rv;
  int position = 15, expensePos;
  Calendar date = Calendar.getInstance();
  List<Date> dateList = new ArrayList<>();
  TextView tvMonthName, tvMonthDay, expenseCount, tvNoInfo;
  Dialog expenseInfoDialog;
  ShadowView shadowView;
  BarChart barChart;
  String category;
  List<String> xAxisLabel = new ArrayList<>();
  float[] sumByCategory = {0, 0, 0, 0, 0, 0};
  float maxSum = 0, sum = 0;
  BarDataSet barDataSet;
  BarData barData;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.fragment_journal, container, false);
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.add(Calendar.DAY_OF_MONTH, (-1) * position);
    for (int i = 1; i <= 30; i++) {
      dateList.add(date.getTime());
      date.add(Calendar.DATE, 1);
    }
    initViews();
    showCalendar();
    showExpenses();
    return view;
  }

  @SuppressLint("SetTextI18n")
  private void initEmptyBarChart() {
    data_expenses.clear();
    sumByCategory = new float[] {0, 0, 0, 0, 0, 0};
    maxSum = 0;
    sum = 0;
    int[] colors = new int[sumByCategory.length];
    TypedArray palette = getResources().obtainTypedArray(R.array.colors);
    for (int i = 0; i < sumByCategory.length; i++) {
      data_expenses.add(new BarEntry(i, sumByCategory[i]));
      colors[i] = palette.getColor(i, 0);
    }
    String exCount = sum + " RUB";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDay = new SimpleDateFormat("dd");
    SimpleDateFormat formatMonth = new SimpleDateFormat("MMM");

    String currentDay = formatDay.format(dateList.get(position));
    String currentMonth = formatMonth.format(dateList.get(position));
    defaultBarSettings(colors, exCount, currentDay, currentMonth);
  }

  private void defaultBarSettings(
          int[] colors, String exCount, String currentDay, String currentMonth) {
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
    tvMonthDay.setText(currentDay);
    tvMonthName.setText(currentMonth);
  }

  @SuppressLint("SetTextI18n")
  private void initBarChart() {
    data_expenses.clear();
    maxSum = sum = 0;
    sumByCategory = new float[] {0, 0, 0, 0, 0, 0};
    int[] colors = new int[expenses.size()];
    TypedArray palette = getResources().obtainTypedArray(R.array.colors);
    for (int i = 0; i < expenses.size(); i++) {
      category = expenses.get(i).getCategory();
      if (xAxisLabel.size() < 6 && !xAxisLabel.contains(category)) {
        xAxisLabel.add(category);
      }
      if (xAxisLabel.contains(category)) {
        sumByCategory[xAxisLabel.indexOf(category)] +=
            Float.parseFloat(expenses.get(i).getValue()); //.replace('-', ' '));
      }
      colors[i] = palette.getColor(i, 0);
    }
    if (xAxisLabel.size() < 6) {
      xAxisLabel.addAll(Arrays.asList(getResources().getStringArray(R.array.template)));
      xAxisLabel = xAxisLabel.subList(0, 6);
    }
    for (int i = 0; i < sumByCategory.length; i++) {
      data_expenses.add(new BarEntry(i, sumByCategory[i]));
      sum += sumByCategory[i];
      if (sumByCategory[i] > maxSum) {
        maxSum = sumByCategory[i];
      }
    }
    String exCount = sum + " " + expenses.get(0).getCurrency();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDay = new SimpleDateFormat("dd");
    SimpleDateFormat formatMonth = new SimpleDateFormat("MMM");

    String currentDay = formatDay.format(dateList.get(position));
    String currentMonth = formatMonth.format(dateList.get(position));
    defaultBarSettings(colors, exCount, currentDay, currentMonth);
  }

  private void initializeData() {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDay = new SimpleDateFormat("dd");
    SimpleDateFormat formatWeek = new SimpleDateFormat("EEE");
    for (int i = 0; i < dateList.size(); i++) {
      days.add(
          new String[] {formatWeek.format(dateList.get(i)), formatDay.format(dateList.get(i))});
    }
  }

  private void initializeAdapter() {
    RVAdapterCalendar adapter = new RVAdapterCalendar(days);
    rv.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterCalendar.ClickListener() {
          @Override
          public void onItemClick(int pos, View v) {
            position = pos;
            if (position < dateList.size() - 1) {
              showExpenses();
            }
          }

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
    rv.scrollToPosition(position);
  }

  private void initializeDataToday() {
    AppDatabase db = AppDatabase.build(view.getContext());
    long today = dateList.get(position).getTime();
    long tomr = dateList.get(position + 1).getTime();
    expenses = db.expenseDao().getByDate(today, tomr);
    if (expenses.isEmpty()) {
      initEmptyBarChart();
      tvNoInfo.setVisibility(View.VISIBLE);
      shadowView.setVisibility(View.INVISIBLE);
      //      deleteDialog.show();
      // TODO: dialog
    } else {
      initBarChart();
      tvNoInfo.setVisibility(View.INVISIBLE);
      shadowView.setVisibility(View.VISIBLE);
    }
  }

  private void initializeAdapterToday() {
    RVAdapterToday adapter = new RVAdapterToday(expenses);
    rvToday.setAdapter(adapter);
    adapter.setOnItemClickListener(
        new RVAdapterToday.ClickListener() {
          @Override
          public void onItemClick(int position, View v) {
            expensePos = position;
            defineInfoDialog();
            expenseInfoDialog.show();
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
    tvMonthName = view.findViewById(R.id.monthName);
    expenseCount = view.findViewById(R.id.expenseCount);
    shadowView = view.findViewById(R.id.shadowView);
    tvNoInfo = view.findViewById(R.id.noInfoTV);
    barChart = (BarChart) view.findViewById(R.id.barChart);

    tvNoInfo.setVisibility(View.INVISIBLE);
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineInfoDialog() {
    expenseInfoDialog = new Dialog(JournalFragment.this.getContext());
    expenseInfoDialog.setContentView(R.layout.expense_dialog);
    expenseInfoDialog
        .getWindow()
        .setBackgroundDrawable(
            JournalFragment.this.getContext().getDrawable(R.drawable.background_dialog));
    expenseInfoDialog
        .getWindow()
        .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    expenseInfoDialog.setCancelable(false);
    ImageView image = expenseInfoDialog.findViewById(R.id.expenseIcon);
    TextView date = expenseInfoDialog.findViewById(R.id.dateTitle);
    TextView expName = expenseInfoDialog.findViewById(R.id.expenseName);
    TextView expCateg = expenseInfoDialog.findViewById(R.id.expCateg);
    TextView expCount = expenseInfoDialog.findViewById(R.id.expCount);
    ImageButton close = expenseInfoDialog.findViewById(R.id.closeIcon);
    ImageButton delete = expenseInfoDialog.findViewById(R.id.deleteIcon);
    Expense currentExpense = expenses.get(expensePos);
    image.setImageResource(currentExpense.getImage());
    Date d = new Date(currentExpense.getDate());
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss");
    date.setText(df.format(d));
    expName.setText(currentExpense.getNote());
    expCateg.setText(currentExpense.getCategory());
    expCount.setText(currentExpense.getValue() + " " + currentExpense.getCurrency());
    close.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            expenseInfoDialog.dismiss();
          }
        });
    delete.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            AppDatabase db = AppDatabase.build(getContext());
            db.expenseDao().deleteById(currentExpense.getId());
            expenseInfoDialog.dismiss();
            //                Intent intent =
            //                        new Intent(
            //                                HomeActivity.this, HomeActivity.class);
            //                startActivity(intent);
          }
        });
  }
}
