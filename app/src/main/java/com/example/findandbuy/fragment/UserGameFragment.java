package com.example.findandbuy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.findandbuy.R;
import com.example.findandbuy.Sound;
import com.example.findandbuy.Sprite2D;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGameFragment extends Fragment {

    private static UserGameFragment INSTANCE = null;

    private TextView showCoinTV;
    private Integer userCoin=0;
    private Boolean isUserInGame=false;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private View view;

    public UserGameFragment() {
    }

    public static UserGameFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserGameFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_game, container, false);

        loadUserBonusFromFirebase();
        isUserInGame = true;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUserInGame = false;
    }

    private void loadUserBonusFromFirebase() {
        if (getContext() == null) {
            userCoin = 0;
            return;
        }
        progressDialog.setMessage("Loading bonus");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users")
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .child("bonus")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            userCoin = 0;
                            Toast.makeText(getContext(), "Error getting bonus. Set bonus to 0", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            userCoin = Integer.valueOf(task.getResult().getValue().toString());
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Bonus loaded", Toast.LENGTH_SHORT).show();
                        }
                        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.game_animation);
                        showCoinTV = (TextView) view.findViewById(R.id.showCoinTV);
                        showCoinTV.setText("Total coins: " + userCoin.toString());
                        relativeLayout.addView(new AnimationView(getActivity()));
                    }
                });
    }

    private void updateBonusToFirebase(String coins) {
        if (getContext() == null)
            return;
        progressDialog.setMessage("Updating bonus");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users")
                .child(Objects.requireNonNull(firebaseAuth.getUid()))
                .child("bonus")
                .setValue(coins)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Bonus updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private class AnimationView extends View {
        private ArrayList<Sprite2D> sprites;

        // sensor variables
        private SensorManager mSensorManager;
        private float mAccel;
        private float mAccelCurrent;
        private float mAccelLast;

        // sound effect
        private Sound soundCoinDrop;

        private void initSensor() {
            mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

            mSensorManager.registerListener(mSensorListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);

            mAccel = 0.00f;
            mAccelCurrent = SensorManager.GRAVITY_EARTH;
            mAccelLast = SensorManager.GRAVITY_EARTH;
        }

        private void prepareContent()
        {
            sprites = new ArrayList<>();
            CreateAnimation(getWidthResources()/2, getHeightResources()/2 - getHeightResources()/8);
            soundCoinDrop = new Sound("coin Sound Effect", MediaPlayer.create(getContext(), R.raw.coinsoundeffect));
        }

        private void shaking() {
            if (getContext() == null || !isUserInGame)
                return;

            for (int i=0; i<sprites.size(); i++)
                sprites.get(i).update();

            userCoin += 1;
            showCoinTV.setText("Total coins: " + userCoin.toString());

            soundCoinDrop.getSource().start();
            invalidate();

            updateBonusToFirebase(String.valueOf(userCoin));
        }

        public AnimationView(Context context) {
            super(context);
            initSensor();
            prepareContent();
        }

        public AnimationView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initSensor();
            prepareContent();
        }

        public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initSensor();
        }

        public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initSensor();
            prepareContent();
        }

        private void CreateAnimation(int center_left, int center_top) {
            Bitmap[] bitmaps = new Bitmap[8];
            for (int i=0; i<bitmaps.length; i++)
                bitmaps[i] = BitmapFactory.decodeResource(getResources(), R.raw.frame_0+i);

            // get the width and height of the bitmap
            int width = bitmaps[0].getWidth();
            int height = bitmaps[0].getHeight();

            // set left and top to the middle of the screen
            center_left = center_left - width/2;
            center_top = center_top - height/2;

            Sprite2D newSprite = new Sprite2D(bitmaps, center_left, center_top, 0, 0);
            sprites.add(newSprite);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int i=0; i<sprites.size(); i++)
                sprites.get(i).draw(canvas);
        }

        private void selectSprite(int newIndex) {
            for (int i=0; i<sprites.size(); i++)
                if (i == newIndex)
                    sprites.get(i).State = 1;
                else
                    sprites.get(i).State = 0;

        }

        private int getSelectedSpriteIndex(float x, float y) {
            for (int i=sprites.size()-1;i>=0; i--)
                if (sprites.get(i).isSelected(x, y))
                    return i;
            return -1;
        }

        private int getHeightResources() {
            return getResources().getDisplayMetrics().heightPixels;
        }

        private int getWidthResources() {
            return getResources().getDisplayMetrics().widthPixels;
        }

        private final SensorEventListener mSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 12) {
//                    Toast.makeText(getContext().getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                    Log.d("Shake event", "Shake event detected");
                    shaking();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

    }
}