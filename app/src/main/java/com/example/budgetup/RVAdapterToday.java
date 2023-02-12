package com.example.budgetup;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapterToday extends RecyclerView.Adapter<RVAdapterToday.TodayViewHolder> {
  private static ClickListener clickListener;

  public static class TodayViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    CardView cv;
    TextView expenseName;
    ImageView expensePhoto;
    TextView expenseCost;

    TodayViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      cv = itemView.findViewById(R.id.cv);
      expenseName = itemView.findViewById(R.id.expenseName);
      expenseCost = itemView.findViewById(R.id.expenseCost);
      expensePhoto = itemView.findViewById(R.id.expensePhoto);
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

  RVAdapterToday(List<Expense> expenses) {
    this.expenses = expenses;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public TodayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.today, viewGroup, false);
    TodayViewHolder today = new TodayViewHolder(v);
    return today;
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(TodayViewHolder ViewHolder, int i) {
    //        int img = expenses.get(i).getImage();
    //        deviceViewHolder.expenseName.setText(expenses.get(i).getExpenseName());
    //        deviceViewHolder.expenseCost.setText(expenses.get(i).getExpenseCost());
    //        deviceViewHolder.expensePhoto.setBackgroundResource(img);
    ViewHolder.expenseName.setText(expenses.get(i).getNote());
    ViewHolder.expenseCost.setText(
        expenses.get(i).getValue() + " " + expenses.get(i).getCurrency());
    ViewHolder.expensePhoto.setBackgroundResource(expenses.get(i).getImage());
  }

  @Override
  public int getItemCount() {
    return expenses.size();
  }

  public void setOnItemClickListener(ClickListener clickListener) {
    RVAdapterToday.clickListener = clickListener;
  }

  public interface ClickListener {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
  }
}
