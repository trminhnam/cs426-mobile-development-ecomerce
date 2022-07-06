package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private TextView fullNameTextView, shopNameTextView, emailTextView, addressTextView, phoneNumberTextView;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    private static SellerProfileFragment INSTANCE = null;

    public static SellerProfileFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SellerProfileFragment();
        }
        return INSTANCE;
    }

    public static SellerProfileFragment newInstance(String param1, String param2) {
        return new SellerProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        fullNameTextView = view.findViewById(R.id.sellerFullnameProfileTextView);
        shopNameTextView = view.findViewById(R.id.sellerShopnameProfileTextView);
        emailTextView = view.findViewById(R.id.sellerEmailProfileTẽtView);
        addressTextView = view.findViewById(R.id.sellerAddressProfileTextView);
        phoneNumberTextView = view.findViewById(R.id.sellerPhoneNumberProfileTextView);

        loadSellerProfileFromFirebase();

        return view;
    }

    private void loadSellerProfileFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String fullname = ""+ds.child("fullname").getValue();
                            String email = ""+ds.child("email").getValue();
                            String address = ""+ds.child("address").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String phoneNumber = ""+ds.child("phoneNumber").getValue();

                            fullNameTextView.setText(fullname);
                            emailTextView.setText(email);
                            addressTextView.setText(address);
                            shopNameTextView.setText(shopName);
                            phoneNumberTextView.setText(phoneNumber);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}