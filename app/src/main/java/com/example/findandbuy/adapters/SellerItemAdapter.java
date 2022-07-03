package com.example.findandbuy.adapters;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class SellerItemAdapter
        extends RecyclerView.Adapter<SellerItemAdapter.ItemHolder> {

    private Context context;
    public ArrayList<Item> itemArrayList;

    public SellerItemAdapter(Context context, ArrayList<Item> itemArrayList){
        this.context = context;
        this.itemArrayList = itemArrayList;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ItemDetailActivity.class)
//                intent.putExtra("itemID", itemID);
//                context.startActivity(intent);
                // handle item click

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

        public ItemHolder(@NonNull View view){
            super(view);

            itemImageView = view.findViewById(R.id.itemImageView);

            itemNameTextView = view.findViewById(R.id.itemNameTextView);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            priceTextView = view.findViewById(R.id.priceTextView);
        }
    }
}
