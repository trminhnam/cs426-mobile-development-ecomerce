package com.example.findandbuy.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.HolderCartItem> {
    private Context context;
    private ArrayList<Item> cartItems;
    private ShoppingCartAdapter.updateTotalPrice callback;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public ShoppingCartAdapter(Context context, ArrayList<Item> cartItems, ShoppingCartAdapter.updateTotalPrice callback) {
        this.context = context;
        this.cartItems = cartItems;
        this.callback = callback;

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public interface updateTotalPrice{
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart, parent, false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, @SuppressLint("RecyclerView") int position) {

        Item item = cartItems.get(position);

        String title = item.getItemName();
        String price = item.getItemPrice();
        String shopName = item.getShopName();
        String itemCount = item.getItemCount();

        // set data to views
        holder.itemNameTextView.setText(title);
        holder.priceTextView.setText("$ " + price);
        holder.shopNameTextView.setText(shopName);
        holder.itemCountTextView.setText(itemCount);
        callback.onItemClicked(position);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(cartItems.get(position).getItemCount());
                count++;
                holder.itemCountTextView.setText(String.valueOf(count));

                // update total price
                cartItems.get(position).setItemCount(String.valueOf(count));
                callback.onItemClicked(position);

                updateItemInCartToFirebase(cartItems.get(position));
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(cartItems.get(position).getItemCount());
                if(count > 1)
                {
                    holder.btnMinus.setEnabled(true);
                    count--;
                    holder.itemCountTextView.setText(String.valueOf(count));

                    // update total price
                    cartItems.get(position).setItemCount(String.valueOf(count));
                    callback.onItemClicked(position);

                    updateItemInCartToFirebase(cartItems.get(position));
                }
                else {
                    holder.btnMinus.setEnabled(false);
                }
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                // remove item from cart
                removeItemInCartInFirebase(cartItems.get(position));
                cartItems.remove(position);
                // remove item from recycler view
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size(), cartItems);
                // update total price
                callback.onItemClicked(position);

            }
        });
    }

    private void removeItemInCartInFirebase(Item item) {
        progressDialog.setMessage("Removing item from cart");
        progressDialog.show();

        HashMap<String, Object> newData = new HashMap<>();
        newData.put(item.getItemID(), item);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User")
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .child("Cart")
                .child(item.getItemID())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
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

    private void updateItemInCartToFirebase(Item item) {
        progressDialog.setMessage("Updating cart");
        progressDialog.show();

        HashMap<String, Object> newData = new HashMap<>();
        newData.put(item.getItemID(), item);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User")
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .child("Cart")
                .updateChildren(newData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Cart updated", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // views holder class
    static class HolderCartItem extends RecyclerView.ViewHolder {

        // ui views for cart item row
        private TextView itemNameTextView, shopNameTextView, priceTextView, itemCountTextView;
        private ImageButton btnAdd, btnMinus, btnRemove;

        // constructor
        public HolderCartItem(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);

            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
