package com.nhom11.englishapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignIn extends AppCompatActivity {
    private TextView txtDangKy, txtQuenMK;
    private Button btnDangNhap;
    private EditText edtpassword, edemail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        Intent intent = new Intent(SignIn.this, SignUp.class);

        txtDangKy = findViewById(R.id.txtDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edemail = findViewById(R.id.edtMailDN);
        edtpassword = findViewById(R.id.edtMatKhauDN);
        txtQuenMK = findViewById(R.id.txtquenMK);
        txtQuenMK.setOnClickListener(v -> {
            auth.sendPasswordResetEmail(edemail.getText().toString());
            showResetPasswordDialog();
        });
        txtDangKy.setOnClickListener(v -> startActivity(intent));
        auth = FirebaseAuth.getInstance();
        btnDangNhap.setOnClickListener(v -> {

            String email = edemail.getText().toString();
            String password = edtpassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignIn.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private void showResetPasswordDialog() {
        // Tạo một builder để xây dựng dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Thiết lập tiêu đề, nội dung và nút "OK"
        builder.setMessage("Đã gửi mail reset mật khẩu.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Xử lý khi người dùng bấm nút "OK"
                });
        // Hiện thị dialog
        builder.show();
    }
}