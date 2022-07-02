package com.example.findandbuy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findandbuy.R;
import com.example.findandbuy.models.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder>{

    private Context context;
    public ArrayList<Shop> shopsList;

    public ShopAdapter(Context context, ArrayList<Shop> shopsList) {
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
        Shop shop = shopsList.get(position);
        //uid, fullName, shopNames, phoneNum, street, district, city, address, email, password, confirmPassword, profileImage;
        String uid = shop.getUid();
        String fullName = shop.getFullName();
        String shopNames = shop.getShopNames();
        String phoneNum = shop.getPhoneNum();
        String district = shop.getDistrict();
        String city = shop.getCity();
        String address = shop.getAddress();
        String email = shop.getEmail();
        String profileImage = shop.getProfileImage();

        //set necessary data for holder
        holder.shopNameTv.setText(shopNames);
        holder.addressTv.setText(address);
        holder.phoneTv.setText(phoneNum);

        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_baseline_storefront_24).into(holder.shopIv);
        }
        catch (Exception e){
            holder.shopIv.setImageResource(R.drawable.ic_baseline_storefront_24);
        }
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
