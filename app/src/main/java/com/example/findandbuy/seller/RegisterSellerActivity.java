package com.example.findandbuy.seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.findandbuy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegisterSellerActivity extends AppCompatActivity implements LocationListener {
    private ImageButton backButton, gpsButton;
    private EditText nameEt, phoneEt, shopName, streetEt, districtEt, cityEt, passwordEt, emailEt, completeAddress, confirmPasswordEt;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    // permission constants
    private static final int LOCATION_REQUEST_CODE = 100;

    // permission arrays
    private String[] locationPermissions;

    private LocationManager locationManager;
    private double lat = 0.0, lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);
        backButton = findViewById(R.id.backBtn);
        gpsButton = findViewById(R.id.gpsBtn);

        nameEt = findViewById(R.id.fullNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        shopName = findViewById(R.id.shopNameEt);
        streetEt = findViewById(R.id.streetEt);
        districtEt = findViewById(R.id.districtEt);
        cityEt = findViewById(R.id.cityEt);
        completeAddress = findViewById(R.id.completeAddressEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        registerButton = findViewById(R.id.register_btn);


        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    // already allowed
                    detectLocation();
                } else {
                    requestLocationPermission();
                }

//                lat = Math.random() * (10.875863 - 10.727280) + 10.727280;
//                lng = Math.random() * (106.702834 - 106.544569) + 106.544569;

//                Log.d("REGISTER_LOCATION", "Latitude = " + String.valueOf(lat));
//                Log.d("REGISTER_LOCATION", "Longtitude = " + String.valueOf(lng));

//                findAddress();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register seller
                inputData();
            }
        });
    }

    private String fullName, shopNames, phoneNum, street, district, city, address, email, password, confirmPassword;

    private void inputData() {
        //input
        fullName = nameEt.getText().toString().trim();
        shopNames = shopName.getText().toString().trim();
        phoneNum = phoneEt.getText().toString().trim();
        street = streetEt.getText().toString().trim();
        district = districtEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        address = completeAddress.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();

        //validate of data
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shopNames.isEmpty()) {
            Toast.makeText(this, "Shop name is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNum.isEmpty()) {
            Toast.makeText(this, "Phone number is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lat == 0.0 && lng == 0.0) {
            Toast.makeText(this, "Please click GPS button to locate", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Confirm password is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creating account");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        saverFirebaseData();
                        Toast.makeText(RegisterSellerActivity.this, "Create account successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterSellerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Saving account information.");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();

        // upload without image
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("email", "" + email);
        hashMap.put("fullname", "" + fullName);
        hashMap.put("shopName", "" + shopNames);
        hashMap.put("street", "" + street);
        hashMap.put("district", "" + district);
        hashMap.put("city", "" + city);
        hashMap.put("address", "" + address);
        hashMap.put("lat", "" + lat);
        hashMap.put("lng", "" + lng);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("accountType", "Seller");
        hashMap.put("available", "true");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterSellerActivity.this, SellerMainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    // Check location permission
    private boolean checkLocationPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        detectLocation();
                    } else {
                        Toast.makeText(this, "Location permission is required ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK){
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void detectLocation() {
        Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestLocationPermission();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // location detected
        lat = location.getLatitude();
        lng = location.getLongitude();
//        lat = Math.random() * (10.875863 - 10.727280) + 10.727280;
//        lng = Math.random() * (106.702834 - 106.544569) + 106.544569;

        Log.d("REGISTER_LOCATION", "Latitude = " + String.valueOf(lat));
        Log.d("REGISTER_LOCATION", "Longtitude = " + String.valueOf(lng));

        findAddress();
    }

    private void findAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            districtEt.setText(city);
            cityEt.setText(state);
            completeAddress.setText(address);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

//    @Override
//    public void onLocationChanged(@NonNull List<Location> locations) {
//        LocationListener.super.onLocationChanged(locations);
//    }
//
//    @Override
//    public void onFlushComplete(int requestCode) {
//        LocationListener.super.onFlushComplete(requestCode);
//    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
//        LocationListener.super.onProviderDisabled(provider);
        Toast.makeText(this, "Please turn on location...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}