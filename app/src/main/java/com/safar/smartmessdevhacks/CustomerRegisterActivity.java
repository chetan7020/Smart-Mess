package com.safar.smartmessdevhacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.safar.smartmessdevhacks.customer.CustomerMainActivity;
import com.safar.smartmessdevhacks.model.Customer;
import com.safar.smartmessdevhacks.model.Owner;
import com.safar.smartmessdevhacks.model.User;

public class CustomerRegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhoneNumber, etPassword;
    private Button btnRegister;
    private TextView tvLogin;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String name, email, phoneNumber, password;

    String item;

    private void init() {
        initialize();
        registerListener();
    }

    private void initialize() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);

        tvLogin = findViewById(R.id.tvLogin);

        name = email = phoneNumber = password = "";


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerListener() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerRegisterActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (checkEmpty()) {
                    if (!new Validation().emailValidation(email)) {
                        Toast.makeText(CustomerRegisterActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    } else if (!new Validation().phoneNumberValidation(phoneNumber)) {
                        Toast.makeText(CustomerRegisterActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    } else if (!new Validation().passwordValidation(password)) {
                        Toast.makeText(CustomerRegisterActivity.this, "Enter Strong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth
                                .createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        firebaseFirestore
                                                .collection("User")
                                                .document(email)
                                                .set(new User(email, "Customer"))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        firebaseFirestore
                                                                .collection("Customer")
                                                                .document(email)
                                                                .set(new Customer(name, email, phoneNumber))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        startActivity(new Intent(CustomerRegisterActivity.this, CustomerMainActivity.class));
                                                                        finish();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(CustomerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CustomerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CustomerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(CustomerRegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
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
        setContentView(R.layout.activity_customer_register);

        init();
    }
}