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

import com.fpt.hungnm.assigmentfinal.Dal.MyDbContext;
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

    private MyDbContext dbContext;

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
        try{
            List<Transaction> transactions = new ArrayList<>();
            transactions = dbContext.getTransactionByMonth(month);
            List<Transaction> transactionIncomes = new ArrayList<>();
            List<Transaction> transactionExpenses = new ArrayList<>();
            if(transactions.size()>0){
                for (Transaction item :
                        transactions) {
                    if(item.getIsIncome().equals("EXPENSE")){
                        transactionExpenses.add(item);
                    }else{
                        transactionIncomes.add(item);
                    }
                }
            }


            List<Category> categoryIncome = new ArrayList<>();
            List<Category> categoryExpense = new ArrayList<>();
            List<Category> categories = new ArrayList<>();
            categories = dbContext.getAllCategory();
            if(categories.size()>0){
                for (Category item :
                        categories) {
                    for (Transaction tr :
                            transactions) {
                        if (Integer.parseInt(tr.getCategory()) == item.getId()) {
                            if(tr.getPrice() != null && !tr.getPrice().equals("")){
                                Long total = item.getTotal() + Long.valueOf(tr.getPrice());
                                item.setTotal(total);
                            }
                        }
                    }
                }

                for (Category item :
                        categories) {
                    if(item.getIsIncome().equals("EXPENSE")){
                        categoryExpense.add(item);
                    }else{
                        categoryIncome.add(item);
                    }
                }
            }
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (Category item :
                    categoryExpense) {
                entries.add(new PieEntry(item.getTotal(),item.getTitle()));
            }
            PieDataSet pieDataSet = new PieDataSet(entries,"Expenses");
            pieDataSet.setColors(generateRandomColors(entries.size()));
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000);
            pieChart.invalidate();

            ArrayList<BarEntry> visitors = new ArrayList<>();
            for (Category item :
                    categoryIncome) {
                visitors.add(new BarEntry(item.getId(),item.getTotal()));
            }
            BarDataSet barDataSet = new BarDataSet(visitors,"Visitors");
            barDataSet.setColors(generateRandomColors(5));
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData = new BarData(barDataSet);

            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.animateY(2000);

            ArrayList<String> dataList = new ArrayList<>();
            for (Category item :
                    categoryIncome) {
                String s = "ID: " + item.getId() + " Name: " + item.getTitle();
                dataList.add(s);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);

        }catch (Exception ex){
            Log.e(TAG, "MainStatitics - bindingData - " + ex.getMessage());
        }
    }

    public static int[] generateRandomColors(int count) {
        int[] colors = new int[count];
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);

            int color = (255 << 24) | (r << 16) | (g << 8) | b;

            colors[i] = color;
        }

        return colors;
    }

    private void bindingAction() {
        try{
            dbContext = new MyDbContext(this);
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