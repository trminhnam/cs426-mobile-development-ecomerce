package com.example.findandbuy.adapters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;
import com.example.findandbuy.models.Seller;
import com.example.findandbuy.seller.RegisterSellerActivity;
import com.example.findandbuy.seller.SellerMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SellerItemAdapter
        extends RecyclerView.Adapter<SellerItemAdapter.ItemHolder> {

    private Context context;
    public ArrayList<Item> itemArrayList;
    private String userType;

    private Item currentItem;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public SellerItemAdapter(Context context, ArrayList<Item> itemArrayList, String userType){
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.userType = userType;

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.row_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = itemArrayList.get(position);

        // get data
        String itemName = item.getItemName();
        String category = item.getItemCategory();
        String price = item.getItemPrice();
        String itemImage = item.getItemImage();
        String itemID = item.getItemID();

        // set data
        holder.itemNameTextView.setText(itemName);
        holder.categoryTextView.setText(category);
        holder.priceTextView.setText(price);

        // retrieve image
        try{
            Picasso.get().load(itemImage)
                    .placeholder(R.drawable.ic_image_gray)
                    .into(holder.itemImageView);
        }
        catch (Exception e){
            holder.itemImageView.setImageResource(R.drawable.ic_image_gray);
        }

        // User button
        if (userType.equals("User")){
            holder.addtoCartButton.setImageResource(R.drawable.ic_add_shopping_white);
        }
        else {
            holder.addtoCartButton.setImageResource(R.drawable.ic_edit_white);
        }
        holder.addtoCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserItemDetailDialog(item);
            }
        });

    }

    private int quantity = 1;
    private void showUserItemDetailDialog(Item item){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_user_detail_item, null);

        // init data store
        ImageView itemIv = view.findViewById(R.id.itemImageView);
        TextView itemNameTv = view.findViewById(R.id.itemNameTextView);
        TextView categoryTv = view.findViewById(R.id.categoryTextView);
        TextView descriptionTv = view.findViewById(R.id.descriptionTextView);
        TextView priceTv = view.findViewById(R.id.priceTextView);

        ImageButton incrementButton = view.findViewById(R.id.incrementItemButton);
        TextView itemCountTv = view.findViewById(R.id.itemCountTv);
        ImageButton decrementButton = view.findViewById(R.id.decrementItemButton);

        Button addItemButton = view.findViewById(R.id.addItemButton);

        //get data
        String itemImage = item.getItemImage();
        String itemName = item.getItemName();
        String itemCategory = item.getItemCategory();
        String itemDescription = item.getItemDescription();
        String itemPrice = item.getItemPrice();

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        try{
            Picasso.get().load(itemImage).placeholder(R.drawable.ic_baseline_shopping_cart_24).into(itemIv);
        }
        catch (Exception e){
            itemIv.setImageResource(R.drawable.ic_baseline_storefront_24);
        }

        itemNameTv.setText("" + itemName);
        categoryTv.setText("" + itemCategory);
        descriptionTv.setText("" + itemDescription);
        priceTv.setText("$ " + itemPrice);

        //increment
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                itemCountTv.setText("" + String.valueOf(quantity));
            }
        });

        //decrement
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity == 0)
                    return;
                else{
                    quantity--;
                    itemCountTv.setText("" + String.valueOf(quantity));
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        if (userType.equals("User")){
            quantity = 1;
            itemCountTv.setText("1");
            addItemButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_shopping_white, 0, 0, 0);
            addItemButton.setText("Add to cart");
            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItemToUserCart(item, itemCountTv.getText().toString());
                    dialog.dismiss();
                }
            });
        }
        else {
            quantity = Integer.parseInt(item.getItemCount());
            itemCountTv.setText("" + String.valueOf(quantity));
            addItemButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upload_white, 0, 0, 0);
            addItemButton.setText("Apply changes");
            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applySellerItemChanges(item, itemCountTv.getText().toString());
                    dialog.dismiss();
                }
            });
        }
    }

    private void addItemToUserCart(Item item, String newItemCount) {

        progressDialog.setMessage("Adding item to cart");
        progressDialog.show();

        item.setItemCount(newItemCount);

        HashMap<String, Object> newItemToCart = new HashMap<>();
        newItemToCart.put(item.getItemID(), item);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .child("Cart")
                .updateChildren(newItemToCart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void applySellerItemChanges(Item item, String newItemCount) {

        progressDialog.setMessage("Updating item");
        item.setItemCount(newItemCount);

        HashMap<String, Object> newdata = new HashMap<>();
        newdata.put(item.getItemID(), item);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Items")
                .updateChildren(newdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(context.getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context.getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;
        private TextView itemNameTextView, categoryTextView, priceTextView;
        private ImageButton addtoCartButton;

        public ItemHolder(@NonNull View view){
            super(view);

            itemImageView = view.findViewById(R.id.itemImageView);

            itemNameTextView = view.findViewById(R.id.itemNameTextView);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
            addtoCartButton = view.findViewById(R.id.addToCartButton);
        }
    }
}
