package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainStatitics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG ="Hungnm";
    private final int REQUEST_CODE = 4;
    private PieChart pieChart;
    private Spinner spMonth;
    private BarChart barChart;

    private ImageView btnHome;
    private ImageView btnTransaction;
    private ImageView btnBudget;
    private ImageView btnStatistic;

    private MyFbContext dbContext;

    private int month;

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_statitics);
        bindingView();
        bindingAction();
        initData();
    }

    private void initData() {
        try{
            Date currentDate = new Date();
            month = currentDate.getMonth() + 1;
            String spClicked = "Tháng " + String.valueOf(month);
            spMonth.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.month)));
            spMonth.setSelection(month - 1);
            getData();
        }catch (Exception ex){
            Log.e(TAG, "MainStatitics - initData - " + ex.getMessage());
        }
    }

    private void getData() {
        try {
            // Lấy giao dịch theo tháng từ Firebase
            dbContext.getTransactionByMonth(month, new MyFbContext.FirebaseTransactionCallback() {
                @Override
                public void onSuccess(List<Transaction> transactions) {
                    Log.d(TAG, "Giao dịch nhận được: " + transactions.size() + " giao dịch.");
                    List<Transaction> transactionIncomes = new ArrayList<>();
                    List<Transaction> transactionExpenses = new ArrayList<>();

                    // Phân loại giao dịch theo thu nhập và chi phí
                    for (Transaction item : transactions) {
                        Log.d(TAG, "Giao dịch: " + item.toString()); // Log từng giao dịch
                        if (item.getIsIncome().equals("EXPENSE")) {
                            transactionExpenses.add(item);
                        } else {
                            transactionIncomes.add(item);
                        }
                    }

                    // Lấy danh sách danh mục từ Firebase
                    dbContext.getAllCategory(new MyFbContext.CategoryCallback() {
                        @Override
                        public void onSuccess(List<Category> categories) {
                            Log.d(TAG, "Danh mục nhận được: " + categories.size() + " danh mục.");
                            List<Category> categoryIncome = new ArrayList<>();
                            List<Category> categoryExpense = new ArrayList<>();

                            // Tính tổng cho mỗi danh mục
                            for (Category item : categories) {
                                for (Transaction tr : transactions) {
                                    if (Integer.parseInt(tr.getCategory()) == item.getId()) {
                                        if (tr.getPrice() != null && !tr.getPrice().equals("")) {
                                            Long currentTotal = item.getTotal() != null ? item.getTotal() : 0L;
                                            Long price = Long.valueOf(tr.getPrice());
                                            Long newTotal = currentTotal + price;
                                            item.setTotal(newTotal);
                                            Log.d(TAG, "item.getTotal(): " + item.getTotal());
                                            Log.d(TAG, "tr.getPrice(): " + tr.getPrice());
                                        }
                                    }
                                }
                            }


                            // Phân loại danh mục theo thu nhập và chi phí
                            for (Category item : categories) {
                                if (item.getIsIncome().equals("EXPENSE")) {
                                    categoryExpense.add(item);
                                } else {
                                    categoryIncome.add(item);
                                }
                            }

                            // Tạo biểu đồ tròn cho chi phí
                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for (Category item : categoryExpense) {
                                entries.add(new PieEntry(item.getTotal(), item.getTitle()));
                                Log.d(TAG, "Thêm vào biểu đồ tròn: " + item.getTitle() + " - Tổng: " + item.getTotal());
                            }
                            PieDataSet pieDataSet = new PieDataSet(entries, "Expenses");
                            pieDataSet.setColors(generateRandomColors(entries.size()));
                            PieData pieData = new PieData(pieDataSet);
                            pieChart.setData(pieData);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.animateY(1000);
                            pieChart.invalidate();

                            // Tạo biểu đồ cột cho thu nhập
                            ArrayList<BarEntry> visitors = new ArrayList<>();
                            int index = 0; // Thêm biến index để theo dõi vị trí
                            for (Category item : categoryIncome) {
                                visitors.add(new BarEntry(index++, item.getTotal())); // Sử dụng index thay vì item.getId()
                                Log.d(TAG, "Thêm vào biểu đồ cột: " + item.getTitle() + " - Tổng: " + item.getTotal());
                            }

                            BarDataSet barDataSet = new BarDataSet(visitors, "Thống kê");
                            int[] colors  = new int[]{
                                    Color.rgb(64, 89, 128),
                                    Color.rgb(149, 165, 124),
                                    Color.rgb(217, 184, 162),
                                    Color.rgb(191, 134, 134),
                                    Color.rgb(179, 48, 80)
                            };
                            barDataSet.setColors(colors);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(12f);

                            BarData barData = new BarData(barDataSet);
                            barData.setBarWidth(0.9f);
                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setEnabled(false);
                            barChart.getLegend().setEnabled(true);
                            barChart.getXAxis().setDrawGridLines(false);
                            barChart.getAxisLeft().setDrawGridLines(false);
                            barChart.getAxisRight().setDrawGridLines(false);
                            barChart.setDrawValueAboveBar(true);
                            barChart.setDrawBarShadow(false);
                            barChart.setDrawGridBackground(false);

                            barChart.animateY(1000);
                            barChart.invalidate(); // Cập nhật biểu đồ

                            // Hiển thị danh sách các danh mục thu nhập
                            ArrayList<String> dataList = new ArrayList<>();
                            for (Category item : categoryIncome) {
                                String s = "ID: " + item.getId() + " Name: " + item.getTitle();
                                dataList.add(s);
                                Log.d(TAG, "Danh mục thu nhập: " + s);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, dataList);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e(TAG, "getData - Get categories error: " + errorMessage);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "getData - Get transactions error: " + e.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MainStatistics - bindingData - " + ex.getMessage());
        }
    }


    private int[] generateRandomColors(int size) {
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) {
            colors[i] = Color.rgb((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
        }
        return colors;
    }


    private void bindingAction() {
        try{
            dbContext = new MyFbContext();
            btnHome.setOnClickListener(this::goToHome);
            btnBudget.setOnClickListener(this::goToBudget);
            btnTransaction.setOnClickListener(this::goToTransaction);
            btnStatistic.setOnClickListener(this::btnStatistic);
            spMonth.setOnItemSelectedListener(this);
        }catch (Exception ex){
            Log.e(TAG, "MainStatitics - bindingAction - " + ex.getMessage());
        }
    }

    private void goToHome(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,Home.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainStatistic - goToHome - " + ex.getMessage());
        }
    }

    private void btnStatistic(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,MainStatitics.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainStatistic - btnStatistic - " + ex.getMessage());
        }
    }

    private void goIntent(){
        try{
            btnStatistic.setColorFilter(ContextCompat.getColor(this, R.color.xam), PorterDuff.Mode.SRC_IN);
        }catch (Exception ex){
            Log.e(TAG, "MainStatistic - goIntent - " + ex.getMessage());
        }
    }

    private void goToTransaction(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,MainTransaction.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainStatistic - goToTransaction - " + ex.getMessage());
        }
    }

    private void goToBudget(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,MainBudget.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "Home - btnBudget - " + ex.getMessage());
        }
    }
    

    private void bindingView() {
        try{
            listView = findViewById(R.id.listview);
            pieChart = findViewById(R.id.piechart);
            spMonth = findViewById(R.id.sp_statistic_month);
            barChart = findViewById(R.id.bar_chart);
            btnTransaction = findViewById(R.id.img_transaction_btnTransaction);
            btnHome = findViewById(R.id.img_statistic_btnHome);
            btnStatistic = findViewById(R.id.img_statistic_btnAccount);
        }catch (Exception ex){
            Log.e(TAG, "MainStatitics - bindingView - " + ex.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        month = position+ 1;
        getData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}