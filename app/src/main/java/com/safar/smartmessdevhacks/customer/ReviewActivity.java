package com.safar.smartmessdevhacks.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safar.smartmessdevhacks.CustomerRegisterActivity;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.model.Customer;
import com.safar.smartmessdevhacks.model.Owner;
import com.safar.smartmessdevhacks.model.Review;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewActivity extends AppCompatActivity {

    private EditText etText;
    private MaterialRatingBar rbStar;
    private TextView tvName;
    private Button btnPost;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String text, email, currentUser;
    private double star;

    private void init() {
        initialize();
        registerListener();
        getUserData();
    }

    private void getUserData() {
        if (CustomerMainActivity.name == null) {
            firebaseFirestore
                    .collection("Customer")
                    .document(firebaseAuth.getCurrentUser().getEmail())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null) {

                                Customer customer = value.toObject(Customer.class);

                                CustomerMainActivity.name = customer.getName();
                                CustomerMainActivity.email = customer.getEmail();
                                CustomerMainActivity.phoneNumber = customer.getPhoneNumber();

                                tvName.setText(CustomerMainActivity.name);
                            } else if (error != null) {
                                makeToast(error.getMessage());
                            }
                        }
                    });
        } else {
            tvName.setText(CustomerMainActivity.name);
        }
    }

    private void initialize() {
        tvName = findViewById(R.id.tvName);
        etText = findViewById(R.id.descibe_review);
        rbStar = findViewById(R.id.rating_bar);
        btnPost = findViewById(R.id.btn_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("email");

        currentUser = firebaseAuth.getCurrentUser().getEmail();

        text = "";
    }

    private void registerListener() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
                if (checkEmpty()) {

                    firebaseFirestore
                            .collection("Owner")
                            .document(email)
                            .collection("Review")
                            .document(currentUser)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot document) {
                                    if (document.exists()) {
                                        Review review = document.toObject(Review.class);

                                        double previousStar = review.getStar();

                                        firebaseFirestore
                                                .collection("Owner")
                                                .document(email)
                                                .collection("Review")
                                                .document(currentUser)
                                                .set(new Review(currentUser, text, star))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        firebaseFirestore
                                                                .collection("Owner")
                                                                .document(email)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Owner owner = documentSnapshot.toObject(Owner.class);

                                                                        double reviewCount = owner.getReviewCount();
                                                                        int customerCount = owner.getCustomerCount();

                                                                        reviewCount = reviewCount + star - previousStar;

                                                                        Map<String, Object> data = new HashMap<>();
                                                                        data.put("reviewCount", reviewCount);
                                                                        data.put("avgReview", (reviewCount / customerCount));

                                                                        firebaseFirestore
                                                                                .collection("Owner")
                                                                                .document(email)
                                                                                .update(data)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Toast.makeText(ReviewActivity.this, "Review Posted", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {

                                        firebaseFirestore
                                                .collection("Owner")
                                                .document(email)
                                                .collection("Review")
                                                .document(currentUser)
                                                .set(new Review(currentUser, text, star))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        firebaseFirestore
                                                                .collection("Owner")
                                                                .document(email)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Owner owner = documentSnapshot.toObject(Owner.class);

                                                                        double reviewCount = owner.getReviewCount();
                                                                        int customerCount = owner.getCustomerCount();

                                                                        reviewCount = reviewCount + star;
                                                                        customerCount = customerCount + 1;

                                                                        Map<String, Object> data = new HashMap<>();
                                                                        data.put("reviewCount", reviewCount);
                                                                        data.put("customerCount", customerCount);
                                                                        data.put("avgReview", (reviewCount / customerCount));

                                                                        firebaseFirestore
                                                                                .collection("Owner")
                                                                                .document(email)
                                                                                .update(data)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Toast.makeText(ReviewActivity.this, "Review Posted", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                } else {
                    Toast.makeText(ReviewActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getText() {
        text = etText.getText().toString().trim();
        star = rbStar.getRating();
    }

    private boolean checkEmpty() {

        if (text.equals("")) {
            return false;
        }
        return true;
    }

    private void makeToast(String msg) {
        Toast.makeText(ReviewActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        init();
    }
}