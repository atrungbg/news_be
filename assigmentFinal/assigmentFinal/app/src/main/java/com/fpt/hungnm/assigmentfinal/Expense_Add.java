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

import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
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

    private MyFbContext myFbContext;
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
        try {
            Intent intent = getIntent();
            if (intent.hasExtra("id")) {
                int id = intent.getIntExtra("id", 0);
                Log.e(TAG, "Expense_Add - receiverIntent - Received ID: " + id);

                myFbContext.getTransactionById(id, new MyFbContext.TransactionCallback() {
                    @Override
                    public void onSuccess(Transaction receivedTransaction) {
                        if (receivedTransaction != null) {
                            // Cập nhật đối tượng transaction với dữ liệu nhận được
                            transaction = receivedTransaction; // Thêm dòng này

                            btnSave.setText("Update");
                            edtExpenseDes.setText(transaction.getTitle());
                            edtExpenseMoney.setText(transaction.getPrice() != null ? transaction.getPrice() : "");

                            // Kiểm tra danh mục tương ứng
                            for (Category category : categories) {
                                if (category.getId() == Integer.valueOf(transaction.getCategory())) {
                                    int position = adapter.getPosition(category.getTitle());
                                    if (position >= 0) {
                                        spinnerCategory.setSelection(position);
                                        Log.e(TAG, "Expense_Add - receiverIntent - Selected Category: " + category.getTitle());
                                    } else {
                                        Log.e(TAG, "Expense_Add - receiverIntent - Category not found in adapter: " + category.getTitle());
                                    }
                                    break;
                                }
                            }
                        } else {
                            Log.e(TAG, "Expense_Add - receiverIntent - Transaction is null");
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Expense_Add - receiverIntent - " + errorMessage);
                    }
                });
            } else {
                transaction = new Transaction();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Expense_Add - receiverIntent - " + ex.getMessage());
        }
    }

    private void bindingData() {
        try {
            myFbContext = new MyFbContext(); // Khởi tạo MyFbContext
            myFbContext.getAllCategoryByType("EXPENSE", new MyFbContext.CategoryCallback() {
                @Override
                public void onSuccess(List<Category> fetchedCategories) {
                    categories = fetchedCategories; // Lưu danh sách danh mục
                    List<String> titleCategories = new ArrayList<>();
                    for (Category item : categories) {
                        titleCategories.add(item.getTitle());
                    }
                    if (titleCategories.isEmpty()) {
                        tvNoCategories.setText("Chưa tạo danh mục cho thu nhập");
                    } else {
                        tvNoCategories.setText("");
                    }
                    adapter = new ArrayAdapter<>(Expense_Add.this, android.R.layout.simple_spinner_item, titleCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e(TAG, "InCome_Add - bindingData - " + errorMessage);
                    tvNoCategories.setText("Lỗi khi tải danh mục: " + errorMessage); // Hiển thị lỗi nếu có
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "InCome_Add - bindingData - " + ex.getMessage());
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
        try {
            if (transaction.getId() != 0) {
                Log.d(TAG, "Transaction ID: " + transaction.getId() + " - Đang cập nhật giao dịch.");
                onUpdateTransaction(); // Cập nhật nếu có ID
                return;
            } else {
                Log.d(TAG, "Transaction ID: " + transaction.getId() + " - Không có ID, không thực hiện cập nhật.");
            }


            if (!checkValid()) {
                tvError.setText("Không được để trường nào trống");
            } else {
                Transaction newTransaction = new Transaction(); // Khởi tạo giao dịch mới
                Category category = getCategoryByTitle(spinnerCategory.getSelectedItem().toString());
                newTransaction.setCategory(String.valueOf(category.getId()));
                newTransaction.setPrice(edtExpenseMoney.getText().toString());
                newTransaction.setTitle(edtExpenseDes.getText().toString());
                newTransaction.setIsIncome("EXPENSE");

                // Lấy ngày hiện tại
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
                String currentDateString = dateFormat.format(currentDate);
                newTransaction.setCreateDate(currentDateString);

                // Gọi phương thức thêm giao dịch
                myFbContext.addTransaction(newTransaction, new MyFbContext.AddTransactionCallback() {
                    @Override
                    public void onSuccess(String transactionId) {
                        Toast.makeText(Expense_Add.this, "Thêm thành công với ID: " + transactionId, Toast.LENGTH_SHORT).show();
                        resetInput(); // Đặt lại input
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(Expense_Add.this, "Thêm thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ex) {
            Log.e(TAG, "Expense_Add - OnSaveClick - " + ex.getMessage());
        }
    }

    private void onUpdateTransaction() {
        try {
            if (!checkValid()) {
                tvError.setText("Không được để trường nào trống");
            } else {
                // Lấy danh mục từ spinner
                Category category = getCategoryByTitle(spinnerCategory.getSelectedItem().toString());
                transaction.setCategory(String.valueOf(category.getId()));
                transaction.setPrice(String.valueOf(edtExpenseMoney.getText()));
                transaction.setTitle(edtExpenseDes.getText().toString());
                transaction.setIsIncome("EXPENSE");

                // Gọi phương thức cập nhật và xử lý kết quả
                myFbContext.updateTransaction(transaction, new MyFbContext.UpdateTransactionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(Expense_Add.this, "Update thành công", Toast.LENGTH_SHORT).show();
                        resetInput(); // Đặt lại dữ liệu đầu vào
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(Expense_Add.this, "Update thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ex) {
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