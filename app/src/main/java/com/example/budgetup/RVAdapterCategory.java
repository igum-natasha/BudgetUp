package com.example.budgetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RVAdapterCategory extends RecyclerView.Adapter<RVAdapterCategory.CategoryViewHolder> {
  private static ClickListener clickListener;

  public static class CategoryViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    TextView categoryName, minCount, maxCount, expenseCount;
    BarChart barChart;
    String type;

    CategoryViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      categoryName = itemView.findViewById(R.id.categTitle);
      barChart = (BarChart) itemView.findViewById(R.id.barChart);
      minCount = itemView.findViewById(R.id.minCount);
      maxCount = itemView.findViewById(R.id.maxCount);
      expenseCount = itemView.findViewById(R.id.expenseCount);
      type = "week";
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
  String type;
  List<String> categList;

  RVAdapterCategory(List<Expense> expenses, String type) {
    this.expenses = expenses;
    this.type = type;
    this.categList = new ArrayList<>();
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
    ArrayList<String> xAxisLabel;
    switch (type) {
      case "week":
        xAxisLabel =
            new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        break;
      case "month":
        xAxisLabel =
            new ArrayList<>(
                Arrays.asList(
                    "1", "4", "6", "9", "11", "13", "16", "19", "21", "23", "26", "29", "31"));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
    int[] colors = {
      ViewHolder.itemView.getResources().getColor(R.color.primary_200),
      ViewHolder.itemView.getResources().getColor(R.color.base_500),
      ViewHolder.itemView.getResources().getColor(R.color.menu_1),
      ViewHolder.itemView.getResources().getColor(R.color.menu_2),
      ViewHolder.itemView.getResources().getColor(R.color.menu_3),
      ViewHolder.itemView.getResources().getColor(R.color.menu_4),
      ViewHolder.itemView.getResources().getColor(R.color.menu_5),
      ViewHolder.itemView.getResources().getColor(R.color.menu_6),
      ViewHolder.itemView.getResources().getColor(R.color.error_100)
    };
    if (!categList.contains(expenses.get(i).getCategory())) {
      categList.add(expenses.get(i).getCategory());
      ViewHolder.categoryName.setText("Category: " + expenses.get(i).getCategory());
      List<Expense> newExpenses = new ArrayList<>();
      for (Expense ex : expenses) {
        if (ex.getCategory().equals(expenses.get(i).getCategory())) {
          newExpenses.add(ex);
        }
      }
      StatisticsFragment.initBarChart(
          ViewHolder.barChart,
          newExpenses,
          xAxisLabel,
          type,
          colors,
          ViewHolder.maxCount,
          ViewHolder.minCount,
          ViewHolder.expenseCount);
    }
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
}
