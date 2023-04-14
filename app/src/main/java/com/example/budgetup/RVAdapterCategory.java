package com.example.budgetup;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

  Map<Integer, List<Expense>> expenses;
  String type;

  RVAdapterCategory(Map<Integer, List<Expense>> expenses, String type) {
    this.expenses = expenses;
    this.type = type;
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
    ArrayList<String> xAxisLabel;
    switch (type) {
      case "week":
        xAxisLabel =
            new ArrayList<>(
                Arrays.asList(ViewHolder.itemView.getResources().getStringArray(R.array.week)));
        break;
      case "month":
        xAxisLabel =
            new ArrayList<>(
                Arrays.asList(ViewHolder.itemView.getResources().getStringArray(R.array.month)));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
    TypedArray colors = ViewHolder.itemView.getResources().obtainTypedArray(R.array.colors);
    TypedArray baseColors =
        ViewHolder.itemView.getResources().obtainTypedArray(R.array.base_colors);
    String category = expenses.get(i).get(0).getCategory();
    ViewHolder.categoryName.setText(
        ViewHolder.itemView.getContext().getString(R.string.category_title, category));
    StatisticsFragment.initBarChart(
        ViewHolder.barChart,
        expenses.get(i),
        xAxisLabel,
        type,
        colors,
        baseColors,
        ViewHolder.maxCount,
        ViewHolder.minCount,
        ViewHolder.expenseCount);
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
