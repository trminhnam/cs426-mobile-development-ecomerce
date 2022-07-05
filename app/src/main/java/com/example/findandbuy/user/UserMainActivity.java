package com.example.findandbuy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.findandbuy.Database;
import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.fragment.CustomMapFragment;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserMainActivity extends AppCompatActivity {

    // save all the fragments in this array
    private static Fragment[] fragments;
    private static BottomNavigationView navigation;
    private MaterialToolbar toolbar;
    private CoordinatorLayout.LayoutParams layoutParams;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    // initialize the fragments
    private void initFragments() {
        fragments = new Fragment[5];
        fragments[0] = null;
        fragments[1] = null;
        fragments[2] = null;
        fragments[3] = null;
        fragments[4] = null;
    }

    // get the current fragment
    private Fragment getCurrentFragment(int index) {
        if (fragments[index] == null) {
            switch (index) {
                case 0:
                    fragments[index] = new UserProfileFragment();
                    break;
                case 1:
                    fragments[index] = new UserShopFragment();
                    break;
                case 2:
                    fragments[index] = new UserGameFragment();
                    break;
                case 3:
                    fragments[index] = new UserShoppingCartFragment();
                    break;
                case 4:
                    fragments[index] = new CustomMapFragment();
                    break;
            }
        }
        return fragments[index];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initFragments();

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        // set toolbar
        toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        // set the bottom navigation bar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // set the default fragment
        switchFragment(1);

        //set listener for toolbar
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logoutBtn){
                    //Add logout here
                    new AlertDialog.Builder(UserMainActivity.this).setIcon(R.drawable.ic_logout_white)
                            .setTitle("Logging out").setMessage("Are you sure you want to logging out?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logOutUser();
                                    Toast.makeText(UserMainActivity.this, "Logged out",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", null).show();
                    return true;
                }
                return false;
            }
        });
        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

    }

    private void logOutUser() {
        progressDialog.setMessage("Logging out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UserMainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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