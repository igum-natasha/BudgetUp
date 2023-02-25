package com.example.budgetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

  private List<Expense> expenses;
  private List<String[]> days = new ArrayList<>();
  private RecyclerView rv, rvDays;
  int expensePos;
  int dayPos = 30;
  TextView tvInfoExp, tvToday, tvNoInfo;
  Dialog addDialog, expenseInfoDialog;
  Button btnTrack;
  Calendar date = Calendar.getInstance();
  List<Date> dateList = new ArrayList<>();
  ImageButton btnAdd, btnLeft, btnRight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initViews();
    defineAddDialog();
    date.set(Calendar.HOUR_OF_DAY, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.add(Calendar.DAY_OF_MONTH, (-1) * 30);
    for (int i = 1; i <= 32; i++) {
      dateList.add(date.getTime());
      date.add(Calendar.DATE, 1);
    }
    showExpenses();
    showDays();

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

  private void initializeDataDays() {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMMM dd");
    SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    for (int i = 0; i < dateList.size(); i++) {
      days.add(
          new String[] {formatDay.format(dateList.get(i)), formatYear.format(dateList.get(i))});
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
    rvDays = findViewById(R.id.rvDays);

    LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    rvDays.setLayoutManager(llm);
    rvDays.setHasFixedSize(true);
    btnLeft.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            dayPos = llm.findFirstVisibleItemPosition() - 1;
            llm.scrollToPositionWithOffset(dayPos, 0);
            showExpenses();
          }
        });
    btnRight.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            dayPos = llm.findFirstVisibleItemPosition() + 1;
            llm.scrollToPositionWithOffset(dayPos, 0);
            showExpenses();
          }
        });

    initializeDataDays();
    initializeAdapterDays();
    rvDays.scrollToPosition(dayPos);
  }

  private void initializeData() {
    AppDatabase db = AppDatabase.build(getApplicationContext());
    long today = dateList.get(dayPos).getTime();
    long tomr = dateList.get(dayPos + 1).getTime();
    expenses = db.expenseDao().getByDate(today, tomr);
    List<Expense> expensesAll = db.expenseDao().getAll();
    if (expensesAll.isEmpty()) {
      tvToday.setVisibility(View.INVISIBLE);
      tvInfoExp.setVisibility(View.VISIBLE);
      btnTrack.setVisibility(View.VISIBLE);
      initEmptyPieChart();
    } else {
      if (expenses.isEmpty()) {
        tvNoInfo.setVisibility(View.VISIBLE);
        initEmptyPieChart();
      } else {
        tvNoInfo.setVisibility(View.INVISIBLE);
        initPieChart();
      }
    }
  }

  private void initializeAdapter() {
    RVAdapterToday adapter = new RVAdapterToday(expenses);
    rv.setAdapter(adapter);
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
    tvNoInfo = findViewById(R.id.noInfoTV);
    btnAdd = findViewById(R.id.plus_icon);
    btnLeft = findViewById(R.id.left);
    btnRight = findViewById(R.id.right);

    tvNoInfo.setVisibility(View.INVISIBLE);
    tvInfoExp.setVisibility(View.INVISIBLE);
    btnTrack.setVisibility(View.INVISIBLE);
  }

  @SuppressLint("DefaultLocale")
  private void initEmptyPieChart() {
    PieChart pieChart = findViewById(R.id.pieChart);
    ArrayList<PieEntry> data_expenses = new ArrayList<>();
    int[] palette = {getResources().getColor(R.color.base_300)};
    data_expenses.add(new PieEntry(1, ""));
    int[] colors = new int[1];
    colors[0] = palette[0];
    PieDataSet pieDataSet = new PieDataSet(data_expenses, "Expenses");
    pieDataSet.setColors(colors);
    pieDataSet.setDrawValues(false);

    PieData pieData = new PieData(pieDataSet);
    Legend l = pieChart.getLegend();
    l.setEnabled(false);
    pieChart.setData(pieData);
    pieChart.setEntryLabelColor(getResources().getColor(R.color.base_600));
    pieChart.getDescription().setEnabled(false);
    String text = "No info";
    pieChart.setCenterText(text);
    pieChart.setCenterTextSize(18f);
    pieChart.setCenterTextColor(getResources().getColor(R.color.primary_900));
    pieChart.animate();
  }

  @SuppressLint("DefaultLocale")
  private void initPieChart() {
    PieChart pieChart = findViewById(R.id.pieChart);
    ArrayList<PieEntry> data_expenses = new ArrayList<>();
    int[] palette = {
      getResources().getColor(R.color.menu_1),
      getResources().getColor(R.color.menu_2),
      getResources().getColor(R.color.menu_3),
      getResources().getColor(R.color.menu_4),
      getResources().getColor(R.color.menu_5),
      getResources().getColor(R.color.menu_6),
      getResources().getColor(R.color.menu_7)
    };
    int[] colors = new int[expenses.size()];
    float max_income = 0, max_expense = 0;
    String currency = expenses.get(0).getCurrency();
    for (int i = 0; i < expenses.size(); i++) {
      float value = Float.parseFloat(expenses.get(i).getValue());
      data_expenses.add(
          new PieEntry(
              Float.parseFloat(expenses.get(i).getValue().replace('-', ' ')),
              expenses.get(i).getCategory()));
      if (value > 0) {
        max_income += value;
      }
      if (value < 0) {
        max_expense += value;
      }
      colors[i] = palette[i];
    }
    PieDataSet pieDataSet = new PieDataSet(data_expenses, "Expenses");
    pieDataSet.setColors(colors);
    pieDataSet.setDrawValues(false);

    PieData pieData = new PieData(pieDataSet);
    Legend l = pieChart.getLegend();
    l.setEnabled(false);
    pieChart.setData(pieData);
    pieChart.setEntryLabelColor(getResources().getColor(R.color.base_600));
    pieChart.getDescription().setEnabled(false);
    String text = String.format("%s %.2f\n%s %.2f", currency, max_income, currency, max_expense);
    pieChart.setCenterText(text);
    pieChart.setCenterTextSize(18f);
    pieChart.setCenterTextColor(getResources().getColor(R.color.primary_400));
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
                new Intent(
                    HomeActivity.this, NewExpenseActivity.class); // TODO: Add expense activity
            startActivity(intent);
          }
        });
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void defineInfoDialog() {
    expenseInfoDialog = new Dialog(HomeActivity.this);
    expenseInfoDialog.setContentView(R.layout.expense_dialog);
    expenseInfoDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
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
            AppDatabase db = AppDatabase.build(getApplicationContext());
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
