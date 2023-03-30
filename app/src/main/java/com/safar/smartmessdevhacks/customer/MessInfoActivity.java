package com.safar.smartmessdevhacks.customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.model.Owner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MessInfoActivity extends AppCompatActivity {

    private static final String TAG = "MessInfoActivity";
    private TextView tvMessName, tvLocation;
    private ImageView ivAddReview, ivCall, ivBack;
    private RelativeLayout rlLocation, rlPay;
    private RatingBar rbStar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String email;
    private double avgReview = 0.0;
    private String messName = "";
    private String phoneNumber = "";
    private String upi = "";
    private String location = "";
    private GeoPoint geoPoint;

    private void init() {
        initialize();
        registerListener();

        getOwnerInfo();

        getDish();
    }

    private void initialize() {
        ivAddReview = findViewById(R.id.ivAddReview);
        ivCall = findViewById(R.id.ivCall);
        ivBack = findViewById(R.id.ivBack);

        rbStar = findViewById(R.id.rbStar);

        tvMessName = findViewById(R.id.tvMessName);
        tvLocation = findViewById(R.id.tvLocation);

        rlLocation = findViewById(R.id.rlLocation);
        rlPay = findViewById(R.id.rlPay);

        email = getIntent().getExtras().getString("email");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerListener() {
        ivAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessInfoActivity.this, ReviewActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        rlLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = geoPoint.getLatitude();
                double longitude = geoPoint.getLongitude();


            }
        });

        rlPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MessInfoActivity.this, "UPI : " + upi, Toast.LENGTH_SHORT).show();
            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MessInfoActivity.this, "No phone app found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getOwnerInfo() {
        firebaseFirestore
                .collection("Owner")
                .document(email)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Owner owner = value.toObject(Owner.class);

                        avgReview = owner.getAvgReview();
//                        String email = owner.getEmail();
                        messName = owner.getMessName();
                        phoneNumber = owner.getPhoneNumber();
                        upi = owner.getUpi();
                        location = "";
                        geoPoint = owner.getGeoPoint();

                        Geocoder geocoder = new Geocoder(MessInfoActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(owner.getGeoPoint().getLatitude(), owner.getGeoPoint().getLongitude(), 1);
                            if (addresses != null && addresses.size() > 0) {
                                Address address = addresses.get(0);
                                location = address.getSubLocality() + "-" + address.getLocality() + ", " + address.getPostalCode();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        rbStar.setRating((float) avgReview);
                        tvMessName.setText(messName);
                        tvLocation.setText(location);

                    }
                });
    }

    private void getDish() {
//        firebaseFirestore
//                .collection("Owner")
//                .document(email)
//                .collection("Dish")
//                .add
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_info);

        init();
    }
}