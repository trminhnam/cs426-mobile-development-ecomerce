package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.LoginActivity;
import com.example.findandbuy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private TextView fullNameTextView, shopNameTextView, emailTextView, addressTextView, phoneNumberTextView;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProfileFragment newInstance(String param1, String param2) {
        SellerProfileFragment fragment = new SellerProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        fullNameTextView = view.findViewById(R.id.sellerFullnameProfileTextView);
        shopNameTextView = view.findViewById(R.id.sellerShopnameProfileTextView);
        emailTextView = view.findViewById(R.id.sellerEmailProfileTẽtView);
        addressTextView = view.findViewById(R.id.sellerAddressProfileTextView);
        phoneNumberTextView = view.findViewById(R.id.sellerPhoneNumberProfileTextView);

        loadSellerProfileFromFirebase();

        return view;
    }

    private void loadSellerProfileFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String fullname = ""+ds.child("fullname").getValue();
                            String email = ""+ds.child("email").getValue();
                            String address = ""+ds.child("address").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String phoneNumber = ""+ds.child("phoneNumber").getValue();

                            fullNameTextView.setText(fullname);
                            emailTextView.setText(email);
                            addressTextView.setText(address);
                            shopNameTextView.setText(shopName);
                            phoneNumberTextView.setText(phoneNumber);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}