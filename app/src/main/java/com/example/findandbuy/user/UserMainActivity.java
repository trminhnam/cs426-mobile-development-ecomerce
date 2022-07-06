package com.example.findandbuy.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.fragment.CustomMapFragment;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class UserMainActivity extends AppCompatActivity {

    private static Fragment fragment;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    // get the current fragment
    private Fragment getCurrentFragment(int index) {
        switch (index) {
            case 0: // profile
                fragment = UserProfileFragment.getInstance();
                break;
            case 1: // shop
                fragment = UserShopFragment.getInstance();
                break;
            case 2: // game
                fragment = UserGameFragment.getInstance();
                break;
            case 3: // shopping cart
                fragment = UserShoppingCartFragment.getInstance();
                break;
            case 4: // map
                fragment = CustomMapFragment.getInstance();
                break;
        }
        return fragment;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        // set toolbar
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        // set the bottom navigation bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // set the default fragment
        switchFragment(1);

        //set listener for toolbar
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logoutBtn){
                //Add logout here
                new AlertDialog.Builder(UserMainActivity.this).setIcon(R.drawable.ic_logout_black)
                        .setTitle("Logging out").setMessage("Are you sure you want to logging out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            logOutUser();
                            Toast.makeText(UserMainActivity.this, "Logged out",Toast.LENGTH_SHORT).show();
                        }).setNegativeButton("No", null).show();
                return true;
            }
            return false;
        });
        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    switchFragment(0);
                    return true;
                case R.id.navigation_shop:
                    switchFragment(1);
                    return true;
                case R.id.navigation_game:
                    switchFragment(2);
                    return true;
                case R.id.navigation_cart:
                    switchFragment(3);
                    return true;
                case R.id.navigation_map:
                    switchFragment(4);
                    return true;
            }
            return false;
        });
    }

    private void logOutUser() {
        progressDialog.setMessage("Logging out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener(unused -> {
                    firebaseAuth.signOut();
                    checkUser();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(UserMainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Log.d("log out", "logOutUser: log out");
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfor();
        }
    }

    private void loadMyInfor() {
        Log.d("load infor", "loadMyInfor: test");
    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, getCurrentFragment(index));
        transaction.commit();
    }
}