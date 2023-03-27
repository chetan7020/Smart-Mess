package com.safar.smartmessdevhacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safar.smartmessdevhacks.customer.CustomerMainActivity;
import com.safar.smartmessdevhacks.owner.OwnerMainActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: "+firebaseAuth.getCurrentUser().getEmail());
            firebaseFirestore
                    .collection("User")
                    .document(firebaseAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            String userType = document.get("userType").toString();

                            if (userType.equals("Owner")) {
                                startActivity(new Intent(SplashActivity.this, OwnerMainActivity.class));
                                finish();
                            } else if (userType.equals("Customer")) {
                                startActivity(new Intent(SplashActivity.this, CustomerMainActivity.class));
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}