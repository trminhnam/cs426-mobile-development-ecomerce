package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.Constants;
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

public class UserShopDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView shopIv;
    private TextView shopName, phoneNum, email, address;
    private ImageButton callButton, mapButton, backButton;
    private Spinner categorySpinner;

    private String shopUid;

    // TODO: Load from database
    private ArrayList<Item> listItems = new ArrayList<>();
    private ArrayList<Item> fillteredListItems = new ArrayList<>();
    private String[] listCategories = Constants.options;

    RecyclerView listItemRv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public UserShopDetailsFragment() {

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
    public static UserShopDetailsFragment newInstance(String param1, String param2) {
        UserShopDetailsFragment fragment = new UserShopDetailsFragment();
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.shopUid = bundle.getString("shopUid");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_shop_details, container, false);
        shopIv = view.findViewById(R.id.shopIv);
        shopName = view.findViewById(R.id.shopNameDetailTv);
        phoneNum = view.findViewById(R.id.phoneDetailTv);
        email = view.findViewById(R.id.emailDetailTv);
        address = view.findViewById(R.id.addressDetailTv);
        callButton = view.findViewById(R.id.callBtn);
        mapButton = view.findViewById(R.id.mapBtn);
        backButton = view.findViewById(R.id.backBtn);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        listItemRv = (RecyclerView) view.findViewById(R.id.listProductsRv);

//
//        ArrayAdapter ad = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listCategories);
//        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(ad);
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedCategory = listCategories[position];
//                if (selectedCategory.equals("All")) {
//                    fillteredListItems.clear();
//                    fillteredListItems.addAll(listItems);
//                } else {
//                    fillteredListItems.clear();
//                    for (Item item : listItems) {
//                        if (item.getItemCategory().equals(selectedCategory)) {
//                            fillteredListItems.add(item);
//                        }
//                    }
//                }
//                ((SellerItemAdapter) ((RecyclerView) getView()
//                        .findViewById(R.id.listProductsRv)).getAdapter())
//                        .notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        loadSellerItems();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // commit fragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void loadSellerItems() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(shopUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listItems.clear();
                        listItems = new ArrayList<>();
                        for (DataSnapshot ds: snapshot.getChildren()){
//                            String accountType = ""+ds.child("accountType").getValue();
//                            String fullname = ""+ds.child("fullname").getValue();
//                            if (accountType.equals("Seller")){
//                                progressDialog.dismiss();
//
//                            }
//                            else{
//                                progressDialog.dismiss();
//
//                            }

                            for (DataSnapshot dsItem: ds.child("Items").getChildren()){
                                Item item = dsItem.getValue(Item.class);
                                listItems.add(item);
                            }
                        }

                        SellerItemAdapter sellerItemAdapter = new SellerItemAdapter(getContext(), listItems, "User");
                        listItemRv.setAdapter(sellerItemAdapter);
                        listItemRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
