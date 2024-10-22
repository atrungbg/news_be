package com.fpt.hungnm.assigmentfinal;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private List<NotificationModel> notificationList;
    private NotificationItemListener listener;

    public NotificationAdapter(List<NotificationModel> notificationList, NotificationItemListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void removeAllNotification() {
        notificationList.clear();
        notifyDataSetChanged();
        listener.onRemoveAll();
    }
}


class NotificationViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvNotificationTitle;
    private final TextView tvNotificationContent;
    private final TextView tvTime;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
        tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);
        tvTime = itemView.findViewById(R.id.tvTime);
    }

    public void bind(NotificationModel notification) {
        tvNotificationTitle.setText(notification.getTitle());
        tvNotificationContent.setText(notification.getContent());
        tvTime.setText(parseTime(notification.getTimeCreated()));
    }

    private String parseTime(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");
        return timeFormat.format(date);
    }
}

interface NotificationItemListener {
    void onRemoveAll();
}