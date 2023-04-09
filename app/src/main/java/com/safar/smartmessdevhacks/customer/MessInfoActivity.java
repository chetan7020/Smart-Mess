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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.model.Menu;
import com.safar.smartmessdevhacks.model.Owner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessInfoActivity extends AppCompatActivity {

    private static final String TAG = "MessInfoActivity";
    private TextView tvMessName, tvLocation;
    private ImageView ivAddReview, ivCall, ivBack;
    private LinearLayout llData;
    private RelativeLayout rlLocation, rlPay;
    private RatingBar rbStar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String email, messName, phoneNumber, upi;
    private double avgReview;
    private GeoPoint geoPoint;

    private void init() {
        initialize();
        registerListener();
        getOwnerInfo();
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

        llData = findViewById(R.id.llData);

        email = getIntent().getExtras().getString("email");
        messName = phoneNumber = upi = "";
        avgReview = 0.0;


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerListener() {

        firebaseFirestore
                .collection("Owner")
                .document(email)
                .collection("Menu")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentChange change : value.getDocumentChanges()) {

                                Menu menu = change.getDocument().toObject(Menu.class);
                                String id = menu.getId();
                                String menuName = menu.getMenuName();
                                String note = menu.getNote();
                                int price = menu.getPrice();
                                ArrayList<String> contents = menu.getContents();

                                switch (change.getType()) {
                                    case ADDED:
                                        createCard(id, menuName, note, price, contents);
                                        break;
                                    case MODIFIED:
                                        updateCard(id, menuName, note, price, contents);
                                        break;
                                }
                            }
                        }
                    }
                });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                MessInfoActivity.super.onBackPressed();
            }
        });
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

                String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + messName + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);

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

    private void createCard(String id, String menuName, String note, int price, ArrayList<String> contents) {
        View menuCardView = getLayoutInflater().inflate(R.layout.customer_mess_info_dish_layout, null, false);

        TextView tvID = menuCardView.findViewById(R.id.tvID);
        TextView tvMenuName = menuCardView.findViewById(R.id.tvMenuName);
        TextView tvContents = menuCardView.findViewById(R.id.tvContents);
        TextView tvNote = menuCardView.findViewById(R.id.tvNote);
        TextView tvPrice = menuCardView.findViewById(R.id.tvPrice);

        tvID.setText(id);
        tvMenuName.setText(menuName);
        tvNote.setText(note);
        tvPrice.setText("Rs." + price);

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < contents.size(); i++) {

            if (i != (contents.size() - 1)) {
                content.append(i + 1).append(". ").append(contents.get(i)).append("\n");
            } else {
                content.append(i + 1).append(". ").append(contents.get(i));
            }
        }

        tvContents.setText(content.toString());

        llData.addView(menuCardView);
    }

    private void updateCard(String id, String menuName, String note, int price, ArrayList<String> contents) {

        for (int i = 0; i < llData.getChildCount(); i++) {

            TextView tvID = llData.getChildAt(i).findViewById(R.id.tvID);
            TextView tvMenuName = llData.getChildAt(i).findViewById(R.id.tvMenuName);
            TextView tvNote = llData.getChildAt(i).findViewById(R.id.tvNote);
            TextView tvPrice = llData.getChildAt(i).findViewById(R.id.tvPrice);
            TextView tvContents = llData.getChildAt(i).findViewById(R.id.tvContents);

            if (tvID.getText().toString().trim().equals(id)) {

                tvMenuName.setText(menuName);
                tvNote.setText(note);
                tvPrice.setText("Rs." + price);

                StringBuilder content = new StringBuilder();

                for (int j = 0; j < contents.size(); j++) {

                    if (j != (contents.size() - 1)) {
                        content.append(j + 1).append(". ").append(contents.get(j)).append("\n");
                    } else {
                        content.append(j + 1).append(". ").append(contents.get(j));
                    }
                }

                Log.d(TAG, "updateCard: "+content);

                tvContents.setText(content.toString());

                break;
            }

        }

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
                        messName = owner.getMessName();
                        phoneNumber = owner.getPhoneNumber();
                        upi = owner.getUpi();
                        geoPoint = owner.getGeoPoint();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Geocoder geocoder = new Geocoder(MessInfoActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(owner.getGeoPoint().getLatitude(), owner.getGeoPoint().getLongitude(), 1);
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        tvLocation.setText(address.getSubLocality() + "-" + address.getLocality() + ", " + address.getPostalCode());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                        rbStar.setRating((float) avgReview);
                        tvMessName.setText(messName);

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_info);

        init();
    }
}