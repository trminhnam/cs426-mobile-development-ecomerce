package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.ShopAdapter;
import com.example.findandbuy.models.Seller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserShopFragment extends Fragment {

    private static UserShopFragment INSTANCE = null;

    private RecyclerView recyclerView;
    private ArrayList<Seller> sellerArrayList;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public UserShopFragment() {
    }

    public static UserShopFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserShopFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadSellerLists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        progressDialog.setMessage("Loading shops");
        progressDialog.show();
        loadSellerLists();

        View view = inflater.inflate(R.layout.fragment_user_shop, container, false);
        recyclerView = view.findViewById(R.id.shopRv);


        return view;
    }

    private void reOrderShopName() {
        for (int i = 0; i < sellerArrayList.size(); i++) {
            for (int j = 0; j < sellerArrayList.size() - 1; j++) {
                if (sellerArrayList.get(j).getShopName().length() > sellerArrayList.get(j + 1).getShopName().length()) {
                    Seller temp = sellerArrayList.get(j);
                    sellerArrayList.set(j, sellerArrayList.get(j + 1));
                    sellerArrayList.set(j + 1, temp);
                } else if (sellerArrayList.get(j).getShopName().length() == sellerArrayList.get(j + 1).getShopName().length()) {
                    if (sellerArrayList.get(j).getShopName().compareTo(sellerArrayList.get(j + 1).getShopName()) > 0) {
                        Seller temp = sellerArrayList.get(j);
                        sellerArrayList.set(j, sellerArrayList.get(j + 1));
                        sellerArrayList.set(j + 1, temp);
                    }
                }
            }
        }
    }


    private void loadSellerLists() {
        sellerArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sellerArrayList.clear();
                        sellerArrayList = new ArrayList<>();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            Log.d("SELLER_RETRIEVAL", "" + ds.child("email").getValue() + ": " + ds.child("accountType").getValue());
                            if (accountType.equals("Seller")){
                                Seller seller = ds.getValue(Seller.class);
                                sellerArrayList.add(seller);
                            }
                        }
                        progressDialog.dismiss();

                        reOrderShopName();

                        ShopAdapter shopAdapter = new ShopAdapter(getContext(), sellerArrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(shopAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext() , "Action canceled", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}