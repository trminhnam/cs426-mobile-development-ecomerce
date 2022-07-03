package com.example.findandbuy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.findandbuy.R;
import com.example.findandbuy.adapters.SellerItemAdapter;
import com.example.findandbuy.models.Item;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerItemListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerItemListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ArrayList<Item> itemArrayList;

    public SellerItemListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerItemListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerItemListFragment newInstance(String param1, String param2) {
        SellerItemListFragment fragment = new SellerItemListFragment();
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
        itemArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Item item = new Item("1", "Book", "Others",
                            "20", "10", "Just a book",
                        "123", null, "123");
            itemArrayList.add(item);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_item_list, container, false);
        recyclerView = view.findViewById(R.id.itemRecyclerView);
        SellerItemAdapter sellerItemAdapter = new SellerItemAdapter(getContext(), itemArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(sellerItemAdapter);

        return view;
    }
}