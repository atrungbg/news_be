package com.fpt.hungnm.assigmentfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext;
import com.fpt.hungnm.assigmentfinal.Model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister, btnBack;
    private MyFbContext dbContext; // Thêm tham chiếu tới MyFbContext

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindingView();
        bindingAction();

        dbContext = new MyFbContext(); // Khởi tạo MyFbContext

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void bindingView() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
    }

    private void bindingAction() {
        btnRegister.setOnClickListener(this::registerUser);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser(View view) {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký người dùng thông qua MyFbContext
        dbContext.register(username, email, password, new MyFbContext.RegisterCallback() {
            @Override
            public void onRegisterSuccess() {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onRegisterFailed(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
