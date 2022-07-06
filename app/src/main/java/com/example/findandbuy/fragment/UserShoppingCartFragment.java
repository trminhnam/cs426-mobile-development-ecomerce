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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.adapters.SellerItemAdapter;
import com.example.findandbuy.adapters.ShoppingCartAdapter;
import com.example.findandbuy.models.Item;

import com.example.findandbuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class UserShoppingCartFragment extends Fragment {

    private static UserShoppingCartFragment INSTANCE = null;

    private TextView totalPriceTextView;
    private Button checkoutButton;


    private RecyclerView recyclerView;
    private ArrayList<Item> itemsList = new ArrayList<>();
    private double totalPrice = 0;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public UserShoppingCartFragment() {
    }
    public static UserShoppingCartFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserShoppingCartFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_shopping_cart, container, false);
        recyclerView = view.findViewById(R.id.shoppingCartRv);
        totalPriceTextView = view.findViewById(R.id.totalTv);
        checkoutButton = view.findViewById(R.id.checkoutBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadSellerItems();

        return view;
    }

    private void loadSellerItems() {
        progressDialog.setMessage("Loading cart");
        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Cart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        itemsList.clear();
                        itemsList = new ArrayList<>();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Item item = ds.getValue(Item.class);
                            itemsList.add(item);
                        }

                        reOrderItemList();

                        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(getContext(), itemsList, callback);
                        recyclerView.setAdapter(shoppingCartAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Loaded successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void reOrderItemList()
    {
        // sort itemList by uID, itemName
        for(int i = 0; i < itemsList.size(); ++i)
        {
            for(int j = i + 1; j < itemsList.size(); ++j)
            {
                if(itemsList.get(i).getUid().compareTo(itemsList.get(j).getUid()) > 0)
                {
                    Item temp = itemsList.get(i);
                    itemsList.set(i, itemsList.get(j));
                    itemsList.set(j, temp);
                }
                else if(itemsList.get(i).getUid().compareTo(itemsList.get(j).getUid()) == 0)
                {
                    if(itemsList.get(i).getItemName().compareTo(itemsList.get(j).getItemName()) > 0)
                    {
                        Item temp = itemsList.get(i);
                        itemsList.set(i, itemsList.get(j));
                        itemsList.set(j, temp);
                    }
                }
            }
        }
    }

    private final ShoppingCartAdapter.updateTotalPrice callback = (position) -> {
        // get total price
        totalPrice = 0.0;
        for(Item item : itemsList)
        {
            totalPrice += Double.parseDouble(item.getItemPrice()) * Double.parseDouble(item.getItemCount());
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    };
}