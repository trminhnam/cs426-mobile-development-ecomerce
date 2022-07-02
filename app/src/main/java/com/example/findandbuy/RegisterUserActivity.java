package com.example.findandbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.findandbuy.seller.RegisterSellerActivity;

public class RegisterUserActivity extends AppCompatActivity {


    private ImageButton backButton;
    private EditText nameEt, phoneEt, streetEt, districtEt, cityEt, emailEt, passwordEt, confirmPasswordEt;
    private Button registerButton;
    private TextView registerSellerTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        backButton = findViewById(R.id.backBtn);
        nameEt = findViewById(R.id.fullNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        streetEt = findViewById(R.id.streetEt);
        districtEt = findViewById(R.id.districtEt);
        cityEt = findViewById(R.id.cityEt);
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

        registerSellerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register seller
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
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