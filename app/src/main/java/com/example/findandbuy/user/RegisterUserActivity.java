package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.seller.RegisterSellerActivity;
import com.example.findandbuy.seller.SellerMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {

    private ImageButton backButton;

    private EditText nameEt, phoneEt, emailEt, passwordEt, confirmPasswordEt;
    private Button registerButton;

    private TextView registerSellerTv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        backButton = findViewById(R.id.backBtn);
        nameEt = findViewById(R.id.fullNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        registerButton = findViewById(R.id.register_btn);
        registerSellerTv = findViewById(R.id.registerSeller);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });


        registerSellerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register seller
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
                finish();
            }
        });
    }

    private String name, phone, email, password, confirmPassword;
    private void inputData() {
        name = nameEt.getText().toString().trim();
        phone = phoneEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();

        if (name.isEmpty()){
            Toast.makeText(this, "Name must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()){
            Toast.makeText(this, "Email must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be longer than 5 chars", Toast.LENGTH_SHORT).show();
            return;
        }

        if (confirmPassword.length() < 6) {
            Toast.makeText(this, "Confirm password must be longer than 5 chars", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creating user account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        saverFirebaseData();
                        Toast.makeText(RegisterUserActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Saving account information.");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        // upload without image
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+firebaseAuth.getUid());
        hashMap.put("fullname", ""+name);
        hashMap.put("email", ""+email);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("accountType", "User");
        hashMap.put("available", "true");
        hashMap.put("bonus", "0");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterUserActivity.this, UserMainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}