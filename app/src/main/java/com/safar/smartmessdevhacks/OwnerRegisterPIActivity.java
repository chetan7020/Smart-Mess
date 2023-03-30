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

public class OwnerRegisterPIActivity extends AppCompatActivity {

    private static final String TAG = "OwnerRegisterActivity";
    private EditText etName, etPhoneNumber,etEmail, etPassword;
    private Button btnNext;
    private TextView tvLogin;

    public static String name, phoneNumber, email, password;

    private void init() {
        initialize();

        registerListener();

    }

    private void initialize() {
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnNext = findViewById(R.id.btnNext);

        tvLogin = findViewById(R.id.tvLogin);

        name = phoneNumber = email = password = "";

    }

    private void registerListener() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (checkEmpty()) {
                    startActivity(new Intent(OwnerRegisterPIActivity.this, OwnerRegisterMIActivity.class));
                } else {
                    Toast.makeText(OwnerRegisterPIActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnerRegisterPIActivity.this, LoginActivity.class));
            }
        });
    }

    private void getText() {
        name = etName.getText().toString().trim();
        phoneNumber = etPhoneNumber.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
    }

    private boolean checkEmpty() {

        if (name.equals("") || phoneNumber.equals("") || email.equals("") || password.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register_pi);

        init();
    }
}