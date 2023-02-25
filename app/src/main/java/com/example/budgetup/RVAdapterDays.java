package com.example.budgetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapterDays extends RecyclerView.Adapter<RVAdapterDays.DaysViewHolder> {
  private static RVAdapterDays.ClickListener clickListener;

  public static class DaysViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    CardView cv;
    ImageButton left;
    ImageButton right;
    TextView dateName;
    TextView year;

    DaysViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      cv = itemView.findViewById(R.id.cv);
      left = itemView.findViewById(R.id.left);
      right = itemView.findViewById(R.id.right);
      dateName = itemView.findViewById(R.id.monthName);
      year = itemView.findViewById(R.id.yearName);
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

  //    List<Expense> expenses;
  List<String[]> days_list;

  RVAdapterDays(List<String[]> days_list) {
    this.days_list = days_list;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public RVAdapterDays.DaysViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_item, viewGroup, false);
    RVAdapterDays.DaysViewHolder days = new RVAdapterDays.DaysViewHolder(v);
    return days;
  }

  @Override
  public void onBindViewHolder(RVAdapterDays.DaysViewHolder ViewHolder, int i) {
    //        int img = expenses.get(i).getImage();
    //        deviceViewHolder.expenseName.setText(expenses.get(i).getExpenseName());
    //        deviceViewHolder.expenseCost.setText(expenses.get(i).getExpenseCost());
    //        deviceViewHolder.expensePhoto.setBackgroundResource(img);
    ViewHolder.dateName.setText(days_list.get(i)[0]);
    ViewHolder.year.setText(days_list.get(i)[1]);
  }

  @Override
  public int getItemCount() {
    return days_list.size();
  }

  public void setOnItemClickListener(RVAdapterDays.ClickListener clickListener) {
    RVAdapterDays.clickListener = clickListener;
  }

  public interface ClickListener {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
  }
}
