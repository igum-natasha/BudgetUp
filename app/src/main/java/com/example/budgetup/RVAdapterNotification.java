package com.example.budgetup;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class RVAdapterNotification
    extends RecyclerView.Adapter<RVAdapterNotification.NotificationViewHolder> {
  private static ClickListener clickListener;

  public static class NotificationViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener, View.OnLongClickListener {

    CardView cv;
    TextView notificationInfo;
    ImageView status;
    TextView notificationDate;

    NotificationViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      cv = itemView.findViewById(R.id.cv);
      notificationInfo = itemView.findViewById(R.id.notificationInfo);
      notificationDate = itemView.findViewById(R.id.notificationDate);
      status = itemView.findViewById(R.id.status);
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

  List<Notification> notifications;

  RVAdapterNotification(List<Notification> notifications) {
    this.notifications = notifications;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public RVAdapterNotification.NotificationViewHolder onCreateViewHolder(
      ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.notification_item, viewGroup, false);
    RVAdapterNotification.NotificationViewHolder notificationViewHolder =
        new RVAdapterNotification.NotificationViewHolder(v);
    return notificationViewHolder;
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(RVAdapterNotification.NotificationViewHolder ViewHolder, int i) {
    ViewHolder.notificationInfo.setText(
        notifications.get(i).getName() + ": " + notifications.get(i).getMessage());
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    ViewHolder.notificationDate.setText(formatDate.format(notifications.get(i).getDate()));
    String image;
    if (notifications.get(i).getStatus()) {
      image = "check";
    } else {
      image = "close";
    }
    ViewHolder.status.setImageResource(
        ViewHolder.itemView
            .getResources()
            .getIdentifier(image, "drawable", ViewHolder.itemView.getContext().getPackageName()));
  }

  @Override
  public int getItemCount() {
    return notifications.size();
  }

  public void setOnItemClickListener(RVAdapterNotification.ClickListener clickListener) {
    RVAdapterNotification.clickListener = clickListener;
  }

  public interface ClickListener {
    void onItemClick(int position, View v);

    void onItemLongClick(int position, View v);
  }
}
