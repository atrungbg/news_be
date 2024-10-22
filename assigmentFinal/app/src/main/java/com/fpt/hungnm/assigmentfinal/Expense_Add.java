package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.hungnm.assigmentfinal.Dal.MyDbContext;
import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Expense_Add extends AppCompatActivity {
    private static final String PATTERN = "yyyy-MM-dd";
    private static final String TAG = "Hungnm";
    private EditText edtExpenseMoney;
    private Button btnSave;
    private Spinner spinnerCategory;
    private EditText edtExpenseDes;

    private MyDbContext myDbContext;
    private TextView tvNoCategories;

    private List<Category> categories;

    private ImageView imgAddCategory;

    private TextView tvError;

    private ImageView imgBackToHome;

    private Transaction transaction;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_expense_add);
            bindingView();
            bindingAction();
            bindingData();
            receiverIntent();
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - onCreate - " + ex.getMessage());
        }
    }

    private void receiverIntent() {
        try{
            Intent intent = getIntent();
            if(intent.hasExtra("id")){
                int id = intent.getIntExtra("id",0);
                transaction = myDbContext.getTransactionById(id);
                btnSave.setText("Update");
                edtExpenseDes.setText(transaction.getTitle());
                edtExpenseMoney.setText(transaction.getPrice());
                for (Category category :
                        categories) {
                    if (category.getId() == Integer.parseInt(transaction.getCategory())) {
                        int i = adapter.getPosition(category.getTitle());
                        spinnerCategory.setSelection(i);
                        Log.e(TAG, "Expense_Add - receiverIntent - " + category.getTitle() + spinnerCategory.getSelectedItem().toString());
                        break;
                    }
                }
            }else{
                transaction = new Transaction();
            }
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - receiverIntent - " + ex.getMessage());
        }
    }

    private void bindingData() {
        try{
            myDbContext = new MyDbContext(this);
            categories = myDbContext.getAllCategoryByType("EXPENSE");
            List<String> titleCategories = new ArrayList<>();
            for (Category item :
                    categories) {
                titleCategories.add(item.getTitle());
            }
            if(titleCategories.size() == 0 ){
                tvNoCategories.setText("Chưa tạo danh mục cho thu nhập");
            }else{
                tvNoCategories.setText("");
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titleCategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - bindingData - " + ex.getMessage());
        }
    }

    private void bindingAction() {
        try{
            btnSave.setOnClickListener(this::onSaveClick);
            imgBackToHome.setOnClickListener(this::onBackToHome);
            imgAddCategory.setOnClickListener(this::onGoToCategory);
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - bindingAction - " + ex.getMessage());
        }
    }

    private void onGoToCategory(View view) {
        try{
            Intent i = new Intent(this, MainCategory.class);
            i.putExtra("type","EXPENSE");
            startActivity(i);

        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - onGoToCategory - " + ex.getMessage());
        }
    }

    private void onBackToHome(View view) {
        try{
            Intent i = new Intent(this, Home.class);
            startActivity(i);
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - onBackToHome - " + ex.getMessage());
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

    private void onSaveClick(View view) {
        try{
            if(transaction.getId() != 0){
                onUpdateTransaction();
                return;
            }
            if(!checkValid()){
                tvError.setText("Không được để trường nào trống");
            }else{
                Transaction transaction = new Transaction();
                Category category = getCategoryByTitle(spinnerCategory.getSelectedItem().toString());
                transaction.setCategory(String.valueOf(category.getId()));
                transaction.setPrice(String.valueOf(edtExpenseMoney.getText()));
                transaction.setTitle(edtExpenseDes.getText().toString());
                transaction.setIsIncome("EXPENSE");
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
                String currentDateString = dateFormat.format(currentDate);
                transaction.setCreateDate(currentDateString);
                long result = myDbContext.addTransaction(transaction);
                if(result == -1){
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    resetInput();
                }
            }
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - OnSaveClick - " + ex.getMessage());
        }
    }

    private void onUpdateTransaction() {
        try{
            if(!checkValid()){
                tvError.setText("Không được để trường nào trống");
            }else{
                Category category = getCategoryByTitle(spinnerCategory.getSelectedItem().toString());
                transaction.setCategory(String.valueOf(category.getId()));
                transaction.setPrice(String.valueOf(edtExpenseMoney.getText()));
                transaction.setTitle(edtExpenseDes.getText().toString());
                transaction.setIsIncome("EXPENSE");
                long result = myDbContext.updateTransaction(transaction);
                if(result == -1){
                    Toast.makeText(this, "Update thất bại", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
                    resetInput();
                }
            }
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - onUpdateTransaction - " + ex.getMessage());
        }
    }

    private void bindingView() {
        try{
            btnSave = findViewById(R.id.btn_expense_submit);
            edtExpenseMoney = findViewById(R.id.tv_expense_money);
            edtExpenseDes = findViewById(R.id.tv_expense_description);
            spinnerCategory = findViewById(R.id.spin_category);
            tvNoCategories = findViewById(R.id.tv_expense_nocategory);
            tvError = findViewById(R.id.tv_addexpense_error);
            imgAddCategory = findViewById(R.id.img_expense_add_category);
            imgBackToHome = findViewById(R.id.img_expense_back);
            transaction = new Transaction();
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - bindingView - " + ex.getMessage());
        }
    }

    private boolean checkValid(){
        try{
            if(edtExpenseMoney.getText().toString().equals("") || edtExpenseDes.getText().toString().equals("")){
                return false;
            }
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - checkValid - " + ex.getMessage());
        }
        return true;
    }

    private void resetInput(){
        try{
            edtExpenseMoney.setText("");
            edtExpenseDes.setText("");
            tvError.setText("");
            tvNoCategories.setText("");
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - resetInput - " + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(transaction.getId() == 0){
            bindingData();
        }
    }
}