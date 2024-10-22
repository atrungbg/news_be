package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AlertDialog;
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

public class InCome_Add extends AppCompatActivity {
    private static final String PATTERN = "yyyy-MM-dd";
    private static final String TAG ="Hungnm";
    private EditText edtMoney;
    private Button btnSave;
    private Spinner spCategory;
    private EditText edtDescription;

    private MyDbContext myDbContext;
    private TextView tvNoCategories;

    private List<Category> categories;

    private ImageView imgAddCategory;

    private TextView tvError;
    private ImageView imgBackToHome;

    private Transaction transaction;

    public interface PaymentDialogListener {
        void onPaymentConfirmed(boolean isPaid);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_in_come_add);
            bindingView();
            bindingAction();
            bindingData();
            receiverIntent();
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - onCreate - " + ex.getMessage());
        }
    }

    private ArrayAdapter<String> adapter;
    private void receiverIntent() {
        try{
            Intent intent = getIntent();
            if(intent.hasExtra("id")){
                int id = intent.getIntExtra("id",0);
                transaction = myDbContext.getTransactionById(id);
                btnSave.setText("Update");
                edtDescription.setText(transaction.getTitle());
                edtMoney.setText(transaction.getPrice());
                for (Category category :
                        categories) {
                    if (category.getId() == Integer.parseInt(transaction.getCategory())) {
                        int i = adapter.getPosition(category.getTitle());
                        spCategory.setSelection(i);
                        Log.e(TAG, "Expense_Add - receiverIntent - " + category.getTitle() + spCategory.getSelectedItem().toString());
                        break;
                    }
                }
            }
        }catch (Exception ex){
            Log.e(TAG, "Expense_Add - receiverIntent - " + ex.getMessage());
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

    private void bindingData() {
        try{
            myDbContext = new MyDbContext(this);
            categories = myDbContext.getAllCategoryByType("INCOME");
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
            spCategory.setAdapter(adapter);
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - bindingData - " + ex.getMessage());
        }
    }

    private void bindingAction() {
        try{
            imgBackToHome.setOnClickListener(this::onBackToHome);
            btnSave.setOnClickListener(this::onSaveClick);
            imgAddCategory.setOnClickListener(this::onAddCategory);

        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - bindingAction - " + ex.getMessage());
        }
    }

    private void onAddCategory(View view) {
        try{
            Intent i = new Intent(this,MainCategory.class);
            i.putExtra("type","INCOME");
            startActivity(i);
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - bindingAction - " + ex.getMessage());
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
        try {
            if (!checkValid()) {
                tvError.setText("Không được để trường nào trống");
                return;
            }

            int transactionCount = myDbContext.getTransactionCount();
            Log.d(TAG, "Số lượng giao dịch hiện tại: " + transactionCount);

            if (transactionCount >= 5) {
                showPaymentDialog(isPaid -> {
                    if (isPaid) {
                        addTransaction();
                        Toast.makeText(this, "Bạn đã huỷ thanh toán.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                addTransaction();
            }

        } catch (Exception ex) {
            Log.e(TAG, "InCome_Add - onSaveClick - " + ex.getMessage());
        }
    }

    private void addTransaction() {
        Transaction transaction = new Transaction();
        Category category = getCategoryByTitle(spCategory.getSelectedItem().toString());
        transaction.setCategory(String.valueOf(category.getId()));
        transaction.setPrice(String.valueOf(edtMoney.getText()));
        transaction.setTitle(edtDescription.getText().toString());
        transaction.setIsIncome("INCOME");

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        String currentDateString = dateFormat.format(currentDate);
        transaction.setCreateDate(currentDateString);

        long result = myDbContext.addTransaction(transaction);
        if (result == -1) {
            Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            resetInput();
        }
    }

    private void showPaymentDialog(PaymentDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Số lượng giao dịch của bạn đã vượt quá 5. Bạn có muốn thanh toán không?");

        builder.setPositiveButton("Thanh toán", (dialog, which) -> {
            Toast.makeText(this, "Thanh toán đã được thực hiện!", Toast.LENGTH_SHORT).show();
            listener.onPaymentConfirmed(true);
        });

        builder.setNegativeButton("Huỷ", (dialog, which) -> {
            dialog.dismiss();
            listener.onPaymentConfirmed(false);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean checkValid(){
        try{
            if(edtMoney.getText().toString().equals("") || edtDescription.getText().toString().equals("")){
                return false;
            }
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - checkValid - " + ex.getMessage());
        }
        return true;
    }

    private void resetInput(){
        try{
            edtMoney.setText("");
            edtDescription.setText("");
            tvError.setText("");
            tvNoCategories.setText("");
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - resetInput - " + ex.getMessage());
        }
    }

    private void bindingView() {
        try{
            edtMoney = findViewById(R.id.tv_income_money);
            btnSave = findViewById(R.id.btn_income_submit);
            spCategory = findViewById(R.id.spin_income_add_category);
            edtDescription = findViewById(R.id.tv_income_description);
            tvError = findViewById(R.id.tv_addincome_error);
            tvNoCategories = findViewById(R.id.tv_income_nocategory);
            imgAddCategory = findViewById(R.id.img_income_add_category);
            imgBackToHome = findViewById(R.id.img_income_back);
            transaction = new Transaction();
        }catch (Exception ex){
            Log.e(TAG, "InCome_Add - bindingView - " + ex.getMessage());
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