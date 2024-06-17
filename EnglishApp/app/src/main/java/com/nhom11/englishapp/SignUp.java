package com.nhom11.englishapp;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {
    private TextView txtDangnhap;
    private EditText edMatKhau, edXacNhanMatKhau, edEmail;
    private Button btnDangKy;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        txtDangnhap = findViewById(R.id.txtDangnhap);
        edMatKhau = findViewById(R.id.edtMatKhauDK);
        edEmail = findViewById(R.id.edtEmail);
        edXacNhanMatKhau = findViewById(R.id.edtXacnhanmk);
        btnDangKy = findViewById(R.id.btnDangKy);
        // chuyển sang màn hình chính
        Intent myIntent = new Intent(this, SignIn.class);
        txtDangnhap.setOnClickListener(v -> startActivity(myIntent));

        btnDangKy.setOnClickListener(v -> {
            String matkhau = edMatKhau.getText().toString();
            String xacnhanmatkhau = edXacNhanMatKhau.getText().toString();
            String email = edEmail.getText().toString();

            if (email.isEmpty() || matkhau.isEmpty() || xacnhanmatkhau.isEmpty()) {
                Toast.makeText(this, "Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
            else if (!matkhau.equals(xacnhanmatkhau)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                edMatKhau.setText("");
                edXacNhanMatKhau.setText("");
            }
            else {
                auth.createUserWithEmailAndPassword(email,matkhau).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("password", matkhau);
                            db.collection("users")
                                    .document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("FirebaseAuth", "User added to Firestore");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("FirebaseAuth", "Error adding user to Firestore", e);
                                        }
                                    });
                            startActivity(myIntent);

                        }
                        else {
                            Toast.makeText(SignUp.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }}
        );




    }
}