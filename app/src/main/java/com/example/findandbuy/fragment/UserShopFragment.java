package com.example.findandbuy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.ShopAdapter;
import com.example.findandbuy.models.Shop;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserShopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<Shop> shopsList;
    public UserShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserShopFragment newInstance(String param1, String param2) {
        UserShopFragment fragment = new UserShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shopsList = new ArrayList<Shop>();

        for (int i = 0; i < 10; ++i){
            Shop shop = new Shop("1", "a", "NFT1", "0708624730", "HocMon", "HCM", "VietNam", "122 Ba Diem, Hoc Mon, Ho Chi Minh City", "a", "1", "1", "a");
            shopsList.add(shop);
        }
        View view = inflater.inflate(R.layout.fragment_user_shop, container, false);
        recyclerView = view.findViewById(R.id.shopRv);
        ShopAdapter shopAdapter = new ShopAdapter(getContext(), shopsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopAdapter);

        return view;
    }
}