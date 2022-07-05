package com.example.findandbuy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.HolderCartItem> {
    private Context context;
    private ArrayList<Item> cartItems;
    private ShoppingCartAdapter.updateTotalPrice callback;

    public ShoppingCartAdapter(Context context, ArrayList<Item> cartItems, ShoppingCartAdapter.updateTotalPrice callback) {
        this.context = context;
        this.cartItems = cartItems;
        this.callback = callback;
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
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(cartItems.get(position).getItemCount());
                if(count > 1)
                {
                    count--;
                    holder.itemCountTextView.setText(String.valueOf(count));

                    // update total price
                    cartItems.get(position).setItemCount(String.valueOf(count));
                    callback.onItemClicked(position);
                }
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                // remove item from cart
                cartItems.remove(position);
                // remove item from recycler view
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size(), cartItems);
                // update total price
                callback.onItemClicked(position);
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
