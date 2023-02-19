package com.example.budgetup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVAdapterCalendar extends RecyclerView.Adapter<RVAdapterCalendar.CalendarViewHolder> {
  private static RVAdapterCalendar.ClickListener clickListener;

  public static class CalendarViewHolder extends RecyclerView.ViewHolder
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
      clickListener.onItemClick(getAdapterPosition(), v);
    }

    @Override
    public boolean onLongClick(View v) {
      clickListener.onItemLongClick(getAdapterPosition(), v);
      return false;
    }
  }

  List<String[]> calendar;
  List<LinearLayout> linearLayoutList = new ArrayList<>();
  List<TextView> weekNameList = new ArrayList<>();
  List<TextView> monthDayList = new ArrayList<>();

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
    RVAdapterCalendar.CalendarViewHolder cal = new RVAdapterCalendar.CalendarViewHolder(v);
    return cal;
  }

  @Override
  public void onBindViewHolder(CalendarViewHolder ViewHolder, int i) {
    //        int img = expenses.get(i).getImage();
    //        deviceViewHolder.expenseName.setText(expenses.get(i).getExpenseName());
    //        deviceViewHolder.expenseCost.setText(expenses.get(i).getExpenseCost());
    //        deviceViewHolder.expensePhoto.setBackgroundResource(img);
    ViewHolder.weekName.setText(calendar.get(i)[0]);
    ViewHolder.monthDay.setText(calendar.get(i)[1]);
    linearLayoutList.add(ViewHolder.linearLayout);
    weekNameList.add(ViewHolder.weekName);
    monthDayList.add(ViewHolder.monthDay);

    ViewHolder.linearLayout.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            for (int item = 0; item < linearLayoutList.size(); item++) {
              linearLayoutList
                  .get(item)
                  .setBackgroundColor(view.getResources().getColor(R.color.primary_100));
              weekNameList.get(item).setTextColor(view.getResources().getColor(R.color.base_800));
              monthDayList.get(item).setTextColor(view.getResources().getColor(R.color.base_800));
            }
            linearLayoutList
                .get(ViewHolder.getAdapterPosition())
                .setBackgroundColor(view.getResources().getColor(R.color.primary_400));
            weekNameList
                .get(ViewHolder.getAdapterPosition())
                .setTextColor(view.getResources().getColor(R.color.base_100));
            monthDayList
                .get(ViewHolder.getAdapterPosition())
                .setTextColor(view.getResources().getColor(R.color.base_100));
          }
        });
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
