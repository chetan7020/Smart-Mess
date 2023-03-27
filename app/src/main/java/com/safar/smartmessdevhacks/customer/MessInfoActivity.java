package com.safar.smartmessdevhacks.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.safar.smartmessdevhacks.R;

public class MessInfoActivity extends AppCompatActivity {

    private static final String TAG = "MessInfoActivity";

    private ImageView ivAddReview, ivCall, ivBack;

    private RatingBar rbStar;
    private String email;

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

        email = getIntent().getExtras().getString("email");
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
    }

    private void getOwnerInfo() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_info);

        init();
    }
}