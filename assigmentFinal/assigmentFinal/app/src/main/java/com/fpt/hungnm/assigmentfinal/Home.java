package com.fpt.hungnm.assigmentfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.hungnm.assigmentfinal.Adapter.TransitionRecyclerViewAdapter;
import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;

import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity implements TransitionRecyclerViewAdapter.TransitionListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {
    private TransitionRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;
    private static final String TAG ="Hungnm";
    private static final int REQUEST_CODE = 1 ;

    private MyFbContext dbContext;
    private ImageView btnHome;
    private ImageView btnTransaction;
    private ImageView btnBudget;
    private ImageView btnAccount;
    private LinearLayout btnGoIncome;
    private LinearLayout btnGoExpense;
    private TextView tvBalance;
    private Spinner spMonth;
    private TextView tvHomeIncome;
    private TextView tvHomeExpense;
    private ImageButton btnLogout;
    private boolean isLoggedIn = true;
    private Transaction transactionClicked;

    private int month;
    private Button viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            bindingView();
            initData();
            bindingAction();
            reciveIntent();
            setAdapter();
        }catch (Exception ex){
            Log.e(TAG, "Home - onCreate - " + ex.getMessage());
        }
    }

    private void logout(View view) {
        isLoggedIn = false;

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }


    private void reciveIntent() {
        try{
            Intent intent = getIntent();
            btnHome.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            setAdapter();
        }catch (Exception ex){
            Log.e(TAG, "Home - reciveIntent - " + ex.getMessage());
        }
    }

    private void goIntent(){
        try{
            btnHome.setColorFilter(ContextCompat.getColor(this, R.color.xam), PorterDuff.Mode.SRC_IN);
        }catch (Exception ex){
            Log.e(TAG, "Home - reciveIntent - " + ex.getMessage());
        }
    }

    private void setAdapter() {
        // Gọi phương thức lấy tất cả giao dịch từ Firebase
        dbContext.getAllTransactions(new MyFbContext.TransactionListener() {
            @Override
            public void onSuccess(List<Transaction> list) {
                // Thiết lập listener cho adapter
                adapter.setTransitionListener(Home.this);
                adapter.setList(list);

                // Thiết lập layout manager cho RecyclerView
                LinearLayoutManager manager = new LinearLayoutManager(Home.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

                // Thêm DividerItemDecoration
                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Home.this, DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Home - setAdapter - " + errorMessage);
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void initData() {
        dbContext = new MyFbContext();
        Date currentDate = new Date();
        month = currentDate.getMonth() + 1;
        spMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.month)));
        spMonth.setSelection(month - 1);

        fetchTransactionsFromFirebase();
    }

    private void fetchTransactionsFromFirebase() {
        dbContext.getAllTransactions(new MyFbContext.OnTransactionFetchListener() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                adapter.setList(transactions);
                adapter.notifyDataSetChanged();
                updateBalanceInfo(transactions);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Home.this, "Failed to load transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void bindingAction() {
        try{
            btnGoIncome.setOnClickListener(this::goToIncome);
            btnGoExpense.setOnClickListener(this::goToExpense);
            spMonth.setOnItemSelectedListener(this);
            btnTransaction.setOnClickListener(this::goToTransaction);
            btnAccount.setOnClickListener(this::goToStatistic);
            viewAll.setOnClickListener(this::goToTransaction);
            btnLogout.setOnClickListener(this::logout);
        }catch (Exception ex){
            Log.e(TAG, "Home - bindingAction - " + ex.getMessage());
        }
    }


    private void goToStatistic(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,MainStatitics.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "Home - goToStatistic - " + ex.getMessage());
        }
    }

    private void goToTransaction(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,MainTransaction.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "Home - goToTransaction - " + ex.getMessage());
        }
    }

    private void goToExpense(View view) {
        try{
            goIntent();
            Intent i = new Intent(this,Expense_Add.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "Home - goToExpense - " + ex.getMessage());
        }
    }

    private void goToIncome(View view) {
        try{
            Intent i = new Intent(this, InCome_Add.class);
            startActivityForResult(i,REQUEST_CODE);
            goIntent();
        }catch (Exception ex){
            Log.e(TAG, "Home - goToIncome - " + ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == REQUEST_CODE) {
                setAdapter();
            }
        }
    }

    private void updateBalanceInfo(List<Transaction> transactions) {
        double totalIncome = 0, totalExpense = 0;

        for (Transaction transaction : transactions) {
            double amount = Double.parseDouble(transaction.getPrice());
            if (transaction.getIsIncome().equals("INCOME")) {
                totalIncome += amount;
            } else {
                totalExpense += amount;
            }
        }

        tvBalance.setText("$" + (totalIncome - totalExpense));
        tvHomeIncome.setText("$" + totalIncome);
        tvHomeExpense.setText("$" + totalExpense);
    }

    private void bindingView() {
        try{
            viewAll = findViewById(R.id.viewALl);
            btnAccount = findViewById(R.id.btnAccount);
            btnHome = findViewById(R.id.btnHome);
            btnTransaction = findViewById(R.id.btnTransaction);
            btnGoIncome = findViewById(R.id.ll_go_income);
            btnGoExpense = findViewById(R.id.ll_go_expense);
            spMonth = findViewById(R.id.sp_home_month);
            tvBalance = findViewById(R.id.tv_home_balance);
            recyclerView = findViewById(R.id.rcv_home_transition);
            tvHomeIncome = findViewById(R.id.home_income_amount);
            tvHomeExpense = findViewById(R.id.home_expense_amount);
            btnLogout = findViewById(R.id.btnLogout); // Thêm dòng này
            adapter = new TransitionRecyclerViewAdapter();
            dbContext = new MyFbContext();
        }catch (Exception ex){
            Log.e(TAG, "Home - bindingView - " + ex.getMessage());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        try{
            transactionClicked = adapter.getTransition(position);
            if(transactionClicked.getIsIncome().equals("INCOME")){
                Intent i = new Intent(this, InCome_Add.class);
                i.putExtra("id",transactionClicked.getId());
                startActivityForResult(i,REQUEST_CODE);
            }else{
                Intent i = new Intent(this, Expense_Add.class);
                i.putExtra("id",transactionClicked.getId());
                startActivityForResult(i,REQUEST_CODE);
            }
        }catch (Exception ex){
            Log.e(TAG, "Home.Java - onItemClick - " + ex.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try{
            String selectedItem = parent.getItemAtPosition(position).toString();
            month = position+ 1;
            fetchTransactionsFromFirebase();
        }catch (Exception ex){
            Log.e(TAG, "Home.Java - onItemSelected - " + ex.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "longcliock" + position +"/"+id , Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnHome.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }
}