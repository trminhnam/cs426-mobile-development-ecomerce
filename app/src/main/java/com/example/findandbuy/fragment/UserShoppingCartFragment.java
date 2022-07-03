package com.example.findandbuy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.findandbuy.adapters.ShoppingCartAdapter;
import com.example.findandbuy.models.Item;

import com.example.findandbuy.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserShoppingCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserShoppingCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ShoppingCartAdapter shoppingCartAdapter;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private RecyclerView recyclerView;

    private ArrayList<Item> itemsList = new ArrayList<>();

    public UserShoppingCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserShoppingCartFragment newInstance(String param1, String param2) {
        UserShoppingCartFragment fragment = new UserShoppingCartFragment();
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

    private final ShoppingCartAdapter.updateTotalPrice callback = (position) -> {
        // get total price
        double totalPrice = 0;
        for(Item item : itemsList)
        {
            totalPrice += Double.parseDouble(item.getItemPrice()) * Double.parseDouble(item.getItemCount());
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // fake data for shopping cart
        for(int i = 0; i < 5; ++i)
        {
            Item item = new Item("1", "Iphone", "phone", "122.0", "1", "HELLO", "123", "1232", "123");
            itemsList.add(item);
        }

        View view = inflater.inflate(R.layout.fragment_user_shopping_cart, container, false);
        recyclerView = view.findViewById(R.id.shoppingCartRv);
        totalPriceTextView = view.findViewById(R.id.totalTv);
        checkoutButton = view.findViewById(R.id.checkoutBtn);


        ShoppingCartAdapter adapter = new ShoppingCartAdapter(getContext(), itemsList, callback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}