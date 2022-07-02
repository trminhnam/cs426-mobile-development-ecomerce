package com.example.findandbuy.seller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterSellerActivity extends AppCompatActivity {
    private ImageButton backButton;
    private EditText nameEt, phoneEt, shopName, streetEt, districtEt, cityEt, passwordEt,emailEt, completeAddress, confirmPasswordEt;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);
        backButton = findViewById(R.id.backBtn);
        nameEt = findViewById(R.id.fullNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        shopName = findViewById(R.id.shopNameEt);
        streetEt = findViewById(R.id.streetEt);
        districtEt = findViewById(R.id.districtEt);
        cityEt = findViewById(R.id.cityEt);
        completeAddress = findViewById(R.id.completeAddressEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        registerButton = findViewById(R.id.register_btn);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register seller
                inputData();
            }
        });
    }

    private String fullName, shopNames, phoneNum, street, district, city, address, email, password, confirmPassword;
    private void inputData() {
        //input
        fullName = nameEt.getText().toString().trim();
        shopNames = shopName.getText().toString().trim();
        phoneNum = phoneEt.getText().toString().trim();
        street = streetEt.getText().toString().trim();
        district = districtEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        address = completeAddress.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();

        //validate of data
        if (fullName.isEmpty()){
            Toast.makeText(this, "Item name is required ...", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}