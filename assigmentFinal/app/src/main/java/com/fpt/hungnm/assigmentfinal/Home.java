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
import com.fpt.hungnm.assigmentfinal.Dal.MyDbContext;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;

import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity implements TransitionRecyclerViewAdapter.TransitionListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {
    private TransitionRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;
    private static final String TAG ="Hungnm";
    private static final int REQUEST_CODE = 1 ;

    private MyDbContext dbContext;
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
        try{
            List<Transaction> list = dbContext.getAllTransition();
            adapter.setTransitionListener(this);
            adapter.setList(list);
            LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);
        }catch (Exception ex){
            Log.e(TAG, "Home - setAdapter - " + ex.getMessage());
        }
    }

    private void initData() {
        try{
            dbContext = new MyDbContext(this);
            Date currentDate = new Date();
            month = currentDate.getMonth() + 1;
            String spClicked = "Tháng " + String.valueOf(month);
            spMonth.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.month)));
            spMonth.setSelection(month - 1);

            getData();
        }catch (Exception ex){
            Log.e(TAG, "Home - bindingData - " + ex.getMessage());
        }
    }

    private void getData(){
        try{
            tvBalance.setText(String.valueOf(dbContext.getBalance(month,null,null)));
            tvHomeExpense.setText("$" + String.valueOf(dbContext.getBalance(month,"EXPENSE",null)));
            tvHomeIncome.setText("$" + String.valueOf(dbContext.getBalance(month,"INCOME",null)));
        }catch (Exception ex){
            Log.e(TAG, "Home - getData - " + ex.getMessage());
        }
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
            dbContext = new MyDbContext(this);
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
            getData();
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