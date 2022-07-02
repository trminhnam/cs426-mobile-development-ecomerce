package com.example.findandbuy.seller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import com.example.findandbuy.Constants;
import com.example.findandbuy.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SellerAddItem extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView productPhotoImageView;
    private EditText itemNameEditText;
    private EditText  itemCategoryEditText;
    private EditText itemPriceEditText;
    private EditText itemCountEditText;
    private EditText itemDescriptionEditText;

    private Button addProductButton;


    // permission constant
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    // image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    // permission array
    private String[] cameraPermissions;
    private String[] storagePermissions;

    // image picked uri
    private Uri image_uri;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_item);

        // init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        // init ui views
        backButton = findViewById(R.id.backButton);
        productPhotoImageView = findViewById(R.id.itemAddImageButton);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        itemCountEditText = findViewById(R.id.itemCountEditText);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);

        addProductButton = findViewById(R.id.addProductButton);

        cameraPermissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        storagePermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        productPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        itemCategoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaterogyDialig();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Flow: 1. input data, 2. Validate data, 3. add data to db
                inputData();

            }
        });

    }

    private String itemName, itemPrice, itemCategory, itemCount, itemDescription;
    private void inputData() {
        itemName = itemNameEditText.getText().toString();
        itemCategory = itemCategoryEditText.getText().toString();
        itemPrice = itemPriceEditText.getText().toString();
        itemCount = itemCountEditText.getText().toString();
        itemDescription = itemDescriptionEditText.getText().toString();

        if (itemName.isEmpty()){
            Toast.makeText(this, "Item name is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemCategory.isEmpty()){
            Toast.makeText(this, "Category is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemPrice.isEmpty()){
            Toast.makeText(this, "Price is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemCount.isEmpty()){
            Toast.makeText(this, "Quantity is required ...", Toast.LENGTH_SHORT).show();
            return;
        }

        addItem();
    }

    private void addItem() {
        progressDialog.setMessage("Adding Product ...");
        progressDialog.show();

        String timestampt = "" + System.currentTimeMillis();

        if (image_uri == null){
            // upload without image
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("itemID", ""+timestampt);
            hashMap.put("itemName", itemName);
            hashMap.put("itemCategory", itemCategory);
            hashMap.put("itemPrice", itemPrice);
            hashMap.put("itemCount", itemCount);
            hashMap.put("itemDescription", itemCount);

        }
        else{
            // upload with image

        }
    }

    private void showCaterogyDialig() {
        String[] options = Constants.options;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = options[which];
                        itemCategoryEditText.setText(category);
                    }
                })
                .show();
    }


    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            if (checkCameraPermission()){
                                pickFromCamera();
                            }
                            else{
                                requestCameraPermission();
                            }
                        }
                        else if (which == 1){
                            if (checkStoragePermission()){
                                pickFromGallery();
                            }
                            else{
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){

        // using media store to pick high quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        result &= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera & Storage permissions are required ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage permissions are required ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();

                productPhotoImageView.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                productPhotoImageView.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}