package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.hungnm.assigmentfinal.Adapter.RecylerViewAdapter;
import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
import com.fpt.hungnm.assigmentfinal.Model.Category;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainCategory extends AppCompatActivity implements RecylerViewAdapter.ItemListener {
    private static final String PATTERN = "yyyy-MM-dd";
    private RecylerViewAdapter adapter;
    private RecyclerView recyclerView;
    private static final String DATABASE_NAME = "MoneyManage.db";
    private static final String TAG ="HungnmError";
    private EditText edtTitle;
    private RadioGroup radioGroup;
    private RadioButton rbIncome;
    private RadioButton rbExpense;
    private Button btnAdd;
    private Button btnUpdate;
    private Button btnDelete;
    private TextView tvError;

    private ImageView btnBack;

    private RecylerViewAdapter.ItemListener listener;

    private Category categoryClicked = new Category();

    private String pageBack;

    private Button viewAll;

    private MyFbContext myFbContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_category);
            bindingView();
            bindingAction();
            setAdapter();
            reciverIntent();
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - onCreate - " + ex.getMessage());
        }
    }

    private void reciverIntent() {
        try{
            Intent intent = getIntent();
            if(intent.hasExtra("type")){
                String type = intent.getStringExtra("type");
                if(type.equals("INCOME")){
                    rbIncome.setChecked(true);
                    pageBack = "INCOME";
                }else{
                    rbExpense.setChecked(true);
                    pageBack = "EXPENSE";
                }

            }
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - reciverIntent - " + ex.getMessage());
        }
    }

    private void reLoad() {
        try {
            edtTitle.setText("");
            radioGroup.clearCheck();
            btnAdd.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            tvError.setText("");

            // Gọi phương thức từ myFbContext để lấy danh sách danh mục
            myFbContext.getAllCategory(new MyFbContext.CategoryCallback() {
                @Override
                public void onSuccess(List<Category> updatedList) {
                    // Cập nhật danh sách trong adapter và thông báo thay đổi
                    adapter.setList(updatedList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e(TAG, "MainCategory - reLoad - " + errorMessage);
                    tvError.setText("Lỗi: " + errorMessage);
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MainCategory - reLoad - " + ex.getMessage());
        }
    }


    private void setAdapter() {
        try {
            // Gọi phương thức từ myFbContext để lấy danh sách danh mục
            myFbContext.getAllCategory(new MyFbContext.CategoryCallback() {
                @Override
                public void onSuccess(List<Category> list) {
                    // Thiết lập listener và danh sách cho adapter
                    adapter.setItemListener(MainCategory.this);
                    adapter.setList(list);

                    // Thiết lập layout manager và adapter cho RecyclerView
                    LinearLayoutManager manager = new LinearLayoutManager(MainCategory.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e(TAG, "MainCategory - setAdapter - " + errorMessage);
                    // Bạn có thể hiển thị thông báo lỗi cho người dùng nếu cần
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "MainCategory - setAdapter - " + ex.getMessage());
        }
    }


    private void bindingAction() {
        try{
            btnAdd.setOnClickListener(this::onAddClick);
            btnUpdate.setOnClickListener(this::onUpdateClick);
            btnDelete.setOnClickListener(this::onDeleteClick);
            btnBack.setOnClickListener(this::onBackClick);
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - bindingAction - " + ex.getMessage());
        }
    }

    private void onBackClick(View view) {
        try{
            if(pageBack =="INCOME"){
                pageBack ="";
                Intent i = new Intent(this, InCome_Add.class);
                startActivity(i);
            }else{
                pageBack ="";
                Intent i = new Intent(this, Expense_Add.class);
                startActivity(i);
            }
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - bindingAction - " + ex.getMessage());
        }
    }

    private void onDeleteClick(View view) {
        try {
            if (categoryClicked.getIsIncome() != null && !categoryClicked.getIsIncome().equals("")) {
                myFbContext.deleteCategory(categoryClicked.getId(), new MyFbContext.DeleteCategoryCallback() {
                    @Override
                    public void onSuccess() {
                        // Cập nhật lại danh sách sau khi xóa thành công
                        reLoad();
                        categoryClicked = new Category(); // Đặt lại danh mục đã chọn
                        Toast.makeText(MainCategory.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Hiển thị thông báo lỗi nếu việc xóa thất bại
                        Toast.makeText(MainCategory.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Chưa chọn danh mục xóa", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e(TAG, "MainCategory - onDeleteClick - " + ex.getMessage());
        }
    }


    private void onUpdateClick(View view) {
        try {
            if (categoryClicked.getIsIncome() != null && !categoryClicked.getIsIncome().equals("")) {
                Category category = getCategory();

                    if (category != null) {
                        // Cập nhật thuộc tính danh mục được chọn
                        categoryClicked.setTitle(category.getTitle());
                        categoryClicked.setIsIncome(category.getIsIncome());

                        // Gọi phương thức cập nhật danh mục trong Firebase
                        myFbContext.updateCategory(categoryClicked, new MyFbContext.UpdateCategoryCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(MainCategory.this, "Cập nhật dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                reLoad();
                                categoryClicked = new Category(); // Reset categoryClicked
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(MainCategory.this, "Có lỗi trong quá trình xử lý: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
                    }
            } else {
                Toast.makeText(this, "Chưa chọn danh mục sửa", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Log.e(TAG, "MainCategory - onUpdateClick - " + ex.getMessage());
        }
    }

    private Category getCategory(){
        try{
            Category category = new Category();
            String title = edtTitle.getText().toString();
            if(title == null || title.equals("")){
                tvError.setText("Tên danh mục không hợp lệ");
            }else{
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId == -1){
                    tvError.setText("Chưa chọn loại danh mục");
                }else{
                    category.setTitle(title);
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String type = selectedRadioButton.getText().toString();
                    if(type.equals("Tiền vào")){
                        category.setIsIncome("INCOME");
                    }else{
                        category.setIsIncome("EXPENSE");
                    }
                    tvError.setText("");
                }
            }
            if(tvError.getText().toString().isEmpty()){
                return category;
            }
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - onUpdateClick - " + ex.getMessage());
        }
        return null;
    }

    private void onAddClick(View view) {
        try {
            Category category = getCategory();
            if (category != null) {

                // Lấy ngày hiện tại
                Date currentDate = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
                String currentDateString = dateFormat.format(currentDate);
                category.setCreateDate(currentDateString);

                // Thêm danh mục vào Firebase
                myFbContext.addCategory(category, new MyFbContext.AddCategoryCallback() {
                    @Override
                    public void onSuccess(int categoryId) {
                        Toast.makeText(MainCategory.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        reLoad(); // Tải lại danh sách danh mục
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(MainCategory.this, "Có lỗi trong quá trình thêm: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e(TAG, "MainCategory - onAddClick - " + ex.getMessage());
        }
    }


    private void bindingView() {
        try{
            viewAll = findViewById(R.id.viewALl);
            tvError = findViewById(R.id.tv_category_error);
            btnAdd = findViewById(R.id.btn_add_category);
            btnDelete = findViewById(R.id.btn_clear_category);
            btnUpdate = findViewById(R.id.btn_update_category);
            rbIncome = findViewById(R.id.rb_income);
            rbExpense = findViewById(R.id.rb_expense);
            radioGroup = findViewById(R.id.rdogr);
            edtTitle = findViewById(R.id.edt_category_title);
            myFbContext = new MyFbContext();
            recyclerView = findViewById(R.id.rcv_category);
            adapter = new RecylerViewAdapter();
            btnBack = findViewById(R.id.img_categoryback);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - bindingView - " + ex.getMessage());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        try{
            categoryClicked = adapter.getCategory(position);
            edtTitle.setText(categoryClicked.getTitle());
            if(categoryClicked.getIsIncome().equals("INCOME")){
                rbIncome.setChecked(true);
            }else {
                rbExpense.setChecked(true);
            }
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
            btnAdd.setEnabled(false);
        }catch (Exception ex){
            Log.e(TAG, "MainCategory - onItemClick - " + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }
}