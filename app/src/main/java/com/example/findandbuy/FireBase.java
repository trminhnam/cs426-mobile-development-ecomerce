package com.example.findandbuy;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FireBase {
    private static FireBase INSTANCE = null;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private FireBase(String database) {
        firebaseDatabase = FirebaseDatabase.getInstance("https://findandbuy-701ca-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(database);
    }

    public static synchronized FireBase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FireBase("FindAndBuy");
        }
        return(INSTANCE);
    }

    public void addData(Object obj)
    {
        databaseReference.setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // ...
                }
            });
    }

    public DatabaseReference getDatabaseReference()
    {
        return databaseReference;
    }

    public void addData(String child, Object obj)
    {
        databaseReference.child(child).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ...
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                // ...
            }
        });
    }

    public void addData(String key, String data)
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(key).setValue(data);
                // after adding this data we are showing toast message.
//                System.out.println("data added");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                System.out.println("Fail to add data " + error);
            }
        });
    }

    public String getData(String key)
    {
        final String[] result = {""};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        result[0] = snapshot.child(key).getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Fail to add data " + error);
            }
        });

        return result[0];
    }

    public Object getData()
    {
        return databaseReference.get().getResult().getValue();
    }
}