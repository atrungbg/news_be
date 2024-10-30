package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptApi;
import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptRequest;
import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptResponse;
import com.fpt.hungnm.assigmentfinal.ChatGptApi.RetrofitClientInstance;
import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
import com.fpt.hungnm.assigmentfinal.Model.Budget;
import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

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

    private MyFbContext dbContext;

    private TextView tvError;

    List<Transaction> list;
    FloatingActionButton fab;
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
        try {
            // Lấy dữ liệu từ Firebase
            dbContext.getAllTransaction(new MyFbContext.FirebaseCallback() {
                @Override
                public void onSuccess(List<Transaction> transactions) {
                    list = transactions; // Gán danh sách giao dịch

                    // Lấy danh mục từ Firebase
                    dbContext.getAllCategories(new MyFbContext.FirebaseCategoryCallback() {
                        @Override
                        public void onSuccess(List<Category> categoriesResult) {
                            categories = categoriesResult;
                            List<String> titleCategories = new ArrayList<>();
                            titleCategories.add("ALL"); // Thêm tùy chọn 'ALL'

                            // Thêm tiêu đề danh mục vào danh sách
                            for (Category item : categories) {
                                titleCategories.add(item.getTitle());
                            }

                            // Thiết lập adapter cho Spinner
                            spCategory.setAdapter(new ArrayAdapter<>(
                                    MainTransaction.this,
                                    android.R.layout.simple_spinner_item,
                                    titleCategories
                            ));

                            // Gán sự kiện click cho các EditText ngày tháng
                            edtStartDate.setOnClickListener(MainTransaction.this);
                            edtToDate.setOnClickListener(MainTransaction.this);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, "bindingData - getAllCategories failed: " + e.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "bindingData - getAllTransactions failed: " + e.getMessage());
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "bindingData - Error: " + ex.getMessage());
        }
    }


    private void onSearch(View view) {
        try {
            String toDate = edtToDate.getText().toString();
            String fromDate = edtStartDate.getText().toString();

            // Log thông tin từ và đến ngày
            Log.d(TAG, "FromDate: " + fromDate + ", ToDate: " + toDate);

            // Kiểm tra điều kiện ngày bắt đầu phải nhỏ hơn ngày kết thúc
            if (!toDate.isEmpty() && !fromDate.isEmpty()) {
                boolean check = compareDates(fromDate, toDate);
                if (!check) {
                    tvError.setText("Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                    return;
                }
            }

            String title = edtTitle.getText().toString();
            String categoryId = "ALL";
            Category category = getCategoryByTitle(spCategory.getSelectedItem().toString());

            if (category != null) {
                categoryId = String.valueOf(category.getId());
            }

            // Log thông tin tiêu đề và categoryId
            Log.d(TAG, "Title: " + title + ", CategoryId: " + categoryId);

            // Gọi hàm tìm kiếm dữ liệu từ Firebase
            dbContext.getTransactionSearch(title, categoryId, fromDate, toDate, new MyFbContext.FirebaseTransactionCallback() {
                @Override
                public void onSuccess(List<Transaction> transactions) {
                    list = transactions;
                    setAdapter();  // Cập nhật adapter sau khi có dữ liệu
                    tvError.setText("");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "MainTransaction - onSearch - " + e.getMessage());
                    tvError.setText("Lỗi khi tìm kiếm dữ liệu.");
                }
            });

        } catch (Exception ex) {
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
        try {
            dbContext = new MyFbContext();

            btnHome.setOnClickListener(this::goToHome);
            btnAccount.setOnClickListener(this::goToAccount);
            btnSearch.setOnClickListener(this::onSearch);

            // Đặt listener cho fab
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFinancialAdviceDialog(); // Gọi phương thức để hiển thị lời khuyên tài chính
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MainTransaction - bindingAction - " + ex.getMessage());
        }
    }

    private void showFinancialAdviceDialog() {
        ChatGptApi apiService = RetrofitClientInstance.getRetrofitInstance().create(ChatGptApi.class);

        // Gọi phương thức để lấy báo cáo
        dbContext.getAllTransactionsByString(new MyFbContext.OnTransactionFetchListener1() {
            @Override
            public void onSuccess(String report) {
                // Tạo chuỗi thông điệp từ báo cáo
                String userMessage = "Đây là thông tin tài chính của tôi: " + report + ". Hãy đọc chi tiết về tình hình doanh số với con số cụ thể và đưa ra về lời khuyên tài chính";

                // Tạo danh sách message cho ChatGPT
                List<ChatGptRequest.Message> messages = new ArrayList<>();
                messages.add(new ChatGptRequest.Message("user", userMessage));

                // Tạo yêu cầu cho ChatGPT
                ChatGptRequest request = new ChatGptRequest("gpt-3.5-turbo", messages);

                // Gọi API
                Call<ChatGptResponse> call = apiService.getAllTransactionsByString(request);
                call.enqueue(new Callback<ChatGptResponse>() {
                    @Override
                    public void onResponse(Call<ChatGptResponse> call, Response<ChatGptResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Lấy nội dung lời khuyên
                            String advice = response.body().getChoices().get(0).getMessage().getContent();

                            // Hiển thị lời khuyên trong hộp thoại
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainTransaction.this);
                            builder.setTitle("Lời khuyên tài chính");
                            builder.setMessage(advice);
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        } else {
                            // Xử lý lỗi nếu cần
                            showErrorDialog("Không thể lấy lời khuyên tài chính");
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatGptResponse> call, Throwable t) {
                        // Xử lý lỗi khi gọi API
                        showErrorDialog("Lỗi mạng: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                showErrorDialog("Lỗi: " + e.getMessage());
            }
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
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
        try {
            fab = findViewById(R.id.fab_financial_advice); // Sửa lại dòng này
            rcvTransaction = findViewById(R.id.rcv_main_transaction);
            btnHome = findViewById(R.id.img_transaction_btnHome);
            btnAccount = findViewById(R.id.img_transaction_btnAccount);
            btnTransaction = findViewById(R.id.img_transaction_btnTransaction);
            edtTitle = findViewById(R.id.edt_transaction_title);
            spCategory = findViewById(R.id.sp_transation_category);
            edtStartDate = findViewById(R.id.edt_transaction_fromDate);
            edtToDate = findViewById(R.id.edt_transaction_toDate);
            btnSearch = findViewById(R.id.btn_transaction_search);
            tvError = findViewById(R.id.tv_transaction_error);
        } catch (Exception ex) {
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