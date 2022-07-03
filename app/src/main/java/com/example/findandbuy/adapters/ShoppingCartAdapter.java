package com.example.findandbuy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.HolderCartItem> {
    private Context context;
    private ArrayList<Item> cartItems;

    public ShoppingCartAdapter(Context context, ArrayList<Item> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_cart, parent, false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {

        Item item = cartItems.get(position);

        String title = item.getItemName();
        String price = item.getItemPrice();
        String category = item.getItemCategory();

        // set data to views
        holder.itemNameTextView.setText(title);
        holder.priceTextView.setText(price);
        holder.categoryTextView.setText(category);

    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }


    static class HolderCartItem extends RecyclerView.ViewHolder {

        private TextView itemNameTextView, categoryTextView, priceTextView;
        private Button btnAdd, btnMinus, btnRemove;

        public HolderCartItem(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);

            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
