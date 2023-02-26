package com.example.budgetup;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapterCalendar extends RecyclerView.Adapter<RVAdapterCalendar.CalendarViewHolder> {
  private static RVAdapterCalendar.ClickListener clickListener;
  private int selected_position = 15; // see position on JournalFragment

  public class CalendarViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    CardView cvCal;
    TextView weekName;
    TextView monthDay;
    LinearLayout linearLayout;

    CalendarViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      cvCal = itemView.findViewById(R.id.cvCal);
      weekName = itemView.findViewById(R.id.weekName);
      monthDay = itemView.findViewById(R.id.monthDay);
      linearLayout = itemView.findViewById(R.id.calLinear);
    }

    @Override
    public void onClick(View v) {
      if (clickListener != null) {
        int position = getAdapterPosition();
        selected_position = position;
        clickListener.onItemClick(position, v);
        notifyDataSetChanged();
      }
    }

    @Override
    public boolean onLongClick(View v) {
      clickListener.onItemLongClick(getAdapterPosition(), v);
      return false;
    }
  }

  List<String[]> calendar;

  RVAdapterCalendar(List<String[]> calendar) {
    this.calendar = calendar;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public RVAdapterCalendar.CalendarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.calendar_item, viewGroup, false);
    return new CalendarViewHolder(v);
  }

  @SuppressLint("ResourceAsColor")
  @Override
  public void onBindViewHolder(CalendarViewHolder ViewHolder, int i) {
    ViewHolder.weekName.setText(calendar.get(i)[0]);
    ViewHolder.monthDay.setText(calendar.get(i)[1]);

    if (selected_position == i) {
      ViewHolder.weekName.setTextColor(
          ViewHolder.itemView.getResources().getColor(R.color.base_100));
      ViewHolder.monthDay.setTextColor(
          ViewHolder.itemView.getResources().getColor(R.color.base_100));
      ViewHolder.linearLayout.setBackgroundColor(
          ViewHolder.itemView.getResources().getColor(R.color.primary_400));

    } else {
      ViewHolder.weekName.setTextColor(
          ViewHolder.itemView.getResources().getColor(R.color.base_800));
      ViewHolder.monthDay.setTextColor(
          ViewHolder.itemView.getResources().getColor(R.color.base_800));
      ViewHolder.linearLayout.setBackgroundColor(
          ViewHolder.itemView.getResources().getColor(R.color.primary_100));
    }
  }

  @Override
  public int getItemCount() {
    return calendar.size();
  }

  public void setOnItemClickListener(RVAdapterCalendar.ClickListener clickListener) {
    RVAdapterCalendar.clickListener = clickListener;
  }

  public interface ClickListener {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
  }
}
