package com.safar.smartmessdevhacks.customer.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.R;
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
        binding.llAllData.removeAllViews();
        binding.llTopRatedData.removeAllViews();
        getAlldata();
        getTopRated();

    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void registerListener() {
        binding.rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.rbAll.getId() == i) {
                    getAlldata();
                } else if (binding.rbLocation.getId() == i) {
                    getLocationData();
                } else if (binding.rbVeg.getId() == i) {
                    getCategoryData("Veg");
                } else if (binding.rbNonVeg.getId() == i) {
                    getCategoryData("NonVeg");
                } else if (binding.rbVegan.getId() == i) {
                    getCategoryData("Vegan");
                }
            }
        });
    }

    private void getTopRated() {
        //customer_mess_top_rated_layout
        firebaseFirestore
                .collection("Owner")
                .orderBy("avgReview")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Owner owner = documentSnapshot.toObject(Owner.class);

                            Log.d(TAG, "onSuccess: " + owner);

                            String messName = owner.getMessName();
                            String email = owner.getEmail();
                            double avgReview = owner.getAvgReview();

                            createTopRatedCard(messName, avgReview, email);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createTopRatedCard(String messName, double avgReview, String email) {
        binding.llTopRatedData.removeAllViews();
        View topRatedMessView = getLayoutInflater().inflate(R.layout.customer_mess_top_rated_layout, null, false);

        TextView tvMessName = topRatedMessView.findViewById(R.id.tvMessName);
        TextView tvEmail = topRatedMessView.findViewById(R.id.tvEmail);
        RatingBar rbStar = topRatedMessView.findViewById(R.id.rbStar);
        LinearLayout llMain = topRatedMessView.findViewById(R.id.llMain);

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MessInfoActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        tvMessName.setText(messName);
        tvEmail.setText(email);

        rbStar.setRating((float) avgReview);

        binding.llTopRatedData.addView(topRatedMessView);
    }

    private void getAlldata() {
        binding.llAllData.removeAllViews();
        firebaseFirestore
                .collection("Owner")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Owner owner = documentSnapshot.toObject(Owner.class);

                            Log.d(TAG, "onSuccess: " + owner);

                            String messName = owner.getMessName();
                            double avgReview = owner.getAvgReview();
                            String email = owner.getEmail();
                            String location = "";
                            GeoPoint geoPoint = owner.getGeoPoint();

                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);

                                    // Get the first address line and locality (city)
                                    String addressText = "";
                                    int maxAddressLineIndex = address.getMaxAddressLineIndex();
                                    if (maxAddressLineIndex >= 0) {
                                        addressText = address.getAddressLine(0);
                                        if (address.getLocality() != null) {
                                            addressText += ", " + address.getLocality();
                                            location = addressText;
                                        }
                                    }

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            createCard(messName, avgReview, email, location);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createCard(String messName, double avgReview, String email, String location) {
        Log.d(TAG, "createCard: " + messName + avgReview + email + location);
        View messAllView = getLayoutInflater().inflate(R.layout.customer_mess_all_layout, null, false);

        TextView tvMessName = messAllView.findViewById(R.id.tvMessName);
        TextView tvLocation = messAllView.findViewById(R.id.tvLocation);
        TextView tvEmail = messAllView.findViewById(R.id.tvEmail);
        RatingBar rbStar = messAllView.findViewById(R.id.rbStar);
        LinearLayout llMain = messAllView.findViewById(R.id.llMain);

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MessInfoActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        tvMessName.setText(messName);
        tvLocation.setText(location);
        tvEmail.setText(email);

        rbStar.setRating((float) avgReview);

        binding.llAllData.addView(messAllView);
    }

    private void getLocationData() {
        binding.llAllData.removeAllViews();
        firebaseFirestore
                .collection("Owner")
                .orderBy("geoHash")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Owner owner = documentSnapshot.toObject(Owner.class);

                            Log.d(TAG, "onSuccess: " + owner);

                            String messName = owner.getMessName();
                            double avgReview = owner.getAvgReview();
                            String email = owner.getEmail();
                            String location = "";
                            GeoPoint geoPoint = owner.getGeoPoint();

                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);

                                    // Get the first address line and locality (city)
                                    String addressText = "";
                                    int maxAddressLineIndex = address.getMaxAddressLineIndex();
                                    if (maxAddressLineIndex >= 0) {
                                        addressText = address.getAddressLine(0);
                                        if (address.getLocality() != null) {
                                            addressText += ", " + address.getLocality();
                                            location = addressText;
                                        }
                                    }

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            createCard(messName, avgReview, email, location);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getCategoryData(String category) {
        binding.llAllData.removeAllViews();
        firebaseFirestore
                .collection("Owner")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            Owner owner = documentSnapshot.toObject(Owner.class);

                            Log.d(TAG, "onSuccess: " + owner);

                            String messName = owner.getMessName();
                            double avgReview = owner.getAvgReview();
                            String email = owner.getEmail();
                            String location = "";
                            GeoPoint geoPoint = owner.getGeoPoint();

                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);

                                    // Get the first address line and locality (city)
                                    String addressText = "";
                                    int maxAddressLineIndex = address.getMaxAddressLineIndex();
                                    if (maxAddressLineIndex >= 0) {
                                        addressText = address.getAddressLine(0);
                                        if (address.getLocality() != null) {
                                            addressText += ", " + address.getLocality();
                                            location = addressText;
                                        }
                                    }

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            createCard(messName, avgReview, email, location);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentMessBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }
}