package com.example.findandbuy.adapters;

import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Item;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

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

        holder.addtoCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserItemDetailDialog(item);
            }
        });

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

    private int quantity = 0;
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

        AppCompatButton addItemButton = view.findViewById(R.id.addItemButton);

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
        priceTv.setText("$" + itemPrice);
        itemCountTv.setText("" + String.valueOf(quantity));

        AlertDialog dialog = builder.create();
        dialog.show();

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

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //have to insert add to cart function related to database
                quantity = 0;
                dialog.dismiss();
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
