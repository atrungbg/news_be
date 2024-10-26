package com.fpt.hungnm.assigmentfinal;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NotificationList extends AppCompatActivity implements NotificationItemListener {
    private RecyclerView rvNotification;
    private NotificationAdapter notificationAdapter;
    private TextView tvNoNotification;

    private MaterialToolbar toolbar;

    private List<NotificationModel> notificationModelList = new ArrayList<>(Arrays.asList(
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date()),
            new NotificationModel("Shopping budget has exceeds the Shopping budget has exceeds the", "Shopping budget has exceeds the Shopping budget has exceeds the", new Date())
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rvNotification = findViewById(R.id.rvNotification);
        tvNoNotification = findViewById(R.id.tvNoNotification);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.removeAll) {
            notificationAdapter.removeAllNotification();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        notificationAdapter = new NotificationAdapter(notificationModelList, this);
        rvNotification.setAdapter(notificationAdapter);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        rvNotification.setHasFixedSize(true);
    }

    @Override
    public void onRemoveAll() {
        tvNoNotification.setVisibility(View.VISIBLE);
    }
}