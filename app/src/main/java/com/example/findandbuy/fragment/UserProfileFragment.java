package com.example.findandbuy.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {

    private static UserProfileFragment INSTANCE = null;

    private FirebaseAuth firebaseAuth;

    public UserProfileFragment() {
    }

    public static UserProfileFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserProfileFragment();
        }
        return INSTANCE;
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        TextView fullnameTextView = view.findViewById(R.id.fullNameProfileTv);
        TextView emailTextView = view.findViewById(R.id.emailProfileTv);
        TextView bonusTextView = view.findViewById(R.id.bonusProfileTv);

        loadUserProfile(fullnameTextView, emailTextView, bonusTextView);
        return view;
    }

    private void loadUserProfile(TextView fullNameTextView, TextView emailTextView, TextView bonusTextView) {
        if (getContext() == null) {
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
//                            String accountType = "" + ds.child("accountType").getValue();
                            String fullName = "" + ds.child("fullname").getValue();
                            String email = "" + ds.child("email").getValue();
                            String bonus = "" + ds.child("bonus").getValue();

                            fullNameTextView.setText(fullName);
                            emailTextView.setText(email);
                            bonusTextView.setText(bonus + " coin");
                            if (getContext() != null)
                                Toast.makeText(getContext(), "Load user profile successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (getContext() != null)
                            Toast.makeText(getContext(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}