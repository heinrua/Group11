package com.nhom11.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button loginBtn;
    TextView gotoRegister, forgotPass;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(email);
                checkField(password);
                Log.d("TAG", "onClick: " + email.getText().toString());
                if(valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        forgotPass = findViewById(R.id.txtquenMK);
        forgotPass.setOnClickListener(v -> {
            fAuth.sendPasswordResetEmail(email.getText().toString());
            showResetPasswordDialog();
        } );

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
    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //ectract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                if(documentSnapshot.getString("isAdmin") != null){
                    //user is admin
                    startActivity(new Intent(getApplicationContext(), MainActivityAdmin.class));
                    finish();
                }
                if(documentSnapshot.getString("isUser") != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isAdmin") != null){
                        startActivity(new Intent(getApplicationContext(), MainActivityAdmin.class));
                        finish();
                    }
                    if(documentSnapshot.getString("isUser") != null){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        }
    }
}
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class LoginActivity extends AppCompatActivity {
//    EditText email,password;
//    Button loginBtn,gotoRegister;
//    boolean valid = true;
//    FirebaseAuth fAuth;
//    FirebaseFirestore fstore;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        fAuth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//
//        email = findViewById(R.id.loginEmail);
//        password = findViewById(R.id.loginPassword);
//        loginBtn = findViewById(R.id.loginBtn);
//        gotoRegister = findViewById(R.id.gotoRegister);
//
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkField(email);
//                checkField(password);
//                Log.d("TAG", "onClick" + email.getText().toString());
//
//                if(valid){
//                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            Toast.makeText(LoginActivity.this, "Loged in successfully", Toast.LENGTH_SHORT).show();
//                            checkUserAccesslevel(authResult.getUser().getUid());
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//
//        gotoRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void checkUserAccesslevel(String uid) {
//        DocumentReference df = fstore.collection("Users").document(uid);
//        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Log.d("TAG", "Onsuccess"+documentSnapshot.getData());
//                if(documentSnapshot.getString("isAdmin") !=null ){
//                    startActivity(new Intent(getApplicationContext(), UploadActivity.class));
//                    finish();
//                }
//                if(documentSnapshot.getString("isUser") !=null ) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
//                }
//            }
//        });
//    }
//
//    public boolean checkField(EditText textField){
//        if(textField.getText().toString().isEmpty()){
//            textField.setError("Error");
//            valid = false;
//        }else {
//            valid = true;
//        }
//
//        return valid;
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if(documentSnapshot.getString("is admin") != null){
//                        startActivity(new Intent(getApplicationContext(), UploadActivity.class));
//                        finish();
//                    }
//                    if (documentSnapshot.get("is user") != null){
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    FirebaseAuth.getInstance().signOut();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
//                }
//            });
//        }
//    }
//}

