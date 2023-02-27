package com.example.budgetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterCategory extends RecyclerView.Adapter<RVAdapterCategory.CategoryViewHolder> {
  private static ClickListener clickListener;

  public static class CategoryViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    TextView categoryName;
    BarChart barChart;

    CategoryViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      categoryName = itemView.findViewById(R.id.categTitle);
      barChart = (BarChart) itemView.findViewById(R.id.barChart);
    }

    @Override
    public void onClick(View v) {
      clickListener.onItemClick(getAdapterPosition(), v);
    }

    @Override
    public boolean onLongClick(View v) {
      clickListener.onItemLongClick(getAdapterPosition(), v);
      return false;
    }
  }

  List<Expense> expenses;

  RVAdapterCategory(List<Expense> expenses) {
    this.expenses = expenses;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public RVAdapterCategory.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.category_item, viewGroup, false);
    return new CategoryViewHolder(v);
  }

  @Override
  public void onBindViewHolder(RVAdapterCategory.CategoryViewHolder ViewHolder, int i) {
    //        int img = expenses.get(i).getImage();
    //        deviceViewHolder.expenseName.setText(expenses.get(i).getExpenseName());
    //        deviceViewHolder.expenseCost.setText(expenses.get(i).getExpenseCost());
    //        deviceViewHolder.expensePhoto.setBackgroundResource(img);
    ViewHolder.categoryName.setText("Category: " + expenses.get(i).getCategory());
    initBarChart(ViewHolder.itemView, ViewHolder.barChart);
  }

  @Override
  public int getItemCount() {
    return expenses.size();
  }

  public void setOnItemClickListener(RVAdapterCategory.ClickListener clickListener) {
    RVAdapterCategory.clickListener = clickListener;
  }

  public interface ClickListener {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
  }

  public void initBarChart(View view, BarChart barChart) {
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
      view.getResources().getColor(R.color.menu_1),
      view.getResources().getColor(R.color.menu_2),
      view.getResources().getColor(R.color.menu_3),
      view.getResources().getColor(R.color.menu_4),
      view.getResources().getColor(R.color.menu_5),
      view.getResources().getColor(R.color.menu_6),
      view.getResources().getColor(R.color.error_100)
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
    barChart.getAxisLeft().setGridColor(view.getResources().getColor(R.color.primary_200));
    barChart.getAxisLeft().setTextColor(view.getResources().getColor(R.color.base_500));
    //    barChart.getAxisLeft().setDrawGridLines(false);
    barChart.getAxisRight().setEnabled(false);
    barChart.setFitBars(true);
    barChart.setData(barData);
    barChart.getDescription().setEnabled(false);
    barChart.animateY(1500);
  }
}
