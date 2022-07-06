package com.example.findandbuy.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.findandbuy.R;
import com.example.findandbuy.models.Seller;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomMapFragment extends Fragment {

    private static CustomMapFragment INSTANCE = null;

    MapView mMapView;
    private GoogleMap googleMap;
    private Marker currentMarker;
    private MarkerOptions currentMarkerOptions;
    private double cur_lat = 0.0, cur_lng = 0.0;

    private ArrayList<Seller> sellerArrayList;
    private ArrayList<LatLng> latLngArrayList;

    // permission arrays
    private String[] locationPermissions;
    private static final int LOCATION_REQUEST_CODE = 100;

    private boolean firstTime = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        currentMarkerOptions = new MarkerOptions();

        sellerArrayList = new ArrayList<>();
        latLngArrayList = new ArrayList<>();

        new DetectLocation();
    }

    public CustomMapFragment() {
        // Required empty public constructor
    }
    public static synchronized CustomMapFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomMapFragment();
        }
        return(INSTANCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            loadSellerLists();
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
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for( int i  = 0; i < latLngArrayList.size();i++){
            LatLng latLng = latLngArrayList.get(i);

            Log.d("LAT_LNG", "Latitude = " + latLng.latitude);
            Log.d("LAT_LNG", "Longitude = " + latLng.longitude);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_baseline_storefront_24))
                    .title(sellerArrayList.get(i).getShopName());
            b.include(latLng);
            googleMap.addMarker(markerOptions);
        }

        LatLngBounds bounds = b.build();
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 700,700,2);
        googleMap.animateCamera(cu);

        drawCurrentPosition();
        firstTime = false;
    }

    private void drawCurrentPosition()
    {
        if (currentMarker!=null) {
            currentMarker.remove();
            currentMarker=null;
        }
        LatLng latLng = new LatLng(cur_lat, cur_lng);
        currentMarker = googleMap.addMarker(currentMarkerOptions.position(latLng).icon(bitmapDescriptorFromVector(getContext(),
                R.drawable.ic_current_person_place)));
    }

    private void loadSellersLatLngLists() {
        latLngArrayList = new ArrayList<>();
        for (Seller seller: this.sellerArrayList){
            LatLng latLng = new LatLng(
                    Double.parseDouble(seller.getLat()),
                    Double.parseDouble(seller.getLng())
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
                                assert seller != null;
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
        // check context is null
        if (context == null) {
            return null;
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    class DetectLocation extends AppCompatActivity implements LocationListener {

        public DetectLocation() {
            detectLocation();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        detectLocation();
                    } else {
                        Toast.makeText(getContext(), "Location permission is required ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
//        LocationListener.super.onProviderDisabled(provider);
            Toast.makeText(getContext(), "Please turn on location...", Toast.LENGTH_SHORT).show();
        }
        

        @Override
        public void onLocationChanged(@NonNull Location location) {
            cur_lat = location.getLatitude();
            cur_lng = location.getLongitude();

            Log.d("REGISTER_LOCATION", "Latitude = " + cur_lat);
            Log.d("REGISTER_LOCATION", "Longitude = " + cur_lng);

            if (!firstTime)
                drawCurrentPosition();
        }

        private void detectLocation() {
            Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();

            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestLocationPermission();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
        }

        private void requestLocationPermission() {
            ActivityCompat.requestPermissions((Activity) requireContext(), locationPermissions, LOCATION_REQUEST_CODE);
        }
    }
}