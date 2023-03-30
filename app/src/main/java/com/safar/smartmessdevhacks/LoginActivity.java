package com.safar.smartmessdevhacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safar.smartmessdevhacks.customer.CustomerMainActivity;
import com.safar.smartmessdevhacks.owner.OwnerMainActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etEmail;
    private TextInputEditText etPassword;
    private TextView tvForgetPassword, tvCustomer, tvOwner;

    private Button btnLogin;

    // Data
    private String email, password;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();

        registerListener();
    }

    private void initialize() {

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvOwner = findViewById(R.id.tvOwner);

        email = "";
        password = "";

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void registerListener() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getText();

                if (checkEmpty()) {
                    firebaseFirestore
                            .collection("User")
                            .document(email)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot document) {
                                    if (document.exists()) {
                                        String userType = document.get("userType").toString();

                                        if (userType.equals("Owner")) {
                                            firebaseAuth
                                                    .signInWithEmailAndPassword(email, password)
                                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            startActivity(new Intent(LoginActivity.this, OwnerMainActivity.class));
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        } else if (userType.equals("Customer")) {

                                            firebaseAuth
                                                    .signInWithEmailAndPassword(email, password)
                                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CustomerRegisterActivity.class));
            }
        });

        tvOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, OwnerRegisterPIActivity.class));
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Yet to build", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getText() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
    }

    private boolean checkEmpty() {

        if (email.equals("") || password.equals("")) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
}