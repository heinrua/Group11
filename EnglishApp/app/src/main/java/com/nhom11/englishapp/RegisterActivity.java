package com.nhom11.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText fullName,email,password,phone;
    Button registerBtn;
    TextView goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isadminbox, isuserbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        isadminbox = findViewById(R.id.isAdmin);
        isuserbox = findViewById(R.id.isUser);

        //checkboxies logics
        isuserbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isadminbox.setChecked(false);
                }
            }
        });
        isadminbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isuserbox.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                //checkbox validation
                if(!(isadminbox.isChecked() || isuserbox.isChecked())){
                    Toast.makeText(RegisterActivity.this, "select the account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    //start the registration process
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullName.getText().toString());
                            userInfo.put("Email", email.getText().toString());
                            userInfo.put("PhoneNumber", phone.getText().toString());
                            userInfo.put("User_id",phone.getText().toString());

                            //specify if user is admin
//                            userInfo.put("isUser", "1");
                            if(isadminbox.isChecked()){
                                userInfo.put("isAdmin", "1");
                            }
                            if(isuserbox.isChecked()){
                                userInfo.put("isUser", "1");
                            }

                            df.set(userInfo);
                            if(isadminbox.isChecked()){
                                startActivity((new Intent(getApplicationContext(), MainActivityAdmin.class)));
                                finish();
                            }
                            if(isuserbox.isChecked()){
                                startActivity((new Intent(getApplicationContext(), MainActivity.class)));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
}


