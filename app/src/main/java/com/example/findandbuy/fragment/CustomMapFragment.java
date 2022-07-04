package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.findandbuy.R;
import com.example.findandbuy.databinding.ActivityMapsBinding;
import com.example.findandbuy.models.Seller;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomMapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<Seller> sellerArrayList;
    private ArrayList<LatLng> latLngArrayList;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        sellerArrayList = new ArrayList<>();
        latLngArrayList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


//                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                loadSellerLists();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void drawShopMarkers(GoogleMap googleMap) {
//        Double avgLat = new Double(0.0), avgLng = new Double(0.0);
//        Log.d("LAT_LNG", "AVG Latitude = " + avgLat);
//        Log.d("LAT_LNG", "AVG Longtitude = " + avgLng);
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for( int i  = 0; i < latLngArrayList.size();i++){
            LatLng latLng = latLngArrayList.get(i);
//            avgLat = avgLat + latLng.latitude;
//            avgLng = avgLng + latLng.longitude;
            Log.d("LAT_LNG", "Latitude = " + latLng.latitude);
            Log.d("LAT_LNG", "Longtitude = " + latLng.longitude);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_storefront_24))
                    .title(sellerArrayList.get(i).getShopName());
            b.include(latLng);
            googleMap.addMarker(markerOptions);
        }

        // Reference: https://stackoverflow.com/questions/22553792/how-set-zoom-level-on-google-map-dynamically-in-android
        LatLngBounds bounds = b.build();
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 700,700,2);
        googleMap.animateCamera(cu);
    }

    private void loadSellersLatLngLists() {
        latLngArrayList = new ArrayList<>();
        for (Seller seller: this.sellerArrayList){
            LatLng latLng = new LatLng(
                    Double.valueOf(seller.getLat()),
                    Double.valueOf(seller.getLng())
            );
            Log.d("LOAD_LAT_LNG", ""+Double.valueOf(seller.getLat()) + ", " + Double.valueOf(seller.getLng()));
            latLngArrayList.add(latLng);
        }

        drawShopMarkers(googleMap);
    }

    private void loadSellerLists() {
        sellerArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sellerArrayList.clear();
                        sellerArrayList = new ArrayList<>();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            Log.d("ACCOUNT_RETRIEVAL", "" + ds.child("email").getValue() + ": " + ds.child("accountType").getValue());
                            if (accountType.equals("Seller")){
                                Seller seller = ds.getValue(Seller.class);
                                sellerArrayList.add(seller);
                                Log.d("ACCOUNT_RETRIEVAL", seller.getEmail() + ": " + seller.getAccountType());
                                Log.d("ACCOUNT_RETRIEVAL", "Lat = " + seller.getLat() + ", Lng = " + seller.getLng());
                            }
                        }
                        loadSellersLatLngLists();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext() , "Action canceled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Reference https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}