package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.SellerItemAdapter;
import com.example.findandbuy.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SellerItemListFragment extends Fragment {

    private static SellerItemListFragment INSTANCE = null;

    public SellerItemListFragment() {
        // Required empty public constructor
    }

    public static SellerItemListFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SellerItemListFragment();
        }
        return INSTANCE;
    }

    private RecyclerView recyclerView;
    private ArrayList<Item> itemArrayList;
    private SellerItemAdapter sellerItemAdapter;

    private FirebaseAuth firebaseAuth;

    public static SellerItemListFragment newInstance(String param1, String param2) {
        return new SellerItemListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        
        loadAllItems();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_item_list, container, false);
        recyclerView = view.findViewById(R.id.itemRecyclerView);

        loadAllItems();

        return view;
    }

    private void loadAllItems() {
        itemArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Item item = ds.getValue(Item.class);
                            itemArrayList.add(item);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);

                        sellerItemAdapter = new SellerItemAdapter(getContext(), itemArrayList, "Seller");
                        recyclerView.setAdapter(sellerItemAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Action canceled", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}