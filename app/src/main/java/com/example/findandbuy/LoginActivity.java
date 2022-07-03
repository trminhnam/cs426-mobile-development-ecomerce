package com.example.findandbuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.seller.SellerMainActivity;
import com.example.findandbuy.user.RegisterUserActivity;
import com.example.findandbuy.user.UserMainActivity;

import com.example.findandbuy.dataStructure.Item;
import com.example.findandbuy.dataStructure.Store;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private Database database;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
            }
        });

        //Test the login to user main activity
        //admin have form: abc@admin.com
        //user have form: abc@user.com
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("check", emailEditText.getText().toString().trim());
//                if (emailEditText.getText().toString().trim().matches(".*@admin.com$")){
//                    startActivity(new Intent(LoginActivity.this, SellerMainActivity.class));
//                } else if (emailEditText.getText().toString().trim().matches(".*@user.com$")){
//                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
//                    Log.d("check", emailEditText.toString());
//                } else {
//                    Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
//                }
                loginUser();

            }
        });
        
        // test
//        HashMap<String, Item> items = new HashMap<>();
//        database = Database.getInstance();
//        Item item = new Item().addItem(1, "Iphone Note 10", 12,
//                "Phone", 12, 323, "Iphone Android System");
//        items.put("12233", item);
//
//        Store store = new Store().addStore(12, "AppStore", items, "Only Samsung");
//
//        database.registerSeller("seller0", "seller.0", "abcd", store);
//        database.registerSeller("seller1", "seller.1", "abcd", store);
//        database.registerSeller("seller2", "seller.2", "abcd", store);
//        database.registerSeller("seller3", "seller.3", "abcd", store);

    }

    private String email, password;
    private void loginUser() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Wrong email format", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (password.length() < 6){
            Toast.makeText(this, "Password length must be longer than 5", Toast.LENGTH_SHORT).show();
            return;
        }
        
        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeMeOnline();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeMeOnline() {
        // after logging in, make user online
        progressDialog.setMessage("Checking user status");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "true");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        checkUserType();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserType() {
        // start main screen of user or seller
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            String fullname = ""+ds.child("fullname").getValue();
                            if (accountType.equals("Seller")){
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, SellerMainActivity.class);
                                intent.putExtra("fullname", fullname);

                                startActivity(intent);
                                finish();
                            }
                            else{
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                intent.putExtra("fullname", fullname);

                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}