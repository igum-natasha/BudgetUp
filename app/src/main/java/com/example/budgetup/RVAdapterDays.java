package com.example.budgetup;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    String type;

    DaysViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      cv = itemView.findViewById(R.id.cv);
      left = itemView.findViewById(R.id.left);
      right = itemView.findViewById(R.id.right);
      dateName = itemView.findViewById(R.id.monthName);
      year = itemView.findViewById(R.id.yearName);
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

  List<Date[]> days_list;
  String type;

  RVAdapterDays(List<Date[]> days_list, String type) {

    this.days_list = days_list;
    this.type = type;
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
    SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    switch (type) {
      case "week":
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatWeekDay = new SimpleDateFormat("MMM dd");
        ViewHolder.dateName.setText(
            formatWeekDay.format(days_list.get(i)[0])
                + " - "
                + formatWeekDay.format(days_list.get(i)[1]));
        break;
      case "day":
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMMM dd");
        ViewHolder.dateName.setText(formatDay.format(days_list.get(i)[0]));
        break;
      case "month":
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatMonth = new SimpleDateFormat("MMMM");
        ViewHolder.dateName.setText(formatMonth.format(days_list.get(i)[0]));
        break;
    }
    ViewHolder.year.setText(formatYear.format(days_list.get(i)[0]));
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
