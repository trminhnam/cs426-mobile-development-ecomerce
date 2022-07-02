package com.example.findandbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findandbuy.dataStructure.Item;
import com.example.findandbuy.dataStructure.Store;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userName, password;
    private Button loginBtn;
    private TextView noAccountTv;
    private Database database;

    ArrayList<Item> items = new ArrayList<>();

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

        // test
        database = Database.getInstance();
        Item item = new Item().addItem(1, "test", 12,
                "a", 12, 323, "tess");
        items.add(item);

        Store store = new Store().addStore(12, "aaa", items, "aa");

        database.registerSeller("Test", "test", "test", store);
        database.pushToFirebase();
    }
}