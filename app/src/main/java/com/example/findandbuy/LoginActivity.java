package com.example.findandbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.seller.SellerMainActivity;
import com.example.findandbuy.user.RegisterUserActivity;
import com.example.findandbuy.user.UserMainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userName, password;
    private Button loginBtn;
    private TextView noAccountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        userName = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);
        loginBtn = findViewById(R.id.login_btn);
        noAccountTv = findViewById(R.id.no_account);

        noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
            }
        });

        //Test the login to user main activity
        //admin have form: abc@admin.com
        //user have form: abc@user.com
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check", userName.getText().toString().trim());
                if (userName.getText().toString().trim().matches(".*@admin.com$")){
                    startActivity(new Intent(LoginActivity.this, SellerMainActivity.class));
                } else if (userName.getText().toString().trim().matches(".*@user.com$")){
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    Log.d("check", userName.toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}