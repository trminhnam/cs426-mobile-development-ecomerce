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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserShopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<Seller> sellerArrayList;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public UserShopFragment() {
//        shopsList = new ArrayList<Shop>();
//
//        for (int i = 0; i < 10; ++i){
//            Shop shop = new Shop("1", "a", "NFT1", "0708624730", "HocMon", "HCM", "VietNam", "122 Ba Diem, Hoc Mon, Ho Chi Minh City", "a", "1", "1", "a");
//            shopsList.add(shop);
//        }
//        load
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserShopFragment newInstance(String param1, String param2) {
        UserShopFragment fragment = new UserShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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