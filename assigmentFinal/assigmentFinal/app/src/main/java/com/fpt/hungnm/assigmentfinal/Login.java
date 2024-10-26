package com.fpt.hungnm.assigmentfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpt.hungnm.assigmentfinal.Dal.MyFbContext; // Sử dụng MyFbContext
import com.fpt.hungnm.assigmentfinal.Model.User; // Đảm bảo bạn đã có lớp User

public class Login extends AppCompatActivity {
    private Button btnRegister;
    private Button btnLogin;
    private EditText edtTk;
    private EditText edtMk;
    private MyFbContext fbContext; // Thay đổi thành MyFbContext
    private TextView tvError;

    void bindingAction() {
        btnLogin.setOnClickListener(this::onLogin);
        btnRegister.setOnClickListener(this::onRegister);
    }

    private void onRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void onLogin(View view) {
        String username = edtTk.getText().toString();
        String password = edtMk.getText().toString();

        fbContext.login(username, password, new MyFbContext.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
            }

            @Override
            public void onLoginFailed(String message) {
                tvError.setText(message);
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    void binding() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtTk = findViewById(R.id.edtTk);
        edtMk = findViewById(R.id.edtMk);
        tvError = findViewById(R.id.tvError);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding();
        bindingAction();
        fbContext = new MyFbContext(); // Khởi tạo MyFbContext
    }
}
