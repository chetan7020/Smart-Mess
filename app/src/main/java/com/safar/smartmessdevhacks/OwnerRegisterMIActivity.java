package com.safar.smartmessdevhacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.safar.smartmessdevhacks.comman.Validation;
import com.safar.smartmessdevhacks.model.Owner;
import com.safar.smartmessdevhacks.model.User;
import com.safar.smartmessdevhacks.owner.OwnerMainActivity;

public class OwnerRegisterMIActivity extends AppCompatActivity {

    private static final String TAG = "OwnerRegisterActivity";
    private EditText etMessName, etUpi, etLocation;
    private Button btnGetLocation, btnRegister;
    private TextView tvLogin;
    private Spinner spMessType;
    private String messName, upi, location, messType;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private void init() {
        initialize();

        registerListener();

        setUpDropDown();

    }

    private void initialize() {
        etMessName = findViewById(R.id.etMessName);
        etUpi = findViewById(R.id.etUpi);
        etLocation = findViewById(R.id.etLocation);

        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnRegister = findViewById(R.id.btnRegister);

        spMessType = findViewById(R.id.spMessType);

        tvLogin = findViewById(R.id.tvLogin);

        messName = upi = location = messType = "";

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setUpDropDown() {
        String[] messType = {"Mess Type", "Veg", "NonVeg", "Vegan"};

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, messType);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spMessType.setAdapter(ad);
    }

    private void registerListener() {
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OwnerRegisterMIActivity.this, "Yet to Build", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if (checkEmpty()) {
                    if (!new Validation().upiValidation(upi)) {
                        Toast.makeText(OwnerRegisterMIActivity.this, "Enter Valid UPI", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth
                                .createUserWithEmailAndPassword(OwnerRegisterPIActivity.email, OwnerRegisterPIActivity.password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        firebaseFirestore
                                                .collection("User")
                                                .document(OwnerRegisterPIActivity.email)
                                                .set(new User(OwnerRegisterPIActivity.email, "Owner"))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        String geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(18.455799, 73.866631));

                                                        Log.d(TAG, "onSuccess: " + messType);
                                                        firebaseFirestore
                                                                .collection("Owner")
                                                                .document(OwnerRegisterPIActivity.email)
                                                                .set(new Owner(OwnerRegisterPIActivity.name, OwnerRegisterPIActivity.email, messName, upi, geohash, OwnerRegisterPIActivity.phoneNumber, messType, new GeoPoint(18.455799, 73.866631)))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        startActivity(new Intent(OwnerRegisterMIActivity.this, OwnerMainActivity.class));
                                                                        finish();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(OwnerRegisterMIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(OwnerRegisterMIActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                } else {
                    Toast.makeText(OwnerRegisterMIActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnerRegisterMIActivity.this, LoginActivity.class));
            }
        });
    }

    private void getText() {
        messName = etMessName.getText().toString().trim();
        upi = etUpi.getText().toString().trim();
        location = etLocation.getText().toString().trim();
        messType = spMessType.getSelectedItem().toString();
    }

    private boolean checkEmpty() {

        if (messType.equals("Mess Type")) {
            return false;
        }
        if (messName.equals("") || upi.equals("") || location.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register_mi);

        init();
    }
}