package com.example.findandbuy.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.Objects;

public class UserShopDetailsFragment extends Fragment {

    private ImageView shopIv;
    private TextView shopName, phoneNum, email, address;
    private ImageButton callButton, mapButton, backButton;
    private Spinner categorySpinner;

    private String shopUid, shopNameDetail, emailDetail, addressDetail;

    // TODO: Load from database
    private ArrayList<Item> listItems = new ArrayList<>();
    private ArrayList<Item> fillteredListItems = new ArrayList<>();
    private String[] listCategories = Constants.options;

    RecyclerView listItemRv;

    FirebaseAuth firebaseAuth;

    public UserShopDetailsFragment() {

    }

    public static UserShopDetailsFragment newInstance(String param1, String param2) {
        return new UserShopDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.shopUid = bundle.getString("shopUid");
            this.shopNameDetail = bundle.getString("shopName");
            this.emailDetail = bundle.getString("email");
            this.addressDetail = bundle.getString("address");
        }
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
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
        backButton = view.findViewById(R.id.backBtn);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        listItemRv = view.findViewById(R.id.listProductsRv);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("All");
        categories.addAll(Arrays.asList(listCategories));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        @SuppressLint("CutPasteId") Spinner spinner = view.findViewById(R.id.categorySpinner);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                setFillteredListItems(selectedItem);
                Objects.requireNonNull(listItemRv.getAdapter()).notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        //Set data for information
        shopName.setText(shopNameDetail);
        email.setText(emailDetail);
        address.setText(addressDetail);
        phoneNum.setText("12345678");

        listItemRv = (RecyclerView) view.findViewById(R.id.listProductsRv);

        loadSellerItems();

        backButton.setOnClickListener(view12 -> {
            // commit fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        callButton.setOnClickListener(view1 -> dialPhoneNumber(phoneNum.getText().toString().trim()));
        return view;
    }

    private void setFillteredListItems(String category) {
        fillteredListItems.clear();
        if (category.equals("All")) {
            fillteredListItems.addAll(listItems);
        } else {
            for (Item item : listItems) {
                if (item.getItemCategory().equals(category)) {
                    fillteredListItems.add(item);
                }
            }
        }
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
                            for (DataSnapshot dsItem: ds.child("Items").getChildren()){
                                Item item = dsItem.getValue(Item.class);
                                listItems.add(item);
                            }
                        }
                        fillteredListItems.clear();
                        fillteredListItems.addAll(listItems);
                        SellerItemAdapter sellerItemAdapter = new SellerItemAdapter(getContext(), fillteredListItems, "User");
                        listItemRv.setAdapter(sellerItemAdapter);
                        listItemRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        Log.d("phone1", "dialPhoneNumber: test" + phoneNumber);
//        getActivity().startActivity(intent);
//        Log.d("phone2", "dialPhoneNumber: test");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            requireActivity().startActivity(intent);
            Log.d("phone2", "dialPhoneNumber: test");
        }
    }
}
