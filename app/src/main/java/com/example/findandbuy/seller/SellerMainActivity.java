package com.example.findandbuy.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.fragment.CustomMapFragment;
import com.example.findandbuy.fragment.SellerAddItemFragment;
import com.example.findandbuy.fragment.SellerItemListFragment;
import com.example.findandbuy.fragment.SellerProfileFragment;
import com.example.findandbuy.fragment.UserGameFragment;
import com.example.findandbuy.fragment.UserProfileFragment;
import com.example.findandbuy.fragment.UserShopFragment;
import com.example.findandbuy.fragment.UserShoppingCartFragment;
import com.example.findandbuy.navigation.BottomNavigationBehavior;
import com.example.findandbuy.user.UserMainActivity;
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
import java.util.Objects;

public class SellerMainActivity extends AppCompatActivity {


    private static Fragment fragment = null;

    private MaterialToolbar toolbar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.seller_frame_container, SellerItemListFragment.class, null)
                    .commit();
        }

        // set toolbar
        initToolbar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.seller_navigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();

        layoutParams.setBehavior(new BottomNavigationBehavior());
        firebaseAuth = FirebaseAuth.getInstance();

        switchFragment(0);

        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                //Delete all fragment in container before switch tab
//                for (Fragment fragmentInContainer : getSupportFragmentManager().getFragments()) {
//                    if (fragmentInContainer != null) {
//                        getSupportFragmentManager().beginTransaction().remove(fragmentInContainer).commit();
//                    }
//                }
                switch (item.getItemId()) {
                    case R.id.navigation_item_list:
//                        Log.d("shop", "onNavigationItemSelected: Im shop");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.seller_frame_container, SellerItemListFragment.class, null)
//                                    .commit();
//                        }
                        fragment = null;
                        switchFragment(0);
                        return true;
                    case R.id.navigation_add_item:
//                        Log.d("game", "onNavigationItemSelected: Im game");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.seller_frame_container, SellerAddItemFragment.class, null)
//                                    .commit();
//                        }
                        fragment = null;
                        startActivity(new Intent(SellerMainActivity.this, SellerAddItem.class));
                        return true;
//                    case R.id.navigation_modify_items:
//                        Log.d("card", "onNavigationItemSelected: Im card");
////                        if (savedInstanceState == null) {
////                            getSupportFragmentManager().beginTransaction()
////                                    .setReorderingAllowed(true)
////                                    .add(R.id.seller_frame_container, UserShoppingCartFragment.class, null)
////                                    .commit();
////                        }
//                        return true;
                    case R.id.navigation_seller_profile:
//                        Log.d("profile", "onNavigationItemSelected: Im profile");
//                        if (savedInstanceState == null) {
//                            getSupportFragmentManager().beginTransaction()
//                                    .setReorderingAllowed(true)
//                                    .add(R.id.seller_frame_container, SellerProfileFragment.class, null)
//                                    .commit();
//                        }
                        fragment = null;
                        switchFragment(1);
                        return true;
                }
                return true;
            }
        });
    }

    private void initToolbar() {
        toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logoutBtn){
                    //Add logout here
                    new AlertDialog.Builder(SellerMainActivity.this).setIcon(R.drawable.ic_logout_black)
                            .setTitle("Logging out").setMessage("Are you sure you want to logging out?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logOutSeller();
                                    Toast.makeText(SellerMainActivity.this, "Logged out",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", null).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.seller_frame_container, getCurrentFragment(index));
        transaction.commit();
    }

    private Fragment getCurrentFragment(int index) {
        switch (index) {
            case 0: // profile
                fragment = SellerItemListFragment.getInstance();
                break;
            case 1: // shop
                fragment = SellerProfileFragment.getInstance();
        }
        return fragment;
    }

    private void logOutSeller() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid()))
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        Toast.makeText(SellerMainActivity.this, "" + "Signed out successfully", Toast.LENGTH_SHORT).show();
                        returnToLoginScreen();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellerMainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d("log out", "logOutUser: log out");
    }

    private void returnToLoginScreen() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(SellerMainActivity.this, LoginActivity.class));
            finish();
        }
        else {
            return;
        }
    }

}