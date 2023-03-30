package com.safar.smartmessdevhacks.customer.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.SplashActivity;
import com.safar.smartmessdevhacks.customer.MessInfoActivity;
import com.safar.smartmessdevhacks.databinding.CustomerFragmentMessBinding;
import com.safar.smartmessdevhacks.model.Owner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MessFragment extends Fragment {

    private static final String TAG = "MessFragment";
    private CustomerFragmentMessBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();
        registerListener();

        if (SplashActivity.allQueryDocumentSnapshots == null) {
            Log.d(TAG, "init: Hello");
            getAllData();
        } else {
            displayAllData();
        }

        if (SplashActivity.topRatedQueryDocumentSnapshots == null) {
            Log.d(TAG, "init: Hello");
            getTopRatedData();
        }

    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void registerListener() {


        binding.svSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.svSearch.setIconified(false);
            }
        });

        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                for (int j = 0; j < binding.llAllData.getChildCount(); j++) {
                    TextView tvMessName = binding.llAllData.getChildAt(j).findViewById(R.id.tvMessName);
                    LinearLayout llMain = binding.llAllData.getChildAt(j).findViewById(R.id.llMain);

                    if (!(tvMessName.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {
                        llMain.setVisibility(View.GONE);
                    } else {
                        llMain.setVisibility(View.VISIBLE);
                    }
                }

                return false;
            }
        });

        binding.rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.rbAll.getId() == i) {
                    Log.d(TAG, "rbAll");
                    for (int j = 0; j < binding.llAllData.getChildCount(); j++) {
                        LinearLayout llMain = binding.llAllData.getChildAt(j).findViewById(R.id.llMain);
                        llMain.setVisibility(View.VISIBLE);
                    }
                } else if (binding.rbVegan.getId() == i) {
                    Log.d(TAG, "rbVegan");
                    filter("Vegan");
                } else if (binding.rbVeg.getId() == i) {
                    Log.d(TAG, "rbVeg");
                    filter("Veg");
                } else if (binding.rbNonVeg.getId() == i) {
                    Log.d(TAG, "rbNonVeg");
                    filter("NonVeg");
                    ;
                } else if (binding.rbRating.getId() == i) {
                    makeToast("Yet to Build");
                }
            }
        });
    }

    private void getAllData() {
        firebaseFirestore
                .collection("Owner")
                .orderBy("geoHash")
                .limit(30)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        SplashActivity.allQueryDocumentSnapshots = queryDocumentSnapshots;
                        displayAllData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast(e.getMessage());
                    }
                });
    }

    private void displayAllData() {

        if (SplashActivity.allQueryDocumentSnapshots.getDocuments().stream().count() != 0) {

            for (QueryDocumentSnapshot documentSnapshot : SplashActivity.allQueryDocumentSnapshots) {
                Owner owner = documentSnapshot.toObject(Owner.class);

                View view = getLayoutInflater().inflate(R.layout.customer_mess_all_layout, null, false);

                TextView tvMessName = view.findViewById(R.id.tvMessName);
                TextView tvLocation = view.findViewById(R.id.tvLocation);
                TextView tvMessType = view.findViewById(R.id.tvMessType);
                TextView tvEmail = view.findViewById(R.id.tvEmail);

                LinearLayout llMain = view.findViewById(R.id.llMain);

                RatingBar rbStar = view.findViewById(R.id.rbStar);

                tvMessName.setText(owner.getMessName());
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(owner.getGeoPoint().getLatitude(), owner.getGeoPoint().getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        tvLocation.setText(address.getSubLocality() + "-" + address.getLocality() + ", " + address.getPostalCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tvMessType.setText(owner.getMessType());
                tvEmail.setText(owner.getEmail());
                rbStar.setRating((float) owner.getAvgReview());

                llMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MessInfoActivity.class);
                        intent.putExtra("email", tvEmail.getText());
                        startActivity(intent);
                    }
                });

                binding.llAllData.addView(view);
            }
        }
    }

    private void getTopRatedData() {
        firebaseFirestore
                .collection("Owner")
                .orderBy("avgReview")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        SplashActivity.topRatedQueryDocumentSnapshots = queryDocumentSnapshots;
                        Log.d(TAG, "onSuccess: topRatedQueryDocumentSnapshots setup done");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast(e.getMessage());
                    }
                });
    }

    private void displayTopRatedData() {

    }

    private void filter(String query) {
        for (int j = 0; j < binding.llAllData.getChildCount(); j++) {
            TextView tvMessType = binding.llAllData.getChildAt(j).findViewById(R.id.tvMessType);
            LinearLayout llMain = binding.llAllData.getChildAt(j).findViewById(R.id.llMain);

            if (!(tvMessType.getText().toString().toLowerCase().trim().equals(query.toLowerCase()))) {
                llMain.setVisibility(View.GONE);
            } else {
                llMain.setVisibility(View.VISIBLE);
            }
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentMessBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }
}