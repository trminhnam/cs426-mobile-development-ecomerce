package com.example.findandbuy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Seller;
import com.example.findandbuy.fragment.UserShopDetailsFragment;
import com.example.findandbuy.user.ShopDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {

    private Context context;
    public ArrayList<Seller> shopsList;

    public ShopAdapter(Context context, ArrayList<Seller> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }



    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout of row shop
        View view = LayoutInflater.from(context).inflate(R.layout.row_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, int position) {
        //get data from shop model
        Seller shop = shopsList.get(position);
        String uid = shop.getUid();
        String shopNames = shop.getShopName();
        String phoneNum = shop.getPhoneNumber();
        String address = shop.getAddress();
        String email = shop.getEmail();

        //set necessary data for holder
        holder.shopNameTv.setText(shopNames);
        holder.addressTv.setText(address);
        holder.phoneTv.setVisibility(View.INVISIBLE);
        holder.phoneTv.setText(phoneNum);
        Log.d("SHOP_ADAPTER", "Phone number: " + phoneNum);

        // auto show shop icon
        // replace shop image in the future
        String profileImage = "";
        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.store).into(holder.shopIv);
        }
        catch (Exception e){
            holder.shopIv.setImageResource(R.drawable.store);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Fragment fragment = new UserShopDetailsFragment();
                bundle.putString("shopUid", uid);
                bundle.putString("shopName", shopNames);
                bundle.putString("email", email);
                bundle.putString("address", address);
                bundle.putString("phoneNumber", phoneNum);
                fragment.setArguments(bundle);
                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopsList.size(); //number of records
    }

    //custom view holder
    class ShopHolder extends RecyclerView.ViewHolder{

        private ImageView shopIv;
        private AppCompatTextView shopNameTv, addressTv, phoneTv;
        public ShopHolder(@NonNull View itemView) {
            super(itemView);

            shopIv = itemView.findViewById(R.id.shopIv);
            shopNameTv = itemView.findViewById(R.id.shopNameTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
        }
    }
}
