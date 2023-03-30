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
import com.safar.smartmessdevhacks.model.Owner;
import com.safar.smartmessdevhacks.model.User;
import com.safar.smartmessdevhacks.owner.OwnerMainActivity;

public class OwnerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "OwnerRegisterActivity";
    private EditText etName, etMessName, etPhoneNumber, etUpi, etEmail, etPassword, etLocation;
    private Button btnGetLocation, btnRegister;
    private TextView tvLogin;
    private Spinner spMessType;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String name, messName, phoneNumber, upi, email, password, location, messType;

    private void init() {
        initialize();

        registerListener();

        setUpDropDown();

    }

    private void initialize() {
        etName = findViewById(R.id.etName);
        etMessName = findViewById(R.id.etMessName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUpi = findViewById(R.id.etUpi);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etLocation = findViewById(R.id.etLocation);

        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnRegister = findViewById(R.id.btnRegister);

        spMessType = findViewById(R.id.spMessType);

        tvLogin = findViewById(R.id.tvLogin);

        name = messName = phoneNumber = upi = email = password = location = messType = "";

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
                Toast.makeText(OwnerRegisterActivity.this, "Yet to Build", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (checkEmpty()) {
                    firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    firebaseFirestore
                                            .collection("User")
                                            .document(email)
                                            .set(new User(email, "Owner"))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    String geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(18.455799, 73.866631));

                                                    Log.d(TAG, "onSuccess: "+messType);

                                                    firebaseFirestore
                                                            .collection("Owner")
                                                            .document(email)
                                                            .set(new Owner(name, email, messName, upi, geohash, phoneNumber, new GeoPoint(18.455799, 73.866631), messType))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    startActivity(new Intent(OwnerRegisterActivity.this, OwnerMainActivity.class));
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(OwnerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(OwnerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } else {
                    Toast.makeText(OwnerRegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnerRegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void getText() {
        name = etName.getText().toString().trim();
        messName = etMessName.getText().toString().trim();
        phoneNumber = etPhoneNumber.getText().toString().trim();
        upi = etUpi.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        location = etLocation.getText().toString().trim();
        messType = spMessType.getSelectedItem().toString();
    }

    private boolean checkEmpty() {

        if (messType.equals("Mess Type")) {
            return false;
        }
        if (name.equals("") || messName.equals("") || phoneNumber.equals("") || upi.equals("") || email.equals("") || password.equals("") || location.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);

        init();
    }
}