package com.example.findandbuy;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.findandbuy.models.Seller;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.findandbuy.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<Seller> sellerArrayList;
    private ArrayList<LatLng> latLngArrayList;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        sellerArrayList = new ArrayList<>();
        latLngArrayList = new ArrayList<>();

        loadSellerLists();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_baseline_storefront_24))
                    .title(sellerArrayList.get(i).getShopName());
            b.include(latLng);
            googleMap.addMarker(markerOptions);
        }

//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(centerPoint));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 15), 5000, null);

        // Reference: https://stackoverflow.com/questions/22553792/how-set-zoom-level-on-google-map-dynamically-in-android
//        googleMap.setMaxZoomPreference(15);
        LatLngBounds bounds = b.build();
//Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25,25,5);
        mMap.animateCamera(cu);


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

        drawShopMarkers(mMap);
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
                        Toast.makeText(getApplicationContext() , "Action canceled", Toast.LENGTH_SHORT).show();
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