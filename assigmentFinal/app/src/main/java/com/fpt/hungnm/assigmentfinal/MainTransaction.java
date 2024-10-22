package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fpt.hungnm.assigmentfinal.Adapter.TransitionSearchAdapter;
import com.fpt.hungnm.assigmentfinal.Dal.MyDbContext;
import com.fpt.hungnm.assigmentfinal.Model.Budget;
import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainTransaction extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="HungnmError";
    private static final int REQUEST_CODE =2;
    private RecyclerView rcvTransaction;
    private TransitionSearchAdapter transactionAdapter;
    private EditText edtTitle;
    private Spinner spCategory;
    private EditText edtStartDate;
    private EditText edtToDate;
    private Button btnSearch;

    private ImageView btnHome;
    private ImageView btnBudget;
    private ImageView btnAccount;

    private ImageView btnTransaction;

    private MyDbContext dbContext;

    private TextView tvError;

    List<Transaction> list;

    List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_transaction);
        supportInvalidateOptionsMenu();

        bindingView();
        bindingAction();
        bindingData();
        setAdapter();
    }

    private void bindingData() {
        try{
            list = dbContext.getAllTransition();
            categories = dbContext.getAllCategory();
            List<String> titleCategories = new ArrayList<>();
            titleCategories.add("ALL");
            for (Category item :
                    categories) {
                titleCategories.add(item.getTitle());
            }
            spCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,titleCategories));
            edtStartDate.setOnClickListener(this);
            edtToDate.setOnClickListener(this);

            
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - bindingData - " + ex.getMessage());
        }
    }

    private void onSearch(View view) {
        try{
            String toDate = edtToDate.getText().toString();
            String fromDate = edtStartDate.getText().toString();
            if(!toDate.equals("") && !fromDate.equals("")){
                boolean check = compareDates(fromDate,toDate);
                if(!check){
                    tvError.setText("Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                    return;
                }
            }
            String title = edtTitle.getText().toString();
            String categoryId ="ALL";
            Category category = getCategoryByTitle(spCategory.getSelectedItem().toString());
            if(category != null){
                categoryId = String.valueOf(category.getId());
            }
            list = dbContext.getTransactionSearch(title,categoryId,fromDate,toDate);
            setAdapter();
            tvError.setText("");

        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - onSearch - " + ex.getMessage());
        }
    }

    public Category getCategoryByTitle(String title) {
        try{
            for (Category category : categories) {
                if (category.getTitle().equals(title)) {
                    return category;
                }
            }
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - onSaveClick - " + ex.getMessage());
        }
        return null;
    }

    private void setAdapter() {
        try{
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvTransaction.setLayoutManager(linearLayoutManager);
            transactionAdapter = new TransitionSearchAdapter(list);
            rcvTransaction.setAdapter(transactionAdapter);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rcvTransaction.addItemDecoration(itemDecoration);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - setAdapter - " + ex.getMessage());
        }
    }

    private void bindingAction() {
        try{
            dbContext= new MyDbContext(this);
            btnHome.setOnClickListener(this::goToHome);
            btnAccount.setOnClickListener(this::goToAccount);
            btnSearch.setOnClickListener(this::onSearch);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - onCreate - " + ex.getMessage());
        }
    }

    public static boolean compareDates(String dateString1, String dateString2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(dateString1);
            Date date2 = dateFormat.parse(dateString2);
            return date1.compareTo(date2) < 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void goToAccount(View view) {
        try{
            Intent i = new Intent(this,MainStatitics.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - goToAccount - " + ex.getMessage());
        }
    }

    private void goToBudget(View view) {
        try{
            Intent i = new Intent(this, MainBudget.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - goToBudget - " + ex.getMessage());
        }
    }

    private void goToHome(View view) {
        try{
            Intent i = new Intent(this,Home.class);
            startActivityForResult(i,REQUEST_CODE);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - goToHome - " + ex.getMessage());
        }
    }

    private void bindingView() {
        try{
            rcvTransaction = findViewById(R.id.rcv_main_transaction);
            btnHome = findViewById(R.id.img_transaction_btnHome);
            btnAccount = findViewById(R.id.img_transaction_btnAccount);
            btnTransaction = findViewById(R.id.img_transaction_btnTransaction);
            edtTitle =findViewById(R.id.edt_transaction_title);
            spCategory = findViewById(R.id.sp_transation_category);
            edtStartDate = findViewById(R.id.edt_transaction_fromDate);
            edtToDate = findViewById(R.id.edt_transaction_toDate);
            btnSearch = findViewById(R.id.btn_transaction_search);
            tvError = findViewById(R.id.tv_transaction_error);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - bindingView - " + ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            getMenuInflater().inflate(R.menu.menu_transaction,menu);
        }catch (Exception ex){
            Log.e(TAG, "MainTransaction - bindingView - " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnTransaction.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onClick(View v) {
        if(v == edtStartDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date ="";
                    if(month>8){
                        date = dayOfMonth + "/" + (month+1)+ "/" + year;
                    }else{
                        date = dayOfMonth + "/0" + (month + 1) + "/" + year;
                    }
                    edtStartDate.setText(date);
                }
            },year,month,day);
            datePickerDialog.show();
        }
        if(v == edtToDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date ="";
                    if(month>8){
                        date = dayOfMonth + "/" + (month+1)+ "/" + year;
                    }else{
                        date = dayOfMonth + "/0" + (month + 1) + "/" + year;
                    }
                    edtToDate.setText(date);
                }
            },year,month,day);
            datePickerDialog.show();
        }
    }
}